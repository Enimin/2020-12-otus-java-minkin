package ru.rt.services;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.MessageType;
import ru.rt.messages.DataMessage;

public class FrontendService {

    MsClient msClient;
    String dest;

    public FrontendService(MsClient msClient, String dest) {
        this.msClient = msClient;
        this.dest = dest;
    }

    public void sendMessage(DataMessage data, MessageCallback<DataMessage> callback) {
        var message = msClient.produceMessage(
                                                    dest,
                                                    data,
                                                    MessageType.USER_DATA,
                                                    callback);
        msClient.sendMessage(message);
    }
}
