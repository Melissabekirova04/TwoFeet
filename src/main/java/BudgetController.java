package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class BudgetController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, Double> amountColumn;
    @FXML private TableColumn<Budget, Double> balanceColumn;
    @FXML private TableColumn<Budget, String> timestampColumn;


    @FXML
    private Button backButton;

    private double balance = 0;

    private final DbManager dbManager = new DbManager();

    @FXML
    public void initialize() {
        dbManager.connect();
        loadBudget();
        updateBalance();
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



    @FXML
    public void deposit(ActionEvent event) {
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
    @FXML
    public void withdraw(ActionEvent event) {
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


    // Hvis du vil have en "Save"-knap der bare gemmer den nuværende balance
    @FXML
    public void saveToDatabase(ActionEvent event) {
        // Gem balance uden at ændre den (amount = 0)
        dbManager.insertBudgetEntry(0, "save", balance);
        System.out.println("Saved balance to database: " + balance);
    }

    private void updateBalance() {
        balanceLabel.setText(balance + " kr");
        amountField.clear();
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            // Indlæser mainpage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            // Finder det nuværende vindue og lukker det
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Åbner main menu i et nyt vindue
            Stage mainStage = new Stage();
            mainStage.setScene(scene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
