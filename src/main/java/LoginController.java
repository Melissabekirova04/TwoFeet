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
import main.java.util.CloseProgram;
import main.java.util.UserChecker;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    DBConnector dbConnector = new DBConnector();
    String url1 = "jdbc:sqlite:identifier.sqlite";
    Connection connection;
    private DbManager dbManager;

    public static int currentUserId = -1;



    @FXML
    private Button goToRegisterButton;
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
    @FXML
    private Button loginCloseButton;




    public void start(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 399, 844);
            loginStage.setScene(scene);
            loginStage.show();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));


        dbConnector.connect(url1);
        connection = dbConnector.getConnection();
        dbManager = new DbManager();

        // STYLING

        File brandingFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Logo1.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        URL brandingUrl = getClass().getResource("/Logo1.png");
        if (brandingUrl != null) {
            brandingImageView.setImage(new Image(brandingUrl.toExternalForm()));
        }

        URL lockUrl = getClass().getResource("/Login.png");
        if (lockUrl != null) {
            lockImageView.setImage(new Image(lockUrl.toExternalForm()));
        }
    }

    @FXML
    public void LoginOnAction() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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


    public void goToRegisterButtonOnAction(ActionEvent event){
        RegisterController registerController = new RegisterController();
        registerController.start();
        Stage stage = (Stage) goToRegisterButton.getScene().getWindow();
        stage.close();
    }

    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }


    public void validateLogin() throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        String passwordHash = hashPassword(password);

        int userId = dbManager.loginUser(username, passwordHash);

        if (userId != -1) {
            currentUserId = userId; // REMEMBER USER
            loginMessageLabel.setText("Success!");
            MainPageController mainPageController = new MainPageController();
            mainPageController.start();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }else{
            loginMessageLabel.setText("Wrong Username or Password");
        }



    }

    public void loginCloseButtonOnAction(){
        Stage stage = (Stage) loginButton.getScene().getWindow();
        CloseProgram.close(stage);
    }

}
