package model;

public class ChequeAccount extends Account {
    private final String companyName;
    private final String companyAddress;

    public ChequeAccount(String accountNumber, Customer owner, String branch,
                         double initialBalance, String companyName, String companyAddress) {
        super(accountNumber, owner, branch, initialBalance);
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name is required for a Cheque Account.");
        }
        if (companyAddress == null || companyAddress.isBlank()) {
            throw new IllegalArgumentException("Company address is required for a Cheque Account.");
        }
        this.companyName = companyName.trim();
        this.companyAddress = companyAddress.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds for withdrawal.");
        }
        balance -= amount;
    }

    @Override
    public String getAccountType() {
        return "Cheque Account";
    }
}