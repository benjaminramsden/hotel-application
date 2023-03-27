package main.java.model;

import java.util.regex.Pattern;

public record Customer(String firstName, String lastName, String email) {
    private static final String emailRegex = "^(.+)@(.+).(.+)$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

    public Customer {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email does not match expected pattern, e.g. a@b.com");
        }
    }

    @Override
    public String toString() {
        return "Customer details: " + "Name: " + firstName + " " + lastName + " Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
