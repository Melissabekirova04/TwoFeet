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

import java.io.File;
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
    @FXML
    private Button budgetButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File logoFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Logo1.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        File checklistFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Checklist.png");
        Image checklistImage = new Image(checklistFile.toURI().toString());
        checklistImageView.setImage(checklistImage);

        File budgetFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Budget.png");
        Image budgetImage = new Image(budgetFile.toURI().toString());
        budgetImageView.setImage(budgetImage);

        File shoppingListFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\ShoppingList.png");
        Image shoppingListImage = new Image(shoppingListFile.toURI().toString());
        todoImageView.setImage(shoppingListImage);

        File helpServiceFile = new File("C:\\Users\\morte\\IdeaProjects\\TwoFeet\\src\\main\\resources\\Guide.png");
        Image helpServiceImage = new Image(helpServiceFile.toURI().toString());
        helpServiceImageView.setImage(helpServiceImage);
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mainpage.fxml"));
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(fxmlLoader.load(), 399, 844);
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

    public void budgetButtonOnAction(ActionEvent event){
        LoginController loginController = new LoginController();
        loginController.start();
        Stage stage = (Stage) budgetButton.getScene().getWindow();
        stage.close();
    }

}
