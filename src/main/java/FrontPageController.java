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

import java.net.URL;
import java.util.ResourceBundle;

public class FrontPageController implements Initializable {
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button firstLoginButton;
    @FXML
    private Button firstRegisterButton;
    @FXML
    private Button firstCloseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        URL logoUrl = getClass().getResource("/Logo1.png");
        if (logoUrl != null) {
            logoImageView.setImage(new Image(logoUrl.toExternalForm()));
        }
    }

    public void firstLoginButtonOnAction(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            loginStage.setScene(scene);
            loginStage.show();

            Stage currentStage = (Stage) firstLoginButton.getScene().getWindow();
            currentStage.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void firstRegisterButtonOnAction(ActionEvent event){
        RegisterController registerController = new RegisterController();
        registerController.start();
        Stage currentStage = (Stage) firstRegisterButton.getScene().getWindow();
        currentStage.close();
    }

    public void firstCloseButtonOnAction(ActionEvent event){
        Stage stage = (Stage) firstCloseButton.getScene().getWindow();
        stage.close();
    }
}
