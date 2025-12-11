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
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView checklistImageView;
    @FXML
    private ImageView budgetImageView;
    @FXML
    private ImageView helpServiceImageView;
    @FXML
    private ImageView todoImageView;
    @FXML
    private Button helpServiceButton;
    @FXML
    private Button todoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        // Logo
        URL logoUrl = getClass().getResource("/Logo1.png");
        if (logoUrl != null) {
            logoImageView.setImage(new Image(logoUrl.toExternalForm()));
        }

        // Checklist
        URL checklistUrl = getClass().getResource("/Checklist.png");
        if (checklistUrl != null) {
            checklistImageView.setImage(new Image(checklistUrl.toExternalForm()));
        }

        // Budget
        URL budgetUrl = getClass().getResource("/Budget.png");
        if (budgetUrl != null) {
            budgetImageView.setImage(new Image(budgetUrl.toExternalForm()));
        }

        // Todo / Shopping list
        URL shoppingListUrl = getClass().getResource("/ShoppingList.png");
        if (shoppingListUrl != null) {
            todoImageView.setImage(new Image(shoppingListUrl.toExternalForm()));
        }

        // Help/Guide
        URL helpServiceUrl = getClass().getResource("/Guide.png");
        if (helpServiceUrl != null) {
            helpServiceImageView.setImage(new Image(helpServiceUrl.toExternalForm()));
        }
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(fxmlLoader.load(), 520, 600);
        registerStage.setScene(scene);
        registerStage.show();
    }

    public void helpServiceButtonOnAction(ActionEvent event){
        TwoFeetApp app = new TwoFeetApp();
        Stage stage = new Stage();
        app.start(stage);
        Stage currentStage = (Stage) helpServiceButton.getScene().getWindow();
        currentStage.close();
    }

    public void todoButtonOnAction(ActionEvent event){
        TodoController todo = new TodoController();
        todo.start();
        Stage stage = (Stage) todoButton.getScene().getWindow();
        stage.close();
    }
}
