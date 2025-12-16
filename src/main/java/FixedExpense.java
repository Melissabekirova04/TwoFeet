package main.java;

public class FixedExpense {

    private final String name;
    private final double amount;

    public FixedExpense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return name + " - " + String.format("%.2f kr", amount);
    }
}
