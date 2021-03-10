package ru.rt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rt.exceptions.CassetteIsAlreadySet;
import ru.rt.exceptions.CassetteNotFound;
import ru.rt.exceptions.RackIsEmpty;
import ru.rt.interfaces.IVaultRack;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    void setCassettesThrows(IVaultRack vault, int count, int... denominations) throws CassetteIsAlreadySet {
        for (int denomination: denominations) {
            vault.addCassette(new Cassette(denomination,count));
        }
    }

    void setCassettes(IVaultRack vault, int count, int... denominations){
        try {
            setCassettesThrows(vault, count, denominations);
        } catch (CassetteIsAlreadySet cassetteIsAlreadySet) {
            cassetteIsAlreadySet.printStackTrace();
        }
    }

    void setEmptyCassettesThrows(IVaultRack vault, int... denominations) throws CassetteIsAlreadySet {
        for (int denomination: denominations) {
            vault.addCassette(new Cassette(denomination,0));
        }
    }

    void setEmptyCassettes(IVaultRack vault, int... denominations){
        try {
            setEmptyCassettesThrows(vault, denominations);
        } catch (CassetteIsAlreadySet cassetteIsAlreadySet) {
            cassetteIsAlreadySet.printStackTrace();
        }
    }

    @BeforeEach
    void setUp(){
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown(){
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Ошибка CassetteIsAlreadySet при попытке вставить в АТМ кассету с одинаковым номиналом купюр.")
    void atmAddCassettes() {

        Exception exception = assertThrows(CassetteIsAlreadySet.class, () -> {
                    var atm = new ATM();
                    var vault = atm.getVaultRack();

                    setCassettesThrows(vault, 1000, 10, 10);
                });

        String displayMessage = "Кассета уже установлена";
//        String displayMessage = "";

        assertEquals(displayMessage, exception.getMessage());
//        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Ошибка CassetteNotFound при попытке удалить из АТМ несуществующую кассету.")
    void atmRemoveCassette() {

        Exception exception = assertThrows(CassetteNotFound.class, () -> {
                    var atm = new ATM();
                    var vault = atm.getVaultRack();

                    setCassettesThrows(vault, 1000, 10, 50);

                    vault.removeCassette(10);
                    vault.removeCassette(10);
                });

        String displayMessage = "Нет такой кассеты";
//        String displayMessage = "";

        assertEquals(displayMessage, exception.getMessage());
//        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Ошибка RackIsEmpty при попытке удалить кассету из АТМ, когда кассет нет.")
    void atmRemoveCassetteWhenRackIsEmpty() {

        Exception exception = assertThrows(RackIsEmpty.class, () -> {
                    var atm = new ATM();
                    var vault = atm.getVaultRack();

                    setCassettesThrows(vault, 1000,10, 50);

                    vault.removeCassette(10);
                    vault.removeCassette(50);
                    vault.removeCassette(100);
                });

        String displayMessage = "В хранилище нет кассет";
//        String displayMessage = "";

        assertEquals(displayMessage, exception.getMessage());
//        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Получить сумму средств в АТМ.")
    void atmBalance() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 1000, 10, 50, 1000);

        var balance = 1_060_000;

        assertEquals(balance, atm.balance());
    }


    @Test
    @DisplayName("Получить сумму средств в АТМ после смены кассет.")
    void atmBalanceWhenRemoveCassette() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 1000, 10, 50, 1000);

        var balanceBefore = 1_060_000;

        assertEquals(balanceBefore, atm.balance());

        try {
            vault.removeCassette(1000);
        } catch (RackIsEmpty | CassetteNotFound e) {
            e.printStackTrace();
        }

        var balanceAfter = 60_000;

        assertEquals(balanceAfter, atm.balance());
    }


    @Test
    @DisplayName("Получить сумму средств в АТМ с пустыми кассетами.")
    void atmBalanceWhenCassetteIsEmpty() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setEmptyCassettes(vault, 10, 50, 1000);

        var balance = 0;

        assertEquals(balance, atm.balance());
    }

    @Test
    @DisplayName("Получить сумму средств в АТМ без кассет.")
    void atmBalanceWhenRackIsEmpty() {

        var atm = new ATM();

        var balance = 0;

        assertEquals(balance, atm.balance());
    }

    @Test
    @DisplayName("Получить сумму средств в АТМ после принятия купюры.")
    void atmTakeNote() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setEmptyCassettes(vault, 10);

        atm.takeMoney(new Note(10));

        var balance = 10;

        assertEquals(balance, atm.balance());
    }


    @Test
    @DisplayName("Сообщение \"Банкнота такого номинала не принимается\" при попытке приема купюры номинал, которой не представлен в АТМ.")
    void atmTakeNoteWrongDenomination() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setEmptyCassettes(vault, 10);

        atm.takeMoney(new Note(100));

        var balance = 0;
        String displayMessage = "Банкнота такого номинала не принимается";

        assertEquals(balance, atm.balance());
        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Сообщение \"Нет средств\" при попытке снять сумму в АТМ с пустой кассетой.")
    void atmGiveOutWhenIsEmpty() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setEmptyCassettes(vault, 10);
        atm.giveMoney(50);

        var balance = 0;
        String displayMessage = "Нет средств";

        assertEquals(balance, atm.balance());
        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Сообщение \"Недостаточно средств\" снять сумму когда в АТМ недостаточно средств.")
    void atmGiveOutWhenNotEnough() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 1,10);
        atm.giveMoney(50);

        String displayMessage = "Недостаточно средств";

        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Сообщение \"Сумма должна быть кратной 10\" при попытке снять сумму не кратной минимальному номиналу купюры в АТМ.")
    void atmGiveOutWhenNotMultiplicity() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 10, 10);
        atm.giveMoney(55);

        String displayMessage = "Сумма должна быть кратной 10";

        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Снять сумму при максимальном наборе номиналов купюр(руб) в АТМ.")
    void atmGiveOutMoneyFull() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 100, 10,50,100,200,500,1000,2000,5000);

        atm.giveMoney(155_480);

        var balance = 730_520;
        String displayMessage = "Выдано:\n" +
                                "   банкноты номиналом 10 руб.: 3 шт.;\n" +
                                "   банкноты номиналом 50 руб.: 1 шт.;\n" +
                                "   банкноты номиналом 200 руб.: 2 шт.;\n" +
                                "   банкноты номиналом 5000 руб.: 31 шт.;\n" +
                                "Итого: 155480 руб.";

        assertEquals(balance, atm.balance());
        assertEquals(displayMessage, out.toString().trim());
    }

    @Test
    @DisplayName("Снять сумму при ограниченном наборе номиналов купюр(руб) в АТМ.")
    void atmGiveOutMoneyNotFull() {

        var atm = new ATM();
        var vault = atm.getVaultRack();

        setCassettes(vault, 100, 10,100,500,1000);

        atm.giveMoney(155_480);

        var balance = 5_520;
        String displayMessage = "Выдано:\n" +
                                "   банкноты номиналом 10 руб.: 8 шт.;\n" +
                                "   банкноты номиналом 100 руб.: 4 шт.;\n" +
                                "   банкноты номиналом 1000 руб.: 155 шт.;\n" +
                                "Итого: 155480 руб.";

        assertEquals(balance, atm.balance());
        assertEquals(displayMessage, out.toString().trim());
    }
}
