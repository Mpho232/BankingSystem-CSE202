package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private final String customerId;
    private String firstName;
    private String surname;
    private String address;
    private final List<Account> accounts;

    public Customer(String customerId, String firstName, String surname, String address) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname is required.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required.");
        }
        this.customerId = customerId.trim();
        this.firstName = firstName.trim();
        this.surname = surname.trim();
        this.address = address.trim();
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }
        this.firstName = firstName.trim();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname is required.");
        }
        this.surname = surname.trim();
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required.");
        }
        this.address = address.trim();
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        if (account.getOwner() != this) {
            throw new IllegalArgumentException("Account is not registered to this customer.");
        }
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) | Accounts: %d",
                customerId, getFullName(), address, accounts.size());
    }
}
  