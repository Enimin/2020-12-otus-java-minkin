package ru.rt;

import ru.rt.exceptions.*;
import ru.rt.interfaces.*;

import java.util.*;

public class ATM implements IMediator {
    private final IVault vault;
    private final IDispensing dispenser;
    private final IAccepting acceptor;
    private Integer balance;
    private Transaction transaction;

    public ATM(){
        vault = new Vault(this);
        dispenser = new Dispenser(this, vault);
        acceptor = new Acceptor(this, vault);
        balance = 0;
    }

    public void notify(Component sender, Events event){
        if (sender == vault && event == Events.RECULC_BALANCE){
            balance = reculcBalance();
        }
        if (sender == acceptor && event == Events.NOT_VALID_NOTE){
            System.out.println("Не удалось распознать купюру");
        }
        if (sender == dispenser && event == Events.RETURN_NOTES){
            balance = reculcBalance();
            System.out.println("Наличные возвращены в хранилище");
        }
        if (sender == dispenser && event == Events.TRANSACTION_END){
            balance = reculcBalance();
            System.out.println(transaction.toString());
        }
    }

    public IVaultRack getVaultRack(){
        return this.vault;
    }

    public int balance(){
        return balance;
    }

    public void takeMoney(Note note){
        try {
            checkVault(note.denomination());
            acceptor.takeNote(note);
        } catch (RackIsEmpty | WrongDenomination e) {
            System.out.println(e.getMessage());
        }
    }

    public void giveMoney(int sum){
        try {
            checkBalance(sum);
            Transaction transaction = decomposeSum(sum);
            dispenser.giveOutNotes(transaction);
        } catch (BalanceIsZero | NotEnoughMoney | WrongMultiple e) {
            System.out.println(e.getMessage());
        }
    }

    private int reculcBalance(){
        List<Integer> denominations = vault.getRangeDenominationNotes();
        var sum = 0;
        for (int denomination : denominations) {
            sum += denomination * vault.countCassetteNote(denomination);
        }
        return sum;
    }

    private void checkBalance(int sum) throws BalanceIsZero, NotEnoughMoney, WrongMultiple{
        if ( balance == 0){
            throw new BalanceIsZero(ExceptionMessages.BALANCE_IS_0.toString());
        }
        if (balance < sum){
            throw new NotEnoughMoney(ExceptionMessages.NOT_ENOUGH_MONEY.toString());
        }
        List<Integer> denominations = new ArrayList<>(vault.getRangeDenominationNotes());
        if (sum % Collections.min(denominations) != 0){
            throw new WrongMultiple(String.format(ExceptionMessages.MAST_BE_MULTIPLE_OF.toString(), Collections.min(denominations))); //Exception
        }
    }

    private void checkVault(int denomination) throws RackIsEmpty, WrongDenomination{
        if (vault.rackIsEmpty()){
            throw new RackIsEmpty(ExceptionMessages.RACK_IS_EMPTY.toString());
        }
        if (!vault.rackContainsCassette(denomination)){
            throw new WrongDenomination(ExceptionMessages.DENOMINATION_IS_NOT_ACCEPTED.toString());
        }
    }

    private Transaction decomposeSum(int sum){
        transaction = new Transaction(sum);
        List<Integer> denominations = vault.getRangeDenominationNotes();
        var remainder = sum;
        for (var i = denominations.size() - 1; i >= 0; --i){
           var div = remainder / denominations.get(i);
           if (div != 0){
               transaction.put(denominations.get(i), div);
           }
           remainder %= denominations.get(i);
        }
        return transaction;
    }

}
