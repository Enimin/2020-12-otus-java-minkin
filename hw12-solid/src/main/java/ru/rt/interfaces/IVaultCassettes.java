package ru.rt.interfaces;

import ru.rt.Note;
import ru.rt.StackOfCash;

public interface IVaultCassettes {
    void takeNote(Note note);
    StackOfCash getCash(int denomination, int count);
}
