package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashSet;

public class TwoFeetApp extends Application {

    private static HashSet<User> users = new HashSet<>();
    private HelpCategory initialCategory;
    private boolean skipCategoryPage;

    public TwoFeetApp() {
        // standard: vis kategorilisten først
    }

    public TwoFeetApp(HelpCategory initialCategory, boolean skipCategoryPage) {
        this.initialCategory = initialCategory;
        this.skipCategoryPage = skipCategoryPage;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            if (skipCategoryPage && initialCategory != null) {
                // Start direkte i topics-siden
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TopicsPage.fxml"));
                Parent root = loader.load();

                TopicsPageController controller = loader.getController();
                controller.setCategory(initialCategory);

                primaryStage.setTitle("TwoFeet – " + getCategoryName(initialCategory));
                primaryStage.setScene(new Scene(root));
            } else {
                // Start med kategori-siden
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/HelpPage.fxml"));
                Parent root = loader.load();

                primaryStage.setTitle("TwoFeet – udflytningshjælper");
                primaryStage.setScene(new Scene(root));
            }

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCategoryName(HelpCategory c) {
        return switch (c) {
            case LAUNDRY -> "Tøjvask";
            case ELECTRONICS -> "Elektronik (køl/frys)";
            case MOVE_IN -> "Inden indflytning";
            case STARTERPACK -> "Starterpack";
            case CLEANING -> "Rengøring";
        };
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static HashSet<User> getUsers() {
        return users;
    }

    public static void main(String[] args) {
        launch(args);
    }
}