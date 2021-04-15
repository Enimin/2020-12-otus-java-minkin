package ru.rt.listener.homework;

import ru.rt.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory implements Storageable {
    private final List<MessagePair> history = new ArrayList<>();

    @Override
    public void add(Message oldMsg, Message newMsg) {
        history.add(new MessagePair(oldMsg.copy(), newMsg.copy()));
    }

    @Override
    public Message getLastOldMsg() {
        int maxKey = history.size() - 1;
        return history.get(maxKey).oldMessage();
    }
}