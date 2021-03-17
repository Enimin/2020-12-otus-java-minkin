package ru.rt.listener.homework;

import ru.rt.listener.Listener;
import ru.rt.model.Message;

public class ListenerHistory implements Listener {
    Storageable storage;

    public ListenerHistory(Storageable storage){
        this.storage = storage;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        storage.add(oldMsg, newMsg);
    }

}
