package main.java;

public class FixedExpense {
    private final int id;
    private final int planId;
    private final String name;
    private final double amount;

    // ✅ Bruges af dit budget-UI (uden database)
    public FixedExpense(String name, double amount) {
        this(-1, -1, name, amount);
    }

    // ✅ Bruges af DbManager (hvis der står DB-kode der henter udgifter)
    public FixedExpense(int id, int planId, String name, double amount) {
        this.id = id;
        this.planId = planId;
        this.name = name;
        this.amount = amount;
    }

    public int getId() { return id; }
    public int getPlanId() { return planId; }
    public String getName() { return name; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return name + " - " + String.format("%.2f", amount) + " kr";
    }
}
