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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BudgetController {

    /* ===================== FXML ===================== */

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

    /* ===================== DATA ===================== */

    private final List<FixedExpense> draftExpenses = new ArrayList<>();

    /* ===================== INIT ===================== */

    @FXML
    public void initialize() {
        periodCombo.getItems().addAll("Uge", "Måned", "Sidst på måneden");
        periodCombo.getSelectionModel().select("Måned");
        updatePeriod();
        periodCombo.setOnAction(e -> updatePeriod());
    }

    /* ===================== PERIODE ===================== */

    private void updatePeriod() {
        LocalDate start = LocalDate.now();
        LocalDate end;

        switch (periodCombo.getValue()) {
            case "Uge" -> end = start.plusDays(6);
            case "Sidst på måneden" -> end = start.withDayOfMonth(start.lengthOfMonth());
            default -> end = start.plusMonths(1).minusDays(1);
        }

        long days = ChronoUnit.DAYS.between(start, end) + 1;

        periodDatesLabel.setText(start + " → " + end);
        daysLabel.setText(String.valueOf(days));
    }

    /* ===================== FASTE UDGIFTER ===================== */

    @FXML
    private void addExpense(ActionEvent event) {
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
    private void removeSelectedExpense(ActionEvent event) {
        FixedExpense selected = expensesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            draftExpenses.remove(selected);
            expensesList.getItems().remove(selected);
            updateTotalsOnly();
        }
    }

    /* ===================== BEREGNING ===================== */

    @FXML
    private void calculateBudget(ActionEvent event) {
        try {
            double income = Double.parseDouble(incomeField.getText());
            double savings = savingsField.getText().isBlank()
                    ? 0
                    : Double.parseDouble(savingsField.getText());

            double totalFixed = draftExpenses.stream()
                    .mapToDouble(FixedExpense::getAmount)
                    .sum();

            long days = Long.parseLong(daysLabel.getText());

            double disposable = income - savings - totalFixed;
            double daily = disposable / days;

            totalFixedLabel.setText(String.format("%.2f kr", totalFixed));
            disposableLabel.setText(String.format("%.2f kr", disposable));
            dailyBudgetLabel.setText(String.format("%.2f kr", daily));

        } catch (Exception e) {
            disposableLabel.setText("-");
            dailyBudgetLabel.setText("-");
        }
    }

    private void updateTotalsOnly() {
        double totalFixed = draftExpenses.stream()
                .mapToDouble(FixedExpense::getAmount)
                .sum();

        totalFixedLabel.setText(String.format("%.2f kr", totalFixed));
    }

    /* ===================== NAV ===================== */

    @FXML
    private void backButtonOnAction(ActionEvent event) throws IOException {
        MainPageController mainPageController = new MainPageController();
        mainPageController.start();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void openBudgetHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/budget_history.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Mine budgetter");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
