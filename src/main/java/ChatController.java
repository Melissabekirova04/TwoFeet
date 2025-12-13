package main.java;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    public void start(){
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/chat.fxml"));
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
    public void initialize(URL location, ResourceBundle resources) {

    }
}
