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

        categoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            nextButton.setDisable(newV == null);
        });
    }

    private String prettyName(HelpCategory c) {
        return switch (c) {
            case LAUNDRY -> "Tøjvask";
            case ELECTRONICS -> "Elektronik (køl/frys)";
            case MOVE_IN -> "Inden indflytning";
            case STARTERPACK -> "Starterpack";
            case CLEANING -> "Rengøring";
        };
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


    @FXML
    private void nextButtonOnAction(ActionEvent event) {
        System.out.println("Valgt kategori: " + categoryList.getSelectionModel().getSelectedItem());
    }
}
