package main.java;

public class BudgetPlan {
    private final int id;
    private final int userId;
    private final String periodType; // WEEK, MONTH, END_OF_MONTH
    private final double income;
    private final double savings;
    private final double totalFixed;
    private final int days;
    private final double dailyBudget;
    private final String startDate;
    private final String endDate;
    private final String createdAt;

    public BudgetPlan(int id, int userId, String periodType, double income, double savings,
                      double totalFixed, int days, double dailyBudget,
                      String startDate, String endDate, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.periodType = periodType;
        this.income = income;
        this.savings = savings;
        this.totalFixed = totalFixed;
        this.days = days;
        this.dailyBudget = dailyBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getPeriodType() { return periodType; }
    public double getIncome() { return income; }
    public double getSavings() { return savings; }
    public double getTotalFixed() { return totalFixed; }
    public int getDays() { return days; }
    public double getDailyBudget() { return dailyBudget; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getCreatedAt() { return createdAt; }
}
