package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void updateDocument(Integer documentId, Document document) {
        boolean successfulUpdate = false;

        while (!successfulUpdate) {
            successfulUpdate = updateDocumentIfVersionsMatch(documentId ,document);

            if (!successfulUpdate) {
                // to reduce the contention between multiple threads
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public boolean updateDocumentIfVersionsMatch(Integer documentId, Document document) {
        synchronized (this) {
            if (Objects.equals(document.getVersion(), documents.get(documentId).getVersion())) {
                documents.remove(documentId);
                documents.put(documentId, document);
                documents.get(documentId).getAtomicVersion().incrementAndGet();
                return true;
            }
        }
        return false;
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