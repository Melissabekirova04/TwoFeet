package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class TwoFeetApp extends Application {

    private final HelpService helpService = new HelpService();

    @Override
    public void start(Stage stage) {
        // --- Top: Titel ---
        Label header = new Label("TwoFeet – din udflytnings-hjælper");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // --- Venstre: kategori + emneliste ---
        ComboBox<HelpCategory> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(HelpCategory.values());
        categoryBox.setPromptText("Vælg kategori");

        //Skift side knap , Højre
        Button todo = new Button("Todo");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
// HBox der ligger over svarboksen
        HBox answerTopBar = new HBox(10, new Label("Svar:"), spacer, todo);
        answerTopBar.setPadding(new Insets(0, 0, 5, 0));
        answerTopBar.setAlignment(Pos.CENTER_LEFT);   // Label til venstre, knap til højre

        TextArea answerArea = new TextArea();
        answerArea.setEditable(false);
        answerArea.setWrapText(true);

        VBox centerPane = new VBox(10,
                answerTopBar,
                answerArea
        );
        centerPane.setPadding(new Insets(10));

        todo.setOnAction(e -> {
            try {
                DbManager dbManager = new DbManager();
                dbManager.connect();

                // Load FXML
                URL url = getClass().getResource("/todo.fxml");
                if (url == null) {
                    throw new RuntimeException("FXML file not found: /todo.fxml");
                }
                System.out.println("Loading FXML from: " + url);

                FXMLLoader loader = new FXMLLoader(url);
                Parent root = loader.load();

                // Get controller and inject DbManager + callback
                TodoController controller = loader.getController();
                if (controller == null) {
                    throw new RuntimeException("Controller returned null from FXML!");
                } else {
                    controller.initData(dbManager, () -> {
                        try {
                            refreshTasks(); // Refresh your main task list after adding
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }

                // New stage
                Stage stageTodo = new Stage();
                stageTodo.setTitle("Add Todo Task");
                stageTodo.initOwner(todo.getScene().getWindow());
                stageTodo.initModality(Modality.APPLICATION_MODAL);
                stageTodo.setScene(new Scene(root));
                stageTodo.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace(); // Full stacktrace for debugging
                // Show friendly error alert
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Kunne ikke åbne todo-dialog:\n" + ex.getMessage(),
                        ButtonType.OK);
                alert.showAndWait();
            }
        });


        // vis enum-navne pænere (dansk tekst)
        categoryBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(HelpCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : getCategoryName(item));
            }
        });
        categoryBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(HelpCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Vælg kategori" : getCategoryName(item));
            }
        });

        ListView<HelpTopic> topicList = new ListView<>();
        topicList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(HelpTopic item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        VBox leftPane = new VBox(10,
                new Label("Kategori:"),
                categoryBox,
                new Label("Emner:"),
                topicList
        );
        leftPane.setPadding(new Insets(10));
        leftPane.setPrefWidth(280);

        // --- Midten: svar-område ---
       /* TextArea answerArea = new TextArea();
        answerArea.setEditable(false);
        answerArea.setWrapText(true);

        VBox centerPane = new VBox(10,
                new Label("Svar:"),
                answerArea
        );
        centerPane.setPadding(new Insets(10));*/

        // --- Nederst: fritekst-spørgsmål ---
        TextField questionField = new TextField();
        questionField.setPromptText("Skriv dit eget spørgsmål her (fx: Hvordan vasker jeg hvidt tøj?)");
        Button askButton = new Button("Spørg");

        HBox bottomPane = new HBox(10, questionField, askButton);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.CENTER_LEFT);

        // --- Event-håndtering ---

        // Når man vælger en kategori
        categoryBox.setOnAction(e -> {
            HelpCategory selected = categoryBox.getValue();
            if (selected != null) {
                List<HelpTopic> topics = helpService.getTopicsByCategory(selected);
                topicList.getItems().setAll(topics);
            }
        });

        // Når man klikker på et emne
        topicList.getSelectionModel().selectedItemProperty().addListener((obs, oldTopic, newTopic) -> {
            if (newTopic != null) {
                answerArea.setText(newTopic.getAnswerText());
            }
        });

        // Fritekst-spørgsmål
        askButton.setOnAction(e -> {
            String q = questionField.getText();
            if (q == null || q.isBlank()) {
                return;
            }
            String answer = helpService.ask(q);
            answerArea.setText(answer);
        });

        // Enter i tekstfeltet = samme som knappen
        questionField.setOnAction(askButton.getOnAction());

        // --- Layout ---
        BorderPane root = new BorderPane();
        BorderPane.setMargin(header, new Insets(10));

        root.setTop(header);
        root.setLeft(leftPane);
        root.setCenter(centerPane);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("TwoFeet – udflytnings-hjælper");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTasks() {
    }

    // Hjælpe-metode: pæne danske navne til kategorier
    private String getCategoryName(HelpCategory c) {
        return switch (c) {
            case HelpCategory.LAUNDRY -> "Tøjvask";
            case HelpCategory.ELECTRONICS -> "Elektronik (køl/frys)";
            case HelpCategory.MOVE_IN -> "Inden indflytning";
            case HelpCategory.STARTERPACK -> "Udflytnings-starterpack";
            case HelpCategory.CLEANING -> "Rengøringsrutine";
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
