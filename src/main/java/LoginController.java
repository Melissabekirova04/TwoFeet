package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    DBConnector dbConnector = new DBConnector();
    String url1 = "jdbc:sqlite:C:\\Users\\morte\\IdeaProjects\\SceneBuilderTest\\identifier.sqlite"; // DB-sti kan vi tage senere
    Connection connection;

    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        dbConnector.connect(url1);
        connection = dbConnector.getConnection();

        URL brandingUrl = getClass().getResource("/Logo1.png");
        if (brandingUrl != null) {
            brandingImageView.setImage(new Image(brandingUrl.toExternalForm()));
        }

        URL lockUrl = getClass().getResource("/Login.png");
        if (lockUrl != null) {
            lockImageView.setImage(new Image(lockUrl.toExternalForm()));
        }
    }

    public void loginButtonOnAction(ActionEvent event){

        if(!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()){
            try {
                validateLogin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() throws IOException {
        MainPageController mainPageController = new MainPageController();
        mainPageController.start();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}

