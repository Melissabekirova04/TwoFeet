package main.java;

import java.util.Scanner;

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

    public double withDraw(double balance){
        double amount;
        System.out.println("Enter amount to withdraw");
        amount = scanner.nextDouble();

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
