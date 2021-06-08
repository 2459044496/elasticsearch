package com.example.elasticsearch.test.VO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsReturnInfo<T> implements Serializable {

    // 当前页
    private Integer current;

    // 总页数
    private Integer page;

    // 每页数量
    private Integer size;

    // 总数
    private Long total;

    List<T> data;


    public EsReturnInfo() {
    }

    public EsReturnInfo(Integer current, Integer page, Integer size, Long total) {
        this.current = current;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
