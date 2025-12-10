package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MoveInEssentialsController {

    @FXML
    private Button beforeMoveInButton;

    @FXML
    private Button starterPackButton;

    @FXML
    private Button closeButton;

    @FXML
    private void onBeforeMoveInClicked() {
        // Åbn TwoFeetApp direkte i kategorien "Inden indflytning"
        TwoFeetApp app = new TwoFeetApp(HelpCategory.MOVE_IN, true);
        Stage stage = new Stage();
        app.start(stage);

        // Luk dette lille valg-vindue
        Stage current = (Stage) beforeMoveInButton.getScene().getWindow();
        current.close();
    }

    @FXML
    private void onStarterPackClicked() {
        // Åbn TwoFeetApp direkte i kategorien "Starterpack"
        TwoFeetApp app = new TwoFeetApp(HelpCategory.STARTERPACK, true);
        Stage stage = new Stage();
        app.start(stage);

        // Luk dette lille valg-vindue
        Stage current = (Stage) starterPackButton.getScene().getWindow();
        current.close();
    }

    @FXML
    private void onCloseClicked() {
        Stage current = (Stage) closeButton.getScene().getWindow();
        current.close();
    }
}
