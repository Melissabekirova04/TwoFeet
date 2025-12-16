package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.util.CloseProgram;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.io.File;
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
    @FXML private Button mainCloseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageSafe(logoImageView, "/Logo1.png");
        setImageSafe(checklistImageView, "/MovedOut.png");
        setImageSafe(budgetImageView, "/budget2.png");
        setImageSafe(todoImageView, "/Todo2.png");
        setImageSafe(helpServiceImageView, "/Guide2.png");

        // billeder må ikke blokere klik
        if (logoImageView != null) logoImageView.setMouseTransparent(true);
        if (checklistImageView != null) checklistImageView.setMouseTransparent(true);
        if (budgetImageView != null) budgetImageView.setMouseTransparent(true);
        if (todoImageView != null) todoImageView.setMouseTransparent(true);
        if (helpServiceImageView != null) helpServiceImageView.setMouseTransparent(true);
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(fxmlLoader.load(), 399, 844);
        registerStage.setScene(scene);
        registerStage.show();
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
    @FXML
    private void movingOutChecklistButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/moved_out_checklist.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), 399, 844));
            stage.show();

            // luk main page
            Stage current = (Stage) movingOutChecklistButton.getScene().getWindow();
            current.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Todo
    @FXML
    private void todoButtonOnAction(ActionEvent event) {
        TodoController todo = new TodoController();
        todo.startTodo();
        Stage current = (Stage) todoButton.getScene().getWindow();
        current.close();
    }

    // ✅ Help Service
    @FXML
    private void helpServiceButtonOnAction(ActionEvent event) {
        System.out.println("Help Service clicked"); // debug

        try {
            var url = getClass().getResource("/help_page.fxml");
            if (url == null) {
                System.out.println("FEJL: /help_page.fxml blev ikke fundet i resources!");
                Alert a = new Alert(Alert.AlertType.ERROR, "Kunne ikke finde help_page.fxml i resources.", ButtonType.OK);
                a.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), 399, 844));
            stage.show();

            Stage current = (Stage) helpServiceButton.getScene().getWindow();
            current.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Kunne ikke åbne help_page.fxml.\nSe Console for fejl.", ButtonType.OK);
            a.showAndWait();
        }
    }




    @FXML
    public void budgetButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/budget.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainCloseButtonOnAction(){
        Stage stage = (Stage) mainCloseButton.getScene().getWindow();
        CloseProgram.close(stage);
    }

}

