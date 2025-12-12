package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BudgetController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    private double balance = 0;

    private DbManager dbManager = new DbManager();

    @FXML
    public void initialize() {
        dbManager.connect();
    }

    public void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount < 0) {
                System.out.println("Deposit cannot be negative");
                return;
            }

            balance += amount;
            updateBalance();

            dbManager.insertBudgetEntry(amount, "deposit", balance);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    public void withdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount < 0 || amount > balance) {
                System.out.println("Invalid withdraw amount");
                return;
            }

            balance -= amount;
            updateBalance();

            dbManager.insertBudgetEntry(amount, "withdraw", balance);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }

    private void updateBalance() {
        balanceLabel.setText(balance + " kr");
        amountField.clear();
    }
}
