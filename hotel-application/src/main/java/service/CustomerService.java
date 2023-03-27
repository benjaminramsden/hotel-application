package main.java.service;

import main.java.model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static Map<String, Customer> customerMap;

    private CustomerService() {
        customerMap = new HashMap<>();
    }

    private static final class CustomerServiceHolder {
        private static final CustomerService instance = new CustomerService();
    }

    public static CustomerService getInstance() {
        return CustomerServiceHolder.instance;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        if (customerMap.containsKey(email)) {
            throw new IllegalStateException("Requesting duplicate customer details.");
        }
        Customer customer = new Customer(firstName, lastName, email);
        customerMap.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        if (!customerMap.containsKey(customerEmail)) {
            throw new IllegalArgumentException("Customer is not known, register the customer first.");
        }
        return customerMap.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }

    public void purgeAllCustomers() {
        customerMap.clear();
    }
}
