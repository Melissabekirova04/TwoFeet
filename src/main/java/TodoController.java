package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TodoController {

    @FXML private TextField usernameField;
    @FXML private TextField taskField;
    @FXML private ListView<Task> taskListView;

    @FXML private Button backButton;

    private DbManager dbManager;
    private Runnable onSuccess;

    public void startTodo(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/todo.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 520, 523);
            registerStage.setScene(scene);
            registerStage.show();

            dbManager = new DbManager();

            TodoController controller = fxmlLoader.getController();
            controller.initData(dbManager, onSuccess);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData(DbManager dbManager, Runnable onSuccess) {
        this.dbManager = dbManager;
        this.onSuccess = onSuccess;
        setupDeleteButtons();
    }

    @FXML
    private void onAdd() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String task = taskField.getText() == null ? "" : taskField.getText().trim();

        if (username.isEmpty() || task.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Udfyld både bruger og opgave");
            return;
        }

        if (dbManager.getUserId(username) == null) {
            dbManager.addUser(username);
        }

        dbManager.addTask(task, username);

        if (onSuccess != null) {
            loadTasksForUser(username);
            onSuccess.run();
        }
    }

    private void loadTasksForUser(String username) {
        var tasks = dbManager.getTasksForUser(username);
        taskListView.getItems().setAll(tasks);
    }

    private void setupDeleteButtons() {
        taskListView.setCellFactory(lv -> new ListCell<Task>() {

            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(10);

            {
                container.getChildren().add(deleteButton);

                deleteButton.setOnAction(e -> {
                    Task item = getItem();
                    if (item != null) {
                        dbManager.deleteTask(item.getId());
                        getListView().getItems().remove(item);
                    }
                });
            }

            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getUsername() + ": " + item.getTask());
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) taskField.getScene().getWindow();
        stage.close();
    }

    // ✅ Back: åbn main menu i NYT vindue (UND) og luk Todo bagefter
    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));

            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setScene(new Scene(loader.load(), 399, 844));
            mainStage.show();

            // luk nuværende todo-vindue bagefter
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kunne ikke åbne mainpage.fxml");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.showAndWait();
    }
}
