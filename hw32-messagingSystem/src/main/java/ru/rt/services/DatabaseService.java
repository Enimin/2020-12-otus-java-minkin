package ru.rt.services;

import ru.otus.messagesystem.client.MsClient;

public class DatabaseService {

    MsClient msClient;
    String dest;

    public DatabaseService(MsClient msClient, String dest) {
        this.msClient = msClient;
        this.dest = dest;
    }
}
