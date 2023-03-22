package com.example.myNoSql.model;
import com.example.myNoSql.DocumentStore;
import com.fasterxml.jackson.databind.JsonNode;

import javax.print.Doc;
import java.util.*;

public class Database {
    private String name;
    private JsonNode schema;
    List<Document> documents;
    private HashMap<Integer, Document> idIndex;
    private HashMap<String, TreeMap<Object, List<Document>>> propertyIndexes;

    public Database() {
    }

    public  Database (String name , JsonNode schema)
    {
        this.name=name;
        this.schema = schema;
        documents = new ArrayList<>();
        idIndex = new HashMap<>();
        propertyIndexes = new HashMap<>();
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
        idIndex.put(document.getId(), document);
        documents.add(document);
    }
}