package com.example.myNoSql.model;
import com.example.myNoSql.service.DocumentIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.atomic.AtomicInteger;

public class Document {
    private Integer id;
    private AtomicInteger version;
    private JsonNode data;
    private Node affinityNode;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    public Document(JsonNode data, Integer id) {
        this.id = (id == null) ? DocumentIdGenerator.getInstance().getNextId() : id;
        this.data = data;
        this.version = new AtomicInteger(0);
    }
    public Document(){}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JsonNode getData() {
        return data;
    }

    public AtomicInteger getVersion() {
        return version;
    }

    public Node getAffinityNode() {
        return affinityNode;
    }

    public void setAffinityNode(Node affinityNode) {
        this.affinityNode = affinityNode;
    }

    public void setVersion(AtomicInteger version) {
        this.version = version;
    }
}
