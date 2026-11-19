package model;

public class SavingsAccount extends Account implements InterestBearing {
    public static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, Customer owner, String branch, double initialBalance) {
        super(accountNumber, owner, branch, initialBalance);
    }

    public double getMonthlyInterestRate() {
        return MONTHLY_INTEREST_RATE;
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Withdrawals are not allowed on a Savings Account.");
    }

    @Override
    public double calculateMonthlyInterest() {
        return balance * MONTHLY_INTEREST_RATE;
    }

    @Override
    public void applyMonthlyInterest() {
        balance += calculateMonthlyInterest();
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }
}