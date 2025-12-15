package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BudgetController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, Double> amountColumn;
    @FXML private TableColumn<Budget, Double> balanceColumn;
    @FXML private TableColumn<Budget, String> timestampColumn;


    private double balance = 0;

    private DbManager dbManager = new DbManager();

    @FXML
    public void initialize() {
        dbManager.connect();
        loadBudget();
    }
    public void loadBudget() {
        int userId = LoginController.currentUserId;
        List<Budget> budgetEntries = dbManager.getBudget(userId);

        if (!budgetEntries.isEmpty()) {
            balance = budgetEntries.get(0).getBalanceAfter();
        } else {
            balance = 0;
        }
        updateBalance();

        // Populate table
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        budgetTable.getItems().setAll(budgetEntries);
    }



    public void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount < 0) return;

            balance += amount;
            dbManager.insertBudgetEntry(LoginController.currentUserId, amount, balance);
            loadBudget(); // refresh table and balance

        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }


    public void withdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount < 0 || amount > balance) return;

            balance -= amount;
            dbManager.insertBudgetEntry(LoginController.currentUserId, amount,  balance);
            loadBudget(); // refresh table and balance

        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }


    private void updateBalance() {
        balanceLabel.setText(balance + " kr");
        amountField.clear();
    }
}
