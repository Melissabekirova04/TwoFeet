package main.java;

public class Budget {
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Budget(double amount,  double balanceAfter, String timestamp) {
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
