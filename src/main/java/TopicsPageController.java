package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TopicsPageController {

    @FXML private Label categoryLabel;
    @FXML private ListView<HelpTopic> topicsList;
    @FXML private Button backButton;
    @FXML private Button nextButton;

    private HelpService helpService = new HelpService();
    private HelpCategory currentCategory;

    @FXML
    public void initialize() {
        // Vis topic titler i listen
        topicsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(HelpTopic item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        // Deaktiver "Frem" knappen indtil noget er valgt
        topicsList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            nextButton.setDisable(newV == null);
        });

        nextButton.setDisable(true);
    }

    public void setCategory(HelpCategory category) {
        this.currentCategory = category;

        // Opdater label
        categoryLabel.setText("Kategori: " + prettyName(category));

        // Hent og vis topics
        List<HelpTopic> topics = helpService.getTopicsByCategory(category);
        topicsList.getItems().setAll(topics);
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
            // Gå tilbage til kategori-siden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/help_page.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nextButtonOnAction(ActionEvent event) {
        HelpTopic selected = topicsList.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                // Load TopicDetailPage.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TopicsDetailPage.fxml"));
                Scene scene = new Scene(loader.load(), 399, 844);

                // Send topic og kategori videre
                TopicDetailController controller = loader.getController();
                controller.setTopic(selected);
                controller.setCategory(currentCategory);

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}