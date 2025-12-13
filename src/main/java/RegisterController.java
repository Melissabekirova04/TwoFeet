package main.java;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import main.java.Main;
import main.java.util.CloseProgram;
import main.java.util.UserChecker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button goToLoginButton;
    @FXML
    private Button createUserButton;
    @FXML
    private ImageView shieldImageView;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label registerCheckLabel;
    @FXML
    private Button registerCloseButton;

    private UserChecker userChecker = new UserChecker();


    public void start(){
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 399, 844);
            registerStage.setScene(scene);
            registerStage.show();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File shieldFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Shield.png");
        Image shieldImage = new Image(shieldFile.toURI().toString());
        shieldImageView.setImage(shieldImage);

    }

    public void goToLoginButtonOnAction(ActionEvent event){
        LoginController LoginController = new LoginController();
            LoginController.start();
            Stage stage = (Stage) goToLoginButton.getScene().getWindow();
            stage.close();
    }


    public void createUserButtonOnAction() {
        //Omdan til String objekter
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        //Checker for blanke felter
        if (!userChecker.isFilledOut(firstname, lastname, username, password, confirmPassword)) {
            registerCheckLabel.setText("Please fill in all your information");
        }

        //Checker for at username og password er langt nok
        else if(userChecker.checkLength(username, 5, 12) == false) {
            registerCheckLabel.setText("Username must be between 5 and 12 characters");

        }else if(userChecker.checkLength(password, 8, 18) == false){
            registerCheckLabel.setText("Password must between 8 and 18 characters");
        }

        //Checker om der bliver opfyldt kriterier, regex fundet på: https://www.youtube.com/watch?v=rwp1irWJtTc
        //Skal indholde minumum et lille bogstav
        else if(userChecker.checkForSmallLetters(password) == false ) {
            registerCheckLabel.setText("Password must contain at least 1 small letter");

        //Skal indholde minumum et stort bogstav
        }else if(userChecker.checkForCapitalLetters(password) == false) {
            registerCheckLabel.setText("Password must contain at least 1 capital letter");

        //Skal indholde minimum 1 tal
        }else if(userChecker.checkForNumbers(password) == false){
            registerCheckLabel.setText("Password must contain at least 1 number");

        //Er de to passwords ens
        } else if (!password.equals(confirmPassword)) {
            registerCheckLabel.setText("The passwords doesn't match");
        } else {
            User user = new User(firstname, lastname, username, password);
            TwoFeetApp.addUser(user);
            registerCheckLabel.setText("User made!");

            //Duration fundet fra AI, da Thread.sleep ikke opdatere billedet.
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    MainPageController mainPageController = new MainPageController();
                    mainPageController.start();
                    Stage currentStage = (Stage) createUserButton.getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            pause.play();
        }
    }

    public void registerCloseButtonOnAction(){
        Stage stage = (Stage) registerCloseButton.getScene().getWindow();
        CloseProgram.close(stage);
    }

}
