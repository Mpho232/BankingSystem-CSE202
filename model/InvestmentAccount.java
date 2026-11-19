package model;

public class InvestmentAccount extends Account implements InterestBearing {
    public static final double MONTHLY_INTEREST_RATE = 0.05; // 5% monthly
    public static final double MINIMUM_OPENING_DEPOSIT = 500.00; // BWP 500.00

    public InvestmentAccount(String accountNumber, Customer owner, String branch, double initialDeposit) {
        super(accountNumber, owner, branch, initialDeposit);
        if (initialDeposit < MINIMUM_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                String.format("Investment Account requires a minimum initial deposit of BWP %.2f.",
                              MINIMUM_OPENING_DEPOSIT)
            );
        }
    }

    public double getMonthlyInterestRate() {
        return MONTHLY_INTEREST_RATE;
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
    public double calculateMonthlyInterest() {
        return balance * MONTHLY_INTEREST_RATE;
    }

    @Override
    public void applyMonthlyInterest() {
        balance += calculateMonthlyInterest();
    }

    @Override
    public String getAccountType() {
        return "Investment Account";
    }
}