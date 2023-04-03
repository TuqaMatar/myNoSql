package com.example.myNoSql.service;

import com.example.myNoSql.InvertedIndex;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired FileStorageService fileStorageService;
    @Autowired
    InvertedIndex invertedIndex;

    @Autowired
    DatabaseService databaseService ;

    @PostConstruct
    public void initIndex() {
        Map<String, Set<Integer>> loadedIndex = fileStorageService.loadIndexFromFile();

        // Add the loaded index to the new index
        for (Map.Entry<String, Set<Integer>> entry : loadedIndex.entrySet()) {
            for (Integer docId : entry.getValue()) {
                invertedIndex.getIndex().computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(docId);
            }
        }
    }


    public List<Document> searchDocumentsByKeyword(String keyword , String databaseName) {

        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }

        List<Document> databaseDocuments = db.getDocuments();

        Set<Integer> documentIds = invertedIndex.search(keyword);

        return databaseDocuments.stream()
                .filter(document -> documentIds.contains(document.getId()))
                .collect(Collectors.toList());
    }
}
