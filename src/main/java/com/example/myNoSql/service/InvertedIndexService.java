package com.example.myNoSql.service;

import com.example.myNoSql.model.Document;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class InvertedIndexService {
    private Map<String, Set<Integer>> index = new HashMap<>();

    public void addDocument(Document document) {
        int docId = document.getId();
        JsonNode content = document.getData();

        Iterator<String> fieldNames = content.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            String fieldValue = content.get(field).asText();
            String[] tokens = tokenize(fieldValue);

            for (String token : tokens) {
                index.computeIfAbsent(token, k -> new HashSet<>()).add(docId);
            }
        }
    }

    public Set<Integer> search(String query) {
        String[] tokens = tokenize(query);
        if (tokens.length == 0) {

            return Collections.emptySet();
        }

        Set<Integer> resultSet = new HashSet<>(index.getOrDefault(tokens[0], Collections.emptySet()));

        for (int i = 1; i < tokens.length; i++) {
            Set<Integer> tokenResults = index.getOrDefault(tokens[i], Collections.emptySet());
            resultSet.retainAll(tokenResults);
        }

        return resultSet;
    }

    private String[] tokenize(String text) {
        // Simple tokenization: split by non-word characters and convert to lowercase
        return text.toLowerCase().split("\\W+");
    }

    public Map<String, Set<Integer>> getIndex() {
        return index;
    }

    public void setIndex(Map<String, Set<Integer>> index) {
        this.index = index;
    }
}