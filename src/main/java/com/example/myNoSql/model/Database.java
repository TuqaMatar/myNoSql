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
    public TreeMap<Integer,Document> getDocumentMap (){
        return documents;
    }

    public void addDocument(Document document) {
        if(!documents.containsKey(document.getId())){
            documents.put(document.getId(), document);
        }

    }


    public void updateDocument(Integer documentId, Document document) {
        boolean successfulUpdate = false;

        while (!successfulUpdate) {
            successfulUpdate = updateDocumentIfVersionsMatch(documentId ,document);
            System.out.println("Successful update" + successfulUpdate);
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

            System.out.println(document.getVersion().get()  + " " + documents.get(documentId).getVersion().get());
            if (Objects.equals(document.getVersion().get(), documents.get(documentId).getVersion().get())) {
                documents.remove(documentId);
                documents.put(documentId, document);
                documents.get(documentId).getVersion().incrementAndGet();

                return true;
            }
        }
        return false;
    }


    public void deleteDocument(Integer id ) {
        documents.remove(id);
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

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Database)
        {
            Database c = (Database) o;
            if ( this.name.equals(c.name) )
                return true;
        }
        return false;
    }
}