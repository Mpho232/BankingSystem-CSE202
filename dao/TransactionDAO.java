package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void recordTransaction(String accountNumber, String txType, double amount, double balanceAfter)
            throws SQLException {
        String sql = """
            INSERT INTO transactions (account_number, tx_type, amount, balance_after)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, txType);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, balanceAfter);
            stmt.executeUpdate();
        }
    }

    public List<String> getHistoryForAccount(String accountNumber) throws SQLException {
        List<String> rows = new ArrayList<>();
        String sql = """
            SELECT tx_timestamp, tx_type, amount, balance_after
            FROM transactions
            WHERE account_number = ?
            ORDER BY transaction_id DESC
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(String.format("[%s] %s: BWP %.2f | Bal: BWP %.2f",
                        rs.getString("tx_timestamp"),
                        rs.getString("tx_type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after")));
                }
            }
        }
        return rows;
    }
}