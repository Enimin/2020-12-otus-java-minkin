package ru.rt;


import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    private Map.Entry<Customer, String> getCloneEntry(Map.Entry<Customer, String> sourceEntry){
        if (sourceEntry == null ) return null;

        Customer cloneFirstEntryKey = new Customer(sourceEntry.getKey().getId(), sourceEntry.getKey().getName(), sourceEntry.getKey().getScores());

        return new AbstractMap.SimpleEntry<>(cloneFirstEntryKey, sourceEntry.getValue());
    }

    public Map.Entry<Customer, String> getSmallest() {
        return getCloneEntry(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getCloneEntry(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
