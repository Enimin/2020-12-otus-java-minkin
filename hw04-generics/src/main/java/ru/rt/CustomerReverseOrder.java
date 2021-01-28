package ru.rt;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        this.customers.addFirst(customer);
    }

    public Customer take() {
        return this.customers.pollFirst();
    }
}
