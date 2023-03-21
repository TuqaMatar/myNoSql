package com.example.myNoSql;

import com.example.myNoSql.model.Document;
import com.fasterxml.jackson.databind.JsonNode;

public class CreateDocumentRequest {
    private String databaseName;
    private Document document;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
