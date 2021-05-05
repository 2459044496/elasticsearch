package com.example.elasticsearch.test.doc;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.naming.directory.SearchResult;

// 查询全部doc
// 根据条件查询
// 分页查询
// 排序
// 过滤字段
// 组合查询
// 范围查询
// 模糊查询
// 高亮查询
// 聚合查询
// 分组查询
public class ESDocQueryAllAndCondQuery {
    public static void main(String[] args) throws Exception{
        // 创建ES客户端 使用高级别工具连接
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1", 9200))
        );

        // 1,查询全部doc
        /*SearchRequest request = new SearchRequest();
        request.indices("user");
        // matchAllQuery
        request.source(new SearchSourceBuilder()
        .query(QueryBuilders.matchAllQuery()));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/


        // 2,根据条件查询
        /*SearchRequest request = new SearchRequest();
        request.indices("user");
        // termQuery
        request.source(new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("sex","女")));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 3,分页查询
        /*SearchRequest request = new SearchRequest();
        request.indices("user");
        // from  开始位置   size 数量
        request.source(new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .from(0)
                .size(3));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 4,排序
        /*SearchRequest request = new SearchRequest();
        request.indices("user");
        // sort方法  SortOrder枚举
        request.source(new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .sort("age", SortOrder.DESC));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 5,过滤
        /*SearchRequest request = new SearchRequest();
        request.indices("user");
        // fetchSource方法  excludes排除字段 includes包含字段
        String[] excludes = {"age"};// 排除字段
        String[] includes = {"name"};// 包含字段
        request.source(new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery()).
                        fetchSource(includes, excludes));
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 6,组合查询  对比上面的查询builder变了 QueryBuilders变为BoolQueryBuilder
        /*SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // must相当于and
        //boolQueryBuilder.must(QueryBuilders.matchQuery("age", 30));
        //boolQueryBuilder.must(QueryBuilders.matchQuery("name", "zhangsan"));
        // should相当于or
        boolQueryBuilder.should(QueryBuilders.matchQuery("name", "lisi"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("name", "zhangsan"));

        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 7,范围查询   QueryBuilders变为RangeQueryBuilder
        /*SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("age");
        rangeQueryBuilder.gt(30);// gt > gte>=
        rangeQueryBuilder.lte(40);// lt < lte<=

        searchSourceBuilder.query(rangeQueryBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 8,模糊查询   QueryBuilders
        /*SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // fuzzyQuery       fuzzy模糊
        searchSourceBuilder.query(QueryBuilders.
                fuzzyQuery("name", "wanger")
                .fuzziness(Fuzziness.ONE));// Fuzziness.ONE差一个字符 TWO两个
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }
*/

        // 9,高亮查询   TermsQueryBuilder
        /*SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.
                termsQuery("name", "zhangsan");

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name");

        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(termsQueryBuilder);

        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
            System.out.println(searchHit.getHighlightFields());// 高亮信息
        }*/

        // 11,聚合查询   AggregationBuilder
        /*SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 获取最大的年龄字段
        AggregationBuilder aggregationBuilder = AggregationBuilders.
                max("maxAge").field("age");
        searchSourceBuilder.aggregation(aggregationBuilder);
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }*/

        // 12,分组查询   AggregationBuilder
        SearchRequest request = new SearchRequest();
        request.indices("user");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 根据age分组
        AggregationBuilder aggregationBuilder = AggregationBuilders.
                terms("ageGroup").field("age");
        searchSourceBuilder.aggregation(aggregationBuilder);
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        System.out.println(searchResponse.getTook());
        for (SearchHit searchHit : hits) {
            System.out.println(searchHit.getSourceAsString());
        }


        // 关闭客户端
        restHighLevelClient.close();
    }
}
