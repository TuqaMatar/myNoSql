package com.example.myNoSql.service;

public class DocumentIdGenerator {
    private static DocumentIdGenerator instance;
    private int counter;

    private DocumentIdGenerator() {
        this.counter = 0;
    }

    public static DocumentIdGenerator getInstance() {
        if (instance == null) {
            instance = new DocumentIdGenerator();
        }
        return instance;
    }

    public synchronized int getNextId() {
        return counter++;
    }
}

