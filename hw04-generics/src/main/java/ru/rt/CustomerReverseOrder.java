package ru.rt;


import java.util.Stack;

public class CustomerReverseOrder {

    private Stack<Customer> customers = new Stack<>();

    public void add(Customer customer) {
        this.customers.push(customer);
    }

    public Customer take() {
        return this.customers.pop();
    }
}
