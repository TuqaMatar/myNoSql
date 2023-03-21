package com.example.myNoSql.model;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

public class Database {
    private String name;
    private JsonNode schema;
    List<Document> documents;

    public Database() {
    }

    public  Database (String name , JsonNode schema)
    {
        this.name=name;
        this.schema = schema;
        documents = new ArrayList<>();
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode  getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }


    public void deleteDocument(String id) {
        documents.remove(id);
    }

    public void addDocument(Document document) {
        documents.add(document);
    }
}