package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MovedOutDetailController {

    @FXML private Label titleLabel;
    @FXML private TextArea contentArea;

    public void setData(String title, String text) {
        titleLabel.setText(title);
        contentArea.setText(text);
    }


    @FXML
    private void onBack(ActionEvent event) {
        openWindow("/moved_out_checklist.fxml", 520, 600);
        closeCurrent(event);
    }

    private void openWindow(String fxml, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), w, h));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrent(ActionEvent event) {
        Stage current = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        current.close();
    }
}
