package ru.rt;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>(new Comparator<>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return Long.compare( o1.getScores(), o2.getScores());
        }
    });

    public Map.Entry<Customer, String> getSmallest() {
        Customer smallestCustomer = customers.firstEntry().getKey();

        Customer smallestKey = new Customer(smallestCustomer.getId(), smallestCustomer.getName(), smallestCustomer.getScores());
        String smallestValue = customers.firstEntry().getValue();
        Map.Entry<Customer, String> smallest =new AbstractMap.SimpleEntry<>(smallestKey, smallestValue);

        return smallest;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customers.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
