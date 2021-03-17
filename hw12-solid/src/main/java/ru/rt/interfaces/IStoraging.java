package ru.rt.interfaces;

public interface IStoraging {
    int denomination();
    int count();
    void getNotes(int count);
    void takeNote();
}
