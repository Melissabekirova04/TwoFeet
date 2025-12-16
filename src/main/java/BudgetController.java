package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetController {

    @FXML private ComboBox<String> periodCombo;
    @FXML private Label periodDatesLabel;
    @FXML private Label daysLabel;

    @FXML private TextField incomeField;
    @FXML private TextField savingsField;

    @FXML private TextField expenseNameField;
    @FXML private TextField expenseAmountField;
    @FXML private ListView<FixedExpense> expensesList;

    @FXML private Label totalFixedLabel;
    @FXML private Label disposableLabel;
    @FXML private Label dailyBudgetLabel;

    private final DbManager dbManager = new DbManager();
    private final List<FixedExpense> draftExpenses = new ArrayList<>();

    // Gemte værdier efter beregning
    private double calculatedDailyBudget;
    private double totalFixed;
    private int days;
    private LocalDate startDate;
    private LocalDate endDate;

    @FXML
    public void initialize() {
        dbManager.connect();

        periodCombo.getItems().addAll("Uge", "Måned");
        periodCombo.getSelectionModel().select("Måned");
        updatePeriod();
        periodCombo.setOnAction(e -> updatePeriod());
    }

    private void updatePeriod() {
        startDate = LocalDate.now();

        if (periodCombo.getValue().equals("Uge")) {
            endDate = startDate.plusDays(6);
            days = 7;
        } else {
            endDate = startDate.plusMonths(1).minusDays(1);
            days = endDate.getDayOfMonth();
        }

        periodDatesLabel.setText(startDate + " → " + endDate);
        daysLabel.setText(String.valueOf(days));
    }

    @FXML
    private void addExpense() {
        try {
            String name = expenseNameField.getText();
            double amount = Double.parseDouble(expenseAmountField.getText());

            if (name.isBlank() || amount <= 0) return;

            FixedExpense expense = new FixedExpense(name, amount);
            draftExpenses.add(expense);
            expensesList.getItems().add(expense);

            expenseNameField.clear();
            expenseAmountField.clear();

            updateTotalsOnly();
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void removeSelectedExpense() {
        FixedExpense selected = expensesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            draftExpenses.remove(selected);
            expensesList.getItems().remove(selected);
            updateTotalsOnly();
        }
    }

    private void updateTotalsOnly() {
        totalFixed = draftExpenses.stream().mapToDouble(FixedExpense::getAmount).sum();
        totalFixedLabel.setText(String.format("%.2f kr", totalFixed));
    }

    @FXML
    private void calculateBudget() {
        try {
            double income = Double.parseDouble(incomeField.getText());
            double savings = savingsField.getText().isBlank()
                    ? 0
                    : Double.parseDouble(savingsField.getText());

            double disposable = income - savings - totalFixed;
            calculatedDailyBudget = disposable / days;

            disposableLabel.setText(String.format("%.2f kr", disposable));
            dailyBudgetLabel.setText(String.format("%.2f kr", calculatedDailyBudget));

        } catch (NumberFormatException e) {
            showAlert("Ugyldigt input", "Indtast tal i budget og opsparing.");
        }
    }

    @FXML
    private void saveBudget() {
        if (LoginController.currentUserId == -1) {
            showAlert("Fejl", "Ingen bruger logget ind.");
            return;
        }

        int planId = dbManager.insertBudgetPlan(
                LoginController.currentUserId,
                periodCombo.getValue(),
                Double.parseDouble(incomeField.getText()),
                savingsField.getText().isBlank() ? 0 : Double.parseDouble(savingsField.getText()),
                totalFixed,
                days,
                calculatedDailyBudget,
                startDate.toString(),
                endDate.toString()
        );

        for (FixedExpense e : draftExpenses) {
            dbManager.insertFixedExpense(planId, e.getName(), e.getAmount());
        }

        showAlert("Gemt", "Dit budget er nu gemt ✅");
    }

    @FXML
    private void openBudgetHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/budget_history.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Mine budgetter");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
