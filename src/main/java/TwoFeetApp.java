package main.java;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwoFeetApp extends Application {

    private static HashSet<User> users = new HashSet<>();
    private HelpService helpService = new HelpService();
    private Stage primaryStage;

    private HelpCategory selectedCategory;
    private HelpTopic selectedTopic;

    // NYT: mulighed for at starte direkte i en kategori
    private HelpCategory initialCategory;
    private boolean skipCategoryPage;

    public TwoFeetApp() {
        // standard: vis kategorilisten først
    }

    // NYT: den constructor du kalder fra MoveInEssentialsController
    public TwoFeetApp(HelpCategory initialCategory, boolean skipCategoryPage) {
        this.initialCategory = initialCategory;
        this.skipCategoryPage = skipCategoryPage;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        if (skipCategoryPage && initialCategory != null) {
            // Start direkte i en bestemt kategori
            selectedCategory = initialCategory;
            primaryStage.setTitle("TwoFeet – " + getCategoryName(selectedCategory));
            showTopicsPage();
        } else {
            primaryStage.setTitle("TwoFeet – udflytningshjælper");
            showCategoryPage();
        }

        primaryStage.show();
    }

    // ============== SIDE 1: VÆLG KATEGORI ==============
    private void showCategoryPage() {
        selectedCategory = null;

        Label title = new Label("Vælg en kategori");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        for (HelpCategory c : HelpCategory.values()) {
            listView.getItems().add(getCategoryName(c));
        }

        Button btnNext = new Button("Frem →");
        btnNext.setDisable(true);

        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            if (newV.intValue() >= 0) {
                selectedCategory = HelpCategory.values()[newV.intValue()];
                btnNext.setDisable(false);
            }
        });

        btnNext.setOnAction(e -> showTopicsPage());

        VBox root = new VBox(15, title, listView, btnNext);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);

        primaryStage.setScene(new Scene(root, 500, 400));
    }

    // ============== SIDE 2: VÆLG EMNE ==============
    private void showTopicsPage() {
        selectedTopic = null;

        Label title = new Label("Kategori: " + getCategoryName(selectedCategory));
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        List<HelpTopic> topics = helpService.getTopicsByCategory(selectedCategory);
        ListView<String> listView = new ListView<>();
        for (HelpTopic t : topics) {
            listView.getItems().add(t.getTitle());
        }

        Button btnBack = new Button("← Tilbage");
        Button btnNext = new Button("Frem →");
        btnNext.setDisable(true);

        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            if (newV.intValue() >= 0) {
                selectedTopic = topics.get(newV.intValue());
                btnNext.setDisable(false);
            }
        });

        btnBack.setOnAction(e -> showCategoryPage());
        btnNext.setOnAction(e -> showTopicDetailPage());

        HBox buttons = new HBox(10, btnBack, btnNext);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, title, listView, buttons);
        root.setPadding(new Insets(15));

        primaryStage.setScene(new Scene(root, 600, 450));
    }

    // ============== SIDE 3: SVAR ==============
    private void showTopicDetailPage() {
        Label title = new Label(selectedTopic.getTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextArea answerArea = new TextArea(selectedTopic.getAnswerText());
        answerArea.setEditable(false);
        answerArea.setWrapText(true);

        Button btnBackToTopics = new Button("← Tilbage");
        Button btnBackToStart = new Button("⟲ Til start");

        btnBackToTopics.setOnAction(e -> showTopicsPage());
        btnBackToStart.setOnAction(e -> showCategoryPage());

        HBox buttonRow = new HBox(10, btnBackToTopics, btnBackToStart);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, title, new Label("Svar:"), answerArea, buttonRow);
        root.setPadding(new Insets(15));

        primaryStage.setScene(new Scene(root, 700, 500));
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
