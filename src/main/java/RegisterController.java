package main.java;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.Main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView shieldImageView;

    public void start(){
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(fxmlLoader.load(), 520, 523);
            registerStage.setScene(scene);
            registerStage.show();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        File shieldFile = new File("C:\\Users\\morte\\IdeaProjects\\SceneBuilderTest\\src\\main\\resources\\com\\example\\scenebuildertest\\Shield.png");
        Image shieldImage = new Image(shieldFile.toURI().toString());
        shieldImageView.setImage(shieldImage);

    }



}
