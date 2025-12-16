package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpPageController {

    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private ListView<HelpCategory> categoryList;

    @FXML
    public void initialize() {

        categoryList.getItems().setAll(HelpCategory.values());

        categoryList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(HelpCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : prettyName(item));
            }
        });

        // ✅ gør listen kun så høj som indholdet
        categoryList.setFixedCellSize(44);
        categoryList.setPrefHeight(
                categoryList.getItems().size() * categoryList.getFixedCellSize() + 2
        );

        // Deaktiver "Frem" indtil noget er valgt
        nextButton.setDisable(true);
        categoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) ->
                nextButton.setDisable(newV == null)
        );
    }

    private String prettyName(HelpCategory c) {
        return switch (c) {
            case LAUNDRY -> "Tøjvask";
            case ELECTRONICS -> "Elektronik (køl/frys)";
            case CLEANING -> "Rengøring";
        };
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nextButtonOnAction(ActionEvent event) {
        HelpCategory selected = categoryList.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TopicsPage.fxml"));
                Scene scene = new Scene(loader.load(), 399, 844);

                TopicsPageController controller = loader.getController();
                controller.setCategory(selected);

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Kunne ikke loade TopicsPage.fxml");
            }
        }
    }
}
