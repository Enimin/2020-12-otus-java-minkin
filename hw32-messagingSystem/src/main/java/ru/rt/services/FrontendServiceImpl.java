package ru.rt.services;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.MessageType;
import ru.rt.requestDataTypes.ClientRequest;
import ru.rt.requestDataTypes.ClientRequestDT;
import ru.rt.resultsDT.ClientsResult;

public class FrontendServiceImpl {

    MsClient msClient;
    String dest;

    public FrontendServiceImpl(MsClient msClient, String dest) {
        this.msClient = msClient;
        this.dest = dest;
    }

    public void sendMessage(ClientsResult data, MessageCallback<ClientsResult> callback) {
        var message = msClient.produceMessage(
                dest,
                data,
                MessageType.USER_DATA,
                callback
        );
        msClient.sendMessage(message);
    }
}
