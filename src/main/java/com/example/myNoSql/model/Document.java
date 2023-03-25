package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.atomic.AtomicInteger;

public class Document {
    private Integer id;
    private AtomicInteger version;
    private JsonNode data;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    public Document(JsonNode data) {
        this.id = data.hashCode();
        this.data = data;
        this.version = new AtomicInteger(0);

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


    public Integer getVersion() {
        return version.get();
    }
    public AtomicInteger getAtomicVersion() {
        return version;
    }

    public void setVersion(AtomicInteger version) {
        this.version = version;
    }
}
