package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerController {
    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
    }

    public Customer registerCustomer(String customerId, String firstName, String surname, String address)
            throws SQLException {
        Customer customer = new Customer(customerId, firstName, surname, address);
        if (customerDAO.findById(customer.getCustomerId()) != null) {
            throw new IllegalArgumentException("A customer with ID '" + customer.getCustomerId() + "' already exists.");
        }
        customerDAO.insertCustomer(customer);
        return customer;
    }

    public Customer loadCustomerWithAccounts(String customerId) throws SQLException {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
        Customer customer = customerDAO.findById(customerId.trim());
        if (customer == null) {
            return null;
        }
        List<Account> accounts = accountDAO.findAccountsByCustomer(customer);
        for (Account account : accounts) {
            customer.addAccount(account);
        }
        return customer;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.findAll();
    }

    public void updateCustomer(Customer customer) throws SQLException {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        customerDAO.updateCustomer(customer);
    }
}