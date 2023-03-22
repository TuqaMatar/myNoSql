package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Database {
    private String name;
    private JsonNode schema;
    private TreeMap<Integer, Document> documents;

    public Database() {
    }

    public Database(String name, JsonNode schema) {
        this.schema = schema;
        this.name = name;
        documents = new TreeMap<>();
    }

    public List<Document> getDocuments() {
        return documents.values().stream().collect(toList());
    }
    public void addDocument(Document document) {
        System.out.println(document.getData().hashCode());
        documents.put(document.getId(), document);
    }

    public Document getDocument(int id) {
        return documents.get(id);
    }

    public void updateDocument(Integer documentId,Document document) {
        documents.remove(documentId);
        documents.put(documentId, document);
    }

    public void deleteDocument(Document document) {
        documents.remove(document.getData().hashCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }

}