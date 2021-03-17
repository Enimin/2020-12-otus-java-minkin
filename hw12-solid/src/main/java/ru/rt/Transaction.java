package ru.rt;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Transaction {
    private final Map<Integer, Integer> decomposeSum = new TreeMap<>();
    private final int sum;

    public Transaction(int sum){
        this.sum = sum;
    }

    public int sum(){
        return this.sum;
    }

    public void put(Integer key, Integer value){
        decomposeSum.put(key,value);
    }

    public Map<Integer,Integer> getMap(){
        return new HashMap<>(decomposeSum);
    }

    public String toString()
    {
        Integer sum = 0;
        String summary = "Выдано:\n";

        for (Map.Entry<Integer, Integer> entry : decomposeSum.entrySet()) {
            sum += entry.getKey() * entry.getValue();
            summary += "   банкноты номиналом " + entry.getKey() + " руб.: " + entry.getValue() + " шт.;\n";
        }
        summary += "Итого: " + sum + " руб.";
        return summary;
    }

}
