package ru.rt.listener.homework;

import ru.rt.model.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageHistory implements Storageable {
    private final Map<Integer, MessagePair> history = new HashMap<>();

    @Override
    public void add(Message oldMsg, Message newMsg) {
        history.put(history.size(), new MessagePair(oldMsg.copy(), newMsg.copy()));
    }

    @Override
    public Message getLastOldMsg(){
        int maxKey = history.keySet().size() - 1;
        return history.get(maxKey).oldMessage();
    }
}

class MessagePair {
    private final Message oldMessage;
    private final Message newMessage;

    public MessagePair(Message oldMessage, Message newMessage){
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    public Message oldMessage(){
        return oldMessage.copy();
    }

    public Message newMessage(){
        return newMessage.copy();
    }
}
