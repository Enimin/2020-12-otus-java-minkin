package ru.rt.listener.homework;

import ru.rt.model.Message;

public interface Storageable {

    void add(Message oldMsg, Message newMsg);
}
