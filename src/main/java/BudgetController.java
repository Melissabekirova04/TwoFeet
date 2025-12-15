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

    @FXML private ComboBox<String> periodCombo;
    @FXML private TextField incomeField;
    @FXML private TextField savingsField;

    @FXML private TextField expenseNameField;
    @FXML private TextField expenseAmountField;
    @FXML private ListView<FixedExpense> expensesList;

    @FXML private Label totalFixedLabel;
    @FXML private Label disposableLabel;
    @FXML private Label dailyBudgetLabel;
    @FXML private Label daysLabel;
    @FXML private Label periodDatesLabel;

    @FXML private Button backButton;

    private final List<FixedExpense> draftExpenses = new ArrayList<>();

    @FXML
    public void initialize() {
        periodCombo.getItems().setAll("Uge", "Måned", "Sidst på måneden");
        periodCombo.getSelectionModel().selectFirst();

        refreshExpensesList();
        updatePeriodPreview();

        // Opdater periode preview når man vælger noget nyt
        periodCombo.setOnAction(e -> updatePeriodPreview());
    }

    @FXML
    private void addExpense(ActionEvent event) {
        String name = expenseNameField.getText() == null ? "" : expenseNameField.getText().trim();
        String amountTxt = expenseAmountField.getText() == null ? "" : expenseAmountField.getText().trim().replace(",", ".");

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Skriv et navn på udgiften.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountTxt);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Beløb skal være et tal.");
            return;
        }

        if (amount < 0) {
            showAlert(Alert.AlertType.WARNING, "Beløb må ikke være negativt.");
            return;
        }

        draftExpenses.add(new FixedExpense(name, amount));
        expenseNameField.clear();
        expenseAmountField.clear();

        refreshExpensesList();
        updateTotalsOnly();
    }

    @FXML
    private void removeSelectedExpense(ActionEvent event) {
        FixedExpense selected = expensesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            draftExpenses.remove(selected);
            refreshExpensesList();
            updateTotalsOnly();
        }
    }

    // ✅ KUN beregning (ingen database)
    @FXML
    private void calculateBudget(ActionEvent event) {

        double income = parseNonNegative(incomeField.getText(), "Budget/indtægt");
        double savings = parseNonNegative(savingsField.getText(), "Opsparing");
        if (income < 0 || savings < 0) return;

        double totalFixed = draftExpenses.stream()
                .mapToDouble(FixedExpense::getAmount)
                .sum();

        PeriodInfo p = computePeriodInfo(periodCombo.getValue());

        double disposable = income - totalFixed - savings;
        double dailyBudget = p.days > 0 ? disposable / p.days : disposable;

        // Opdater UI
        totalFixedLabel.setText(formatKr(totalFixed));
        disposableLabel.setText(formatKr(disposable));
        dailyBudgetLabel.setText(formatKr(dailyBudget) + " pr. dag");

        daysLabel.setText(p.days + " dage");
        periodDatesLabel.setText(p.startDate + " → " + p.endDate);

        if (dailyBudget < 0) {
            showAlert(Alert.AlertType.WARNING,
                    "Dit dagligdags-budget er negativt.\n" +
                            "Dine faste udgifter og opsparing er større end dit budget.");
        }
    }

    // --- Helpers: UI ---
    private void refreshExpensesList() {
        expensesList.getItems().setAll(draftExpenses);
    }

    private void updateTotalsOnly() {
        double totalFixed = draftExpenses.stream().mapToDouble(FixedExpense::getAmount).sum();
        totalFixedLabel.setText(formatKr(totalFixed));
    }

    private void updatePeriodPreview() {
        PeriodInfo p = computePeriodInfo(periodCombo.getValue());
        daysLabel.setText(p.days + " dage");
        periodDatesLabel.setText(p.startDate + " → " + p.endDate);
    }

    // --- Period logic ---
    private static class PeriodInfo {
        int days;
        LocalDate startDate;
        LocalDate endDate;
    }

    private PeriodInfo computePeriodInfo(String selection) {
        LocalDate today = LocalDate.now();
        PeriodInfo p = new PeriodInfo();

        if ("Uge".equals(selection)) {
            p.days = 7;
            p.startDate = today;
            p.endDate = today.plusDays(6);

        } else if ("Måned".equals(selection)) {
            p.startDate = today.withDayOfMonth(1);
            p.endDate = today.withDayOfMonth(today.lengthOfMonth());
            p.days = today.lengthOfMonth();

        } else { // Sidst på måneden
            LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
            p.startDate = today;
            p.endDate = lastDay;
            p.days = (int) ChronoUnit.DAYS.between(today, lastDay) + 1; // inkl. i dag
        }

        return p;
    }

    // --- Parsing/helpers ---
    private double parseNonNegative(String text, String fieldName) {
        String s = (text == null ? "" : text.trim()).replace(",", ".");
        if (s.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, fieldName + " mangler.");
            return -1;
        }
        try {
            double v = Double.parseDouble(s);
            if (v < 0) {
                showAlert(Alert.AlertType.WARNING, fieldName + " må ikke være negativ.");
                return -1;
            }
            return v;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, fieldName + " skal være et tal.");
            return -1;
        }
    }

    private String formatKr(double v) {
        return String.format("%.2f kr", v);
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.showAndWait();
    }

    // Back til mainpage
    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
