package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML private ImageView logoImageView;
    @FXML private ImageView checklistImageView;
    @FXML private ImageView budgetImageView;
    @FXML private ImageView todoImageView;
    @FXML private ImageView helpServiceImageView;

    @FXML private Button movingOutChecklistButton;
    @FXML private Button budgetButton;
    @FXML private Button todoButton;
    @FXML private Button helpServiceButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageSafe(logoImageView, "/Logo1.png");
        setImageSafe(checklistImageView, "/Checklist.png");
        setImageSafe(budgetImageView, "/Budget.png");
        setImageSafe(todoImageView, "/ShoppingList.png");
        setImageSafe(helpServiceImageView, "/Guide.png");

        // billeder må ikke blokere klik
        if (logoImageView != null) logoImageView.setMouseTransparent(true);
        if (checklistImageView != null) checklistImageView.setMouseTransparent(true);
        if (budgetImageView != null) budgetImageView.setMouseTransparent(true);
        if (todoImageView != null) todoImageView.setMouseTransparent(true);
        if (helpServiceImageView != null) helpServiceImageView.setMouseTransparent(true);
    }

    private void setImageSafe(ImageView view, String resourcePath) {
        if (view == null) return;
        URL res = getClass().getResource(resourcePath);
        if (res != null) {
            view.setImage(new Image(res.toExternalForm()));
        } else {
            System.out.println("Missing resource: " + resourcePath);
        }
    }

    // ✅ Moving Out Checklist
  /*  @FXML
    private void movingOutChecklistButtonOnAction(ActionEvent event) {
        MovedOutChecklistController movedOut = new MovedOutChecklistController();
        movedOut.start();

        Stage current = (Stage) movingOutChecklistButton.getScene().getWindow();
        current.close();
    }*/

    // ✅ Todo
    @FXML
    private void todoButtonOnAction(ActionEvent event) {
        TodoController todo = new TodoController();
        todo.start();
        Stage current = (Stage) todoButton.getScene().getWindow();
        current.close();
    }

    // ✅ Help Service
    @FXML
    private void helpServiceButtonOnAction(ActionEvent event) {
        TwoFeetApp app = new TwoFeetApp();
        Stage stage = new Stage();
        app.start(stage);

        Stage current = (Stage) helpServiceButton.getScene().getWindow();
        current.close();
    }

    // ✅ Budget (bare placeholder)
    @FXML
    private void budgetButtonOnAction(ActionEvent event) {
        System.out.println("Budget clicked (kan laves senere)");
    }
}

