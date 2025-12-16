package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.util.CloseProgram;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final DBConnector dbConnector = new DBConnector();
    // ⚠️ Jeg lader din sti stå, men du bør helst bruge "jdbc:sqlite:identifier.sqlite"
    private final String url1 = "jdbc:sqlite:C:/intellij/TwoFeet/identifier.sqlite";

    private Connection connection;
    private DbManager dbManager;

    // Du kan beholde den, men vi bruger Session som "sandheden"
    public static int currentUserId = -1;

    @FXML private Button goToRegisterButton;
    @FXML private Label loginMessageLabel;
    @FXML private ImageView brandingImageView;
    @FXML private ImageView lockImageView;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button loginCloseButton;

    public void start() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 399, 844);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        dbConnector.connect(url1);
        connection = dbConnector.getConnection();

        dbManager = new DbManager();
        dbManager.connect(); // ✅ sikrer DbManager er connected

        // STYLING (jeg beholder dit setup)
        File brandingFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Logo1.png");
        if (brandingFile.exists()) {
            Image brandingImage = new Image(brandingFile.toURI().toString());
            brandingImageView.setImage(brandingImage);
        }

        URL brandingUrl = getClass().getResource("/Logo1.png");
        if (brandingUrl != null) {
            brandingImageView.setImage(new Image(brandingUrl.toExternalForm()));
        }

        URL lockUrl = getClass().getResource("/Login.png");
        if (lockUrl != null) {
            lockImageView.setImage(new Image(lockUrl.toExternalForm()));
        }
    }


    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            try {
                validateLogin();
            } catch (IOException e) {
                e.printStackTrace();
                loginMessageLabel.setText("Login error");
            }
        } else {
            loginMessageLabel.setText("Please enter username and password");
        }
    }

    public void goToRegisterButtonOnAction(ActionEvent event) {
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

        String passwordHash = password;//hashPassword(password);

        int userId = dbManager.loginUser(username, passwordHash);

        if (userId != -1) {
            Session.setUserId(userId);
            currentUserId = userId;

            loginMessageLabel.setText("Success!");
            openMainPageInSameWindow();
        } else {
            loginMessageLabel.setText("Wrong Username or Password");
        }
    }


    private void openMainPageInSameWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
            Scene scene = new Scene(loader.load(), 399, 844);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Could not open main page");
        }
    }

    public void loginCloseButtonOnAction() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        CloseProgram.close(stage);
    }
}
