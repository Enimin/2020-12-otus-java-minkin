package ru.rt.interfaces;

import java.util.List;

public interface IVault extends IVaultRack, IVaultCassettes{
    boolean rackIsEmpty();
    boolean rackContainsCassette(int denomination);
    List<Integer> getRangeDenominationNotes();
    int countCassetteNote(int denomination);
}
