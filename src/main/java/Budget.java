package main.java;

public class Budget {
    private final double amount;
    private final double balanceAfter;
    private final String timestamp;

    public Budget(double amount, double balanceAfter, String timestamp) {
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getTimestamp() { return timestamp; }
}
