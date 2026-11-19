package dao;

import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void insertCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getSurname());
            stmt.setString(4, customer.getAddress());
            stmt.executeUpdate();
        }
    }

    public Customer findById(String customerId) throws SQLException {
        String sql = "SELECT customer_id, first_name, surname, address FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address")
                    );
                }
            }
        }
        return null;
    }

    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, first_name, surname, address FROM customers ORDER BY customer_id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("address")
                ));
            }
        }
        return customers;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ? WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getSurname());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getCustomerId());
            stmt.executeUpdate();
        }
    }

    public void deleteCustomer(String customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            stmt.executeUpdate();
        }
    }
}