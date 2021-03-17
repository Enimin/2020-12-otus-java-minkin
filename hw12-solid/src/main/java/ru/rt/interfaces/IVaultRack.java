package ru.rt.interfaces;

import ru.rt.exceptions.CassetteIsAlreadySet;
import ru.rt.exceptions.CassetteNotFound;
import ru.rt.exceptions.RackIsEmpty;

public interface IVaultRack {
    void addCassette(IStoraging cassette) throws CassetteIsAlreadySet;
    void removeCassette(int denomination) throws  RackIsEmpty, CassetteNotFound;

}
