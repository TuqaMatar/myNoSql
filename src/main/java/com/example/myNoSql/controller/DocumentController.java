package com.example.myNoSql.controller;
import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.service.BroadcastingService;
import com.example.myNoSql.service.DocumentIdGenerator;
import com.example.myNoSql.service.DocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private BootStrapProperties bootstrapProperties;

    @Autowired
    private BroadcastingService broadcastingService;

    @Value("${CONTAINER_NAME:unknown}")
    private String containerName;

    @GetMapping("/search/{databaseName}/{keyword}")
    public List<Document> searchDocuments(@PathVariable("databaseName") String databaseName, @PathVariable("keyword") String keyword) {
        return documentService.searchDocumentsByKeyword(keyword, databaseName);
    }

    @PostMapping("/{databaseName}/createDocument")
    public CompletableFuture<Void> createDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        int documentId = DocumentIdGenerator.getInstance().getNextId();
        Document document = new Document(jsonContent, documentId);
        // Assign the affinity node
        Node affinityNode = getAffinityNode(document.getId());
        document.setAffinityNode(affinityNode);
        if (broadcast) {
            broadcastingService.broadcastCreateDocumentToAllContainers(databaseName, document.getId(), jsonContent);
        }

        return documentService.addDocumentToDatabase(databaseName, document);
    }
    @PostMapping("/{databaseName}/updateDocument/{documentId}")
    public CompletableFuture<Void> updateDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                                                  @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast,
                                                  @RequestHeader(value = "redirect", defaultValue = "true") boolean redirected) {

        // Check if the current node has affinity with the document
        Document oldDocument = documentService.getDocumentFromDatabase(databaseName, documentId);
        Document newDocument = new Document(jsonContent, oldDocument.getId());
        newDocument.setVersion(oldDocument.getVersion());

        newDocument.setAffinityNode(oldDocument.getAffinityNode());

        if (!isCurrentNodePort(oldDocument.getAffinityNode())) {
            // Redirect the request to the affinity node
            documentService.redirectToAffinityNode(databaseName, documentId, jsonContent, oldDocument.getAffinityNode());
            return CompletableFuture.completedFuture(null); // Return immediately after redirecting
        }

        // First, update the document in the affinity node
        CompletableFuture<Void> result = documentService.updateDocumentFromDatabase(databaseName, documentId, newDocument);

        // Then, broadcast the update to other nodes
        if (broadcast) {
            broadcastingService.broadcastUpdateDocumentToAllContainers(databaseName, documentId, jsonContent);
        }

        return result;
    }


    @PostMapping("/{databaseName}/updateDocumentFromRedirect/{documentId}")
    public CompletableFuture<Void> updateDocumentFromRedirect(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                                                              @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast, @RequestHeader(value = "redirect", defaultValue = "true") boolean redirected) {

        // Check if the current node has affinity with the document
        Document oldDocument = documentService.getDocumentFromDatabase(databaseName, documentId);
        Document newDocument = new Document(jsonContent , oldDocument.getId());
        newDocument.setVersion(oldDocument.getVersion());
        newDocument.setAffinityNode(oldDocument.getAffinityNode());
        System.out.println("IN  AFFINITY");

        if (broadcast) {
            broadcastingService.broadcastUpdateDocumentToAllContainers(databaseName, documentId, jsonContent);
        }
        return documentService.updateDocumentFromDatabase(databaseName, documentId, newDocument);
    }

    private boolean isCurrentNodePort(Node affinityNode) {
        System.out.println("affinity node : " + affinityNode.getName() + " container name: " + containerName);
        return affinityNode.getName().equals(containerName);
    }

    @PostMapping("/{databaseName}/deleteDocument/{documentId}")
    public CompletableFuture<Void> deleteDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {

        if (broadcast) {
            broadcastingService.broadcastDeleteDocumentToAllContainers(databaseName, documentId);
        }
        return documentService.deleteDocumentFromDatabase(databaseName, documentId);
    }

    private Node getAffinityNode(Integer documentId) {
        List<Node> nodes = bootstrapProperties.getNodes();
        int numberOfNodes = nodes.size();

        // Determine the affinity node based on the document ID
        int affinityIndex = Math.abs(documentId % numberOfNodes);

        // Return the affinity node's address
        return nodes.get(affinityIndex);
    }
}
