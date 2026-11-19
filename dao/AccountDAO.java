package dao;

import model.Account;
import model.ChequeAccount;
import model.Customer;
import model.InvestmentAccount;
import model.SavingsAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void insertAccount(Account account) throws SQLException {
        String sql = """
            INSERT INTO accounts (
                account_number, customer_id, account_type, branch, balance, company_name, company_address
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        String companyName = null;
        String companyAddress = null;
        if (account instanceof ChequeAccount cheque) {
            companyName = cheque.getCompanyName();
            companyAddress = cheque.getCompanyAddress();
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getOwner().getCustomerId());
            stmt.setString(3, account.getAccountType());
            stmt.setString(4, account.getBranch());
            stmt.setDouble(5, account.getBalance());
            stmt.setString(6, companyName);
            stmt.setString(7, companyAddress);
            stmt.executeUpdate();
        }
    }

    public void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        }
    }

    public List<Account> findAccountsByCustomer(Customer owner) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = """
            SELECT account_number, account_type, branch, balance, company_name, company_address
            FROM accounts
            WHERE customer_id = ?
            ORDER BY account_number
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, owner.getCustomerId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String accNum = rs.getString("account_number");
                    String type = rs.getString("account_type");
                    String branch = rs.getString("branch");
                    double balance = rs.getDouble("balance");

                    Account acc = switch (type) {
                        case "Savings Account" -> new SavingsAccount(accNum, owner, branch, balance);
                        case "Investment Account" -> new InvestmentAccount(accNum, owner, branch, balance);
                        case "Cheque Account" -> new ChequeAccount(
                            accNum, owner, branch, balance,
                            rs.getString("company_name"),
                            rs.getString("company_address")
                        );
                        default -> throw new SQLException("Unknown account type in DB: " + type);
                    };
                    accounts.add(acc);
                }
            }
        }
        return accounts;
    }

    public void deleteAccount(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.executeUpdate();
        }
    }
}