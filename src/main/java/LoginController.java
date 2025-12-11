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
    String url1 = "jdbc:sqlite:C:\\Users\\morte\\IdeaProjects\\SceneBuilderTest\\identifier.sqlite";
    Connection connection;
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
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        dbConnector.connect(url1);
        connection = dbConnector.getConnection();
        File brandingFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Logo1.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Login.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);


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

    public void validateLogin() throws IOException {
        MainPageController mainPageController = new MainPageController();
        mainPageController.start();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
        /*
        String verifyLogin = "SELECT * FROM user_account WHERE username = '" + usernameTextField.getText() + "' AND password ='" + passwordTextField.getText() + "' ";

        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(verifyLogin);

            while(rs.next()){
                if(rs.getInt(1) == 1){
                    //loginMessageLabel.setText("You successfully logged in!");
                    createAccountForm();
                } else {
                    loginMessageLabel.setText("Invalid username or password");
                }

            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAccountForm() throws IOException {
        MainPageController mainPageController = new MainPageController();
        mainPageController.start();
    }
*/
}
