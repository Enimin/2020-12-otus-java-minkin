package ru.rt;

import ru.rt.interfaces.*;

import java.util.Map;

public class Dispenser extends Component implements IDispensing {
    private final IVaultCassettes vault;

    public Dispenser(IMediator atm, IVaultCassettes vault){
        super(atm);
        this.vault = vault;
    }

    @Override
    public void giveOutNotes(Transaction transaction){
        var event = Events.TRANSACTION_END;
        var stack = getCash(transaction);
        recountNotes(transaction.sum(), stack);
        openShutter();
        if (!isTakenAway()){
            returnNotes(stack);
            event = Events.RETURN_NOTES;
        }
        closeShutter();
        atm.notify(this, event);
    }

    private void openShutter(){}

    private void closeShutter(){}

    private StackOfCash[] getCash(Transaction transaction){
        StackOfCash[] stack = new StackOfCash[transaction.getMap().size()];
        var i = 0;
        for (Map.Entry<Integer,Integer> entry : transaction.getMap().entrySet()) {
            stack[i] = vault.getCash(entry.getKey(), entry.getValue());
        }
        return stack;
    }

    private void recountNotes(int sum, StackOfCash[] stack){}

    private boolean isTakenAway(){
        return true;
    }

    private void returnNotes(StackOfCash[] stack){}
}
