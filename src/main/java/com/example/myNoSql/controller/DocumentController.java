package com.example.myNoSql.controller;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;


    @GetMapping("/search/{databaseName}/{keyword}")
    public List<Document> searchDocuments(@PathVariable("databaseName") String databaseName, @PathVariable("keyword") String keyword) {
        return documentService.searchDocumentsByKeyword(keyword, databaseName);
    }

    // Other endpoints for adding, updating, and deleting documents
}
