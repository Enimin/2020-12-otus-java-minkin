package ru.rt.listener.homework;

import ru.rt.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHistory implements Storageable {
    Map<Message, Message> history = new HashMap<>();

    @Override
    public void add(Message oldMsg, Message newMsg) {
        history.put(oldMsg.copy(), newMsg.copy());
    }

    public Message getLastOldMsg(){
        List<Message> keys =  new ArrayList<>(history.keySet());
        return keys.get(history.size()-1);
    }
}
