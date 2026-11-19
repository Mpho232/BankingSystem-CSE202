package controller;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.ChequeAccount;
import model.Customer;
import model.InterestBearing;
import model.InvestmentAccount;
import model.SavingsAccount;

import java.sql.SQLException;
import java.util.List;

public class AccountController {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public Account openAccount(Customer customer, String accountType, String accountNumber,
                              String branch, double initialAmount,
                              String companyName, String companyAddress) throws SQLException {
        if (customer == null) {
            throw new IllegalArgumentException("An account must be held by a customer.");
        }
        if (accountType == null || accountType.isBlank()) {
            throw new IllegalArgumentException("Account type is required.");
        }

        Account newAccount = switch (accountType.trim()) {
            case "Savings Account" -> new SavingsAccount(accountNumber, customer, branch, initialAmount);
            case "Investment Account" -> new InvestmentAccount(accountNumber, customer, branch, initialAmount);
            case "Cheque Account" -> new ChequeAccount(
                accountNumber, customer, branch, initialAmount, companyName, companyAddress
            );
            default -> throw new IllegalArgumentException("Unsupported account type: " + accountType);
        };

        accountDAO.insertAccount(newAccount);
        customer.addAccount(newAccount);

        if (newAccount.getBalance() > 0) {
            transactionDAO.recordTransaction(
                newAccount.getAccountNumber(),
                "OPENING_DEPOSIT",
                newAccount.getBalance(),
                newAccount.getBalance()
            );
        }
        return newAccount;
    }

    public void deposit(Account account, double amount) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("Please select an account.");
        }
        account.deposit(amount);
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        transactionDAO.recordTransaction(
            account.getAccountNumber(),
            "DEPOSIT",
            amount,
            account.getBalance()
        );
    }

    public void withdraw(Account account, double amount) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("Please select an account.");
        }
        account.withdraw(amount);
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        transactionDAO.recordTransaction(
            account.getAccountNumber(),
            "WITHDRAWAL",
            amount,
            account.getBalance()
        );
    }

    public double applyMonthlyInterest(Account account) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("Please select an account.");
        }
        if (!(account instanceof InterestBearing interestAccount)) {
            throw new UnsupportedOperationException(
                account.getAccountType() + " does not earn monthly interest."
            );
        }
        double interestEarned = interestAccount.calculateMonthlyInterest();
        interestAccount.applyMonthlyInterest();
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        transactionDAO.recordTransaction(
            account.getAccountNumber(),
            "MONTHLY_INTEREST",
            interestEarned,
            account.getBalance()
        );
        return interestEarned;
    }

    public List<String> getTransactionHistory(String accountNumber) throws SQLException {
        return transactionDAO.getHistoryForAccount(accountNumber);
    }
}