package main.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.event.ActionEvent;
import java.io.File;
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

        User user = new User("Admin", "Admin", "adminman", "Admin123");
        TwoFeetApp.addUser(user);
        File logoFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Logo1.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

    }

    public void firstLoginButtonOnAction(ActionEvent event){
        LoginController loginController = new LoginController();
        loginController.start();
        Stage currentStage = (Stage) firstLoginButton.getScene().getWindow();
        currentStage.close();
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
