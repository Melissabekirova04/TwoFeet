package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TodoController implements Initializable {

    @FXML private TextField taskField;
    @FXML private ListView<Task> taskListView;
    @FXML private Button backButton;

    private DbManager dbManager;

    public void startTodo() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/todo.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), 399, 844));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbManager = new DbManager();
        dbManager.connect();

        setupCells();
        loadTodos();
    }

    @FXML
    public void addTodoOnAction() {
        String task = taskField.getText();
        if (task == null || task.isBlank()) return;

        int userId = LoginController.currentUserId;
        dbManager.addTodo(userId, task.trim());

        taskField.clear();
        loadTodos();
    }

    private void loadTodos() {
        int userId = LoginController.currentUserId;
        List<Task> todos = dbManager.getTodos(userId);
        taskListView.getItems().setAll(todos);
    }

    private void setupCells() {
        taskListView.setCellFactory(lv -> new ListCell<>() {
            private final Label text = new Label();
            private final Button deleteButton = new Button("Delete");
            private final HBox row = new HBox(10, text, deleteButton);

            {
                HBox.setHgrow(text, Priority.ALWAYS);

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
                    setGraphic(null);
                } else {
                    // Task ser ud til at have getTask() (du bruger den andre steder)
                    text.setText(item.getTask());
                    setGraphic(row);
                }
            }
        });
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) taskField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));

            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setScene(new Scene(loader.load(), 399, 844));
            mainStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Kunne ikke åbne mainpage.fxml", ButtonType.OK);
            a.showAndWait();
        }
    }
}
