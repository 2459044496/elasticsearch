package com.example.elasticsearch.test.doc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

// 批量新增doc
public class ESDocBatchInsert {
    public static void main(String[] args) throws Exception{
        // 创建ES客户端 使用高级别工具连接
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1", 9200))
        );

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("user").id("1001")
                .source(XContentType.JSON, "name","zhangsan", "sex","男","age","30"));
        bulkRequest.add(new IndexRequest().index("user").id("1002")
                .source(XContentType.JSON, "name","lisi","sex","男","age","30"));
        bulkRequest.add(new IndexRequest().index("user").id("1003")
                .source(XContentType.JSON, "name","wanger","sex","男","age","40"));
        bulkRequest.add(new IndexRequest().index("user").id("1004")
                .source(XContentType.JSON, "name","wanger1","sex","女","age","50"));
        bulkRequest.add(new IndexRequest().index("user").id("1005")
                .source(XContentType.JSON, "name","wanger2","sex","女","age","40"));
        // 批量新增数据
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.getTook());
        System.out.println(bulk.getItems());
        // 关闭客户端
        restHighLevelClient.close();
    }
}
