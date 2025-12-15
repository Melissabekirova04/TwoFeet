

package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    @FXML private ListView<Task> taskListView;

    private DbManager dbManager;

    public void startTodo() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/todo.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), 520, 523));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbManager = new DbManager();
        dbManager.connect();

        setupDeleteButtons();
        loadTodos();
    }

    @FXML
    public void addTodoOnAction() {
        String task = taskField.getText();

        if (task == null || task.isBlank()) return;

        int userId = LoginController.currentUserId;
        dbManager.addTodo(userId, task);

        taskField.clear();
        loadTodos();
    }

    private void loadTodos() {
        int userId = LoginController.currentUserId;

        List<Task> todos = dbManager.getTodos(userId);
        taskListView.getItems().setAll(todos);
    }

    private void setupDeleteButtons() {
        taskListView.setCellFactory(lv -> new ListCell<>() {

            private final Button deleteButton = new Button("Delete");
            private final HBox box = new HBox(10, deleteButton);

            {
                container.getChildren().add(deleteButton);

                deleteButton.setOnAction(e -> {
                    Task task = getItem();
                    if (task != null) {
                        dbManager.deleteTask(task.getId());
                        getListView().getItems().remove(task);
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
                    setText(item.getTask());
                    setGraphic(box);
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
