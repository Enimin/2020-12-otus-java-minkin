package ru.rt.listener.homework;

import ru.rt.model.Message;

public class MessagePair {
    private final Message oldMessage;
    private final Message newMessage;

    public MessagePair(Message oldMessage, Message newMessage) {
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    public Message oldMessage() {
        return oldMessage.copy();
    }

    public Message newMessage() {
        return newMessage.copy();
    }
}