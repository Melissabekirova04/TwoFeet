package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class BudgetHistoryController {

    @FXML private TableView<BudgetPlan> budgetTable;
    @FXML private TableColumn<BudgetPlan, String> periodColumn;
    @FXML private TableColumn<BudgetPlan, Double> dailyColumn;
    @FXML private TableColumn<BudgetPlan, String> createdColumn;

    private final DbManager dbManager = new DbManager();

    @FXML
    public void initialize() {
        dbManager.connect();

        periodColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPeriodType()
                )
        );

        dailyColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getDailyBudget()
                )
        );

        createdColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCreatedAt()
                )
        );

        int userId = LoginController.currentUserId;
        budgetTable.getItems().setAll(
                dbManager.getUserBudgetHistory(userId)
        );
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) budgetTable.getScene().getWindow();
        stage.close();
    }
}
