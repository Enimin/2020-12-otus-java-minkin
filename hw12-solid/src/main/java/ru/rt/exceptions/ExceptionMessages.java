package ru.rt.exceptions;

public enum ExceptionMessages {
    CASSETTE_IS_ALREADY_SET("Кассета уже установлена"),
    RACK_IS_EMPTY("В хранилище нет кассет"),
    BALANCE_IS_0("Нет средств"),
    NOT_ENOUGH_MONEY("Недостаточно средств"),
    MAST_BE_MULTIPLE_OF("Сумма должна быть кратной %d"),
    CASSETTE_NOT_FOUND("Нет такой кассеты"),
    DENOMINATION_IS_NOT_ACCEPTED("Банкнота такого номинала не принимается"),
    NOTE_IS_NOT_VALID("Не удалось распознать купюру");

    private final String message;

    ExceptionMessages(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
