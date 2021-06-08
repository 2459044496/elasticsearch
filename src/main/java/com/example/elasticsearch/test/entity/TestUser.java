package com.example.elasticsearch.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "testuser", shards = 3, replicas = 1)
//indexName索引名称 shards分片数 replicas备份数
public class TestUser {

    // 必须有
    @Id
    private Long id;

    // ik_max_work中文分词，根据实际情况使用
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String cnName;

    // simple英文分词，根据实际情况使用
    @Field(type = FieldType.Text, analyzer = "simple")
    private String enName;

    // Keyword不分词，index = false不建立索引
    @Field(type = FieldType.Keyword, index = false)
    private Integer money;

    public TestUser(String cnName, String enName, Integer money) {
        this.cnName = cnName;
        this.enName = enName;
        this.money = money;
    }

    public TestUser(Integer money) {
        this.money = money;
    }
}
