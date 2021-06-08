package com.example.elasticsearch;


import com.example.elasticsearch.test.VO.EsReturnInfo;
import com.example.elasticsearch.test.entity.TestUser;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsRestTemplateTest {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    public void saveDoc() {
        saveDocByClass(TestUser.class);
    }

    public void saveDocByClass(Class<?> clazz)  {
        // 判断索引是否存在，存在删除
        boolean exists = template.indexOps(clazz).exists();
        if (exists) {
            template.indexOps(clazz).delete();
        }
        // 创建索引
        template.indexOps(clazz).create();
        // 创建映射
        template.indexOps(clazz).putMapping(template.indexOps(clazz)
            .createMapping(clazz));
        if (Objects.equals(TestUser.class.getName(), clazz.getName())) {
            // do something 模拟从数据库查询数据
            // 模拟一次1000
            List<TestUser> list = new ArrayList<>(1000);
            for (int i=0; i<10000; i++) {
                list.add(new TestUser("张"+i, "李"+i, i));
                if (list.size() % 1000==0) {
                    batchSaveToEs(clazz, list);
                    list.clear();
                }
            }
            if (list.size() >0) {
                batchSaveToEs(clazz, list);
            }
        } // else if...
    }

    public void batchSaveToEs(Class<?> clazz, List<?> listInfo) {
        // 根据clazz获取索引名称
        IndexCoordinates indexCoordinates = IndexCoordinates.of(
                AnnotationUtils.findAnnotation(clazz, Document.class).indexName());
        int count = 0;
        List<IndexQuery> indexQueries = new ArrayList<>(1000);
        for (Object info : listInfo) {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(info);
            indexQueries.add(indexQuery);
            if (count % 1000 ==0) {
                template.bulkIndex(indexQueries, indexCoordinates);
                indexQueries.clear();
            }
            count ++;
        }
        if (indexQueries.size() >0) {
            template.bulkIndex(indexQueries, indexCoordinates);
        }
    }

    @Test
    public void searchEsInfo() {
        System.out.println(searchInfoByCond("张"));

        // 在es7.8.1版本中，可以在以下代码打断点，看实际的es查询语句
        /*@Override
        public <T> SearchHits<T> search(Query query, Class<T> clazz, IndexCoordinates index) {
            SearchRequest searchRequest = requestFactory.searchRequest(query, clazz, index);
            SearchResponse response = execute(client -> client.search(searchRequest, RequestOptions.DEFAULT));

            AbstractElasticsearchTemplate.SearchDocumentResponseCallback<SearchHits<T>> callback = new AbstractElasticsearchTemplate.ReadSearchDocumentResponseCallback<>(clazz, index);
            return callback.doWith(SearchDocumentResponse.from(response));
        }*/
    }

    /*
    * es中的query
    * matchPhraseQuery match_phrase查询分析文本并根据分析的文本创建一个短语查询。match_phrase 会将检索关键词分词。match_phrase的分词结果必须在被检索字段的分词中都包含，而且顺序必须相同，而且默认必须都是连续的。
    * termQuery 字符的精确匹配查询
    * termsQuery
    * wildcardQuery 模糊查询
    * boolQuery 逻辑查询 must==>and     should==>or
    * rangeQuery 范围查询
    * matchQuery 分词查询
    * */
    public EsReturnInfo searchInfoByCond(String cond) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("cnName", cond).boost(5f))
                .should(QueryBuilders.matchQuery("cnName", cond).boost(5f))
                .should(QueryBuilders.matchPhraseQuery("cnName", cond).boost(3f))
                //.should(QueryBuilders.wildcardQuery("cnName", "*"+cond+"*"))// 模糊查询，根据具体需求，我们项目里不选择使用
                .should(QueryBuilders.termQuery("enName", cond).boost(5f))
                .should(QueryBuilders.matchQuery("enName", cond).boost(5f))
                .should(QueryBuilders.wildcardQuery("enName", "*"+cond+"*"))
                .should(QueryBuilders.matchPhraseQuery("enName", cond).boost(9f))
                )
                .withPageable(PageRequest.of(0, 20))//分页从0开始，如前端传1，需减去1
                .withHighlightFields(new HighlightBuilder.Field("cnName")// 高亮字段必须是查询字段
                        .preTags("<font color='red'>").postTags("</font>"))
                .withHighlightFields(new HighlightBuilder.Field("enName")
                        .preTags("<font color='red'>").postTags("</font>"))
                .build();

        SearchHits<TestUser> searchHits = template.search(query, TestUser.class);
        // es默认最大返回值10000条数据，要根据实际情况选择合适的分页方式
        if (searchHits.getTotalHits() == 0) {
            return new EsReturnInfo();
        }

        List<TestUser> returnList = new ArrayList<>(20);// 获取分页值，应该从前端传值获取
        // 替换高亮字段
        for (SearchHit<TestUser> hit : searchHits) {
            Map<String, List<String>> highlightFields= hit.getHighlightFields();
            hit.getContent().setCnName(highlightFields.get("cnName") == null?
                    hit.getContent().getCnName() : highlightFields.get("cnName").get(0));
            hit.getContent().setEnName(highlightFields.get("enName") == null?
                    hit.getContent().getEnName() : highlightFields.get("enName").get(0));
            returnList.add(hit.getContent());
        }

        EsReturnInfo<TestUser> returnInfo = new EsReturnInfo<>();
        returnInfo.setCurrent(0);// 当前页，应该从前端传值获取
        returnInfo.setSize(20);
        returnInfo.setTotal(searchHits.getTotalHits());
        returnInfo.setPage((int) Math.ceil(Float.valueOf(searchHits.getTotalHits() / 20)));
        returnInfo.setData(returnList);
        return returnInfo;
    }


}
