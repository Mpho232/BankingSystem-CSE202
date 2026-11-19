package model;

public abstract class Account {
    private final String accountNumber;
    private final Customer owner;
    private final String branch;
    protected double balance;

    public Account(String accountNumber, Customer owner, String branch, double initialBalance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required.");
        }
        if (owner == null) {
            throw new IllegalArgumentException("An account must be held by a customer.");
        }
        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Branch is required.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber.trim();
        this.owner = owner;
        this.branch = branch.trim();
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public String getBranch() {
        return branch;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    public abstract void withdraw(double amount);

    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("%s (%s) [%s] - BWP %.2f",
                getAccountType(), accountNumber, branch, balance);
    }
}