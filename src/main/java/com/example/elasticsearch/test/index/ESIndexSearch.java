package com.example.elasticsearch.test.index;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.util.Map;

@Slf4j
// 查询索引
public class ESIndexSearch {
    public static void main(String[] args) throws Exception{
        // 创建ES客户端 使用高级别工具连接
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1", 9200))
        );
        GetIndexRequest request = new GetIndexRequest("user");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        Map aliases = getIndexResponse.getAliases();
        log.info("aliases:"+aliases.toString());
        log.info("mappings:"+getIndexResponse.getMappings());
        log.info("settings:"+getIndexResponse.getSettings());
        // 关闭客户端
        restHighLevelClient.close();
    }
}
