package com.example.myNoSql;

import com.example.myNoSql.model.Document;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class DocumentStore {
    private HashMap<Integer, Document> idIndex;
    private HashMap<String, TreeMap<Object, List<Document>>> propertyIndexes;

    public DocumentStore() {
        idIndex = new HashMap<>();
        propertyIndexes = new HashMap<>();
    }
}