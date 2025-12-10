

package main.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.IIOException;
import java.io.IOException;

public class TodoController {

    @FXML private TextField usernameField;
    @FXML private TextField taskField;
    @FXML
    private ListView<Task> taskListView;

    private DbManager dbManager;
    private Runnable onSuccess;

    public void start(){
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

    // Kald denne fra den kode som loader FXML
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

        // Opretter brugeren hvis brugeren ikke findes
        if (dbManager.getUserId(username) == null) {
            dbManager.addUser(username); // opret bruger
        }

        dbManager.addTask(task, username);

        // Callback for at opdatere hoved-UI (hvis du gav en)
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

                // Når knappen trykkes, skal den slette tasken
                deleteButton.setOnAction(e -> {
                    Task item = getItem();  // Hent det aktuelle task-objekt
                    if (item != null) {
                        dbManager.deleteTask(item.getId());  // Slet i databasen
                        getListView().getItems().remove(item);  // Fjern fra GUI
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
                    setText(item.getUsername() + ": " + item.getTask());  // Vis taskens navn og opgave
                    setGraphic(container);  // Sæt knappen som grafisk indhold i cellen
                }
            }
        });
    }


    @FXML
    private void onCancel() {
        Stage stage = (Stage) taskField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.showAndWait();
    }
}


