package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Document {
    private String id;
    private JsonNode data;

    public Document(String id, JsonNode data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
