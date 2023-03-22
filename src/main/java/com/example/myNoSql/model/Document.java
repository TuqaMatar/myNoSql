package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.atomic.AtomicInteger;

public class Document {
    private Integer id;
    private JsonNode data;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    public Document(JsonNode data) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }


}
