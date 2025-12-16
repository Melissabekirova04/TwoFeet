package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class TopicDetailController {

    @FXML private Label titleLabel;
    @FXML private TextArea answerArea;
    @FXML private Button backButton;
    @FXML private Button homeButton;

    private HelpTopic currentTopic;
    private HelpCategory currentCategory;

    @FXML
    public void initialize() {
        // Sæt TextArea til read-only
        if (answerArea != null) {
            answerArea.setEditable(false);
            answerArea.setWrapText(true);
        }
    }

    public void setTopic(HelpTopic topic) {
        this.currentTopic = topic;

        // Opdater UI
        if (titleLabel != null) {
            titleLabel.setText(topic.getTitle());
        }

        if (answerArea != null) {
            answerArea.setText(topic.getAnswerText());
        }
    }

    public void setCategory(HelpCategory category) {
        this.currentCategory = category;
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            // Gå tilbage til topics-listen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TopicsPage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            // Send kategorien tilbage
            TopicsPageController controller = loader.getController();
            controller.setCategory(currentCategory);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void homeButtonOnAction(ActionEvent event) {
        try {
            // Gå til hovedmenuen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}