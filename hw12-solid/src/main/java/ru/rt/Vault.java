package ru.rt;

import ru.rt.exceptions.*;
import ru.rt.interfaces.*;

import java.util.*;

class Vault extends Component implements IVault, IVaultRack, IVaultCassettes {
    private final HashMap<Integer, IStoraging> cassetteRack;

    public Vault(IMediator atm){
        super(atm);
        cassetteRack = new HashMap<>();
    }

    @Override
    public void addCassette(IStoraging cassette) throws CassetteIsAlreadySet {
        checkContainsCassette(cassette.denomination());
        cassetteRack.put(cassette.denomination(), cassette);
        atm.notify(this, Events.RECULC_BALANCE);
    }

    @Override
    public void removeCassette(int denomination) throws  RackIsEmpty, CassetteNotFound {
        checkRack();
        checkNotContainsCassette(denomination);
        cassetteRack.remove(denomination);
        atm.notify(this, Events.RECULC_BALANCE);
    }

    @Override
    public void takeNote(Note note){
        cassetteRack.get(note.denomination()).takeNote();
        atm.notify(this, Events.RECULC_BALANCE);
    }

    @Override
    public StackOfCash getCash(int denomination, int count){
        cassetteRack.get(denomination).getNotes(count);
        return new StackOfCash(denomination, count);
    }

    @Override
    public boolean rackIsEmpty(){
        return cassetteRack.isEmpty();
    }

    @Override
    public boolean rackContainsCassette(int denomination){
        return cassetteRack.containsKey(denomination);
    }

    @Override
    public List<Integer> getRangeDenominationNotes(){
        var keys = new ArrayList<>(cassetteRack.keySet());
        Collections.sort(keys);
        return keys;
    }

    @Override
    public int countCassetteNote(int denomination){
        return cassetteRack.get(denomination).count();
    }

    private void checkRack() throws RackIsEmpty{
        if (rackIsEmpty()){
            throw new RackIsEmpty(ExceptionMessages.RACK_IS_EMPTY.toString());
        }
    }

    private void checkContainsCassette(int denomination) throws CassetteIsAlreadySet {
        if (rackContainsCassette(denomination)){
            throw new CassetteIsAlreadySet(ExceptionMessages.CASSETTE_IS_ALREADY_SET.toString());
        }
    }

    private void checkNotContainsCassette(int denomination) throws CassetteNotFound {
        if (!rackContainsCassette(denomination)){
            throw new CassetteNotFound(ExceptionMessages.CASSETTE_NOT_FOUND.toString());
        }
    }

}
