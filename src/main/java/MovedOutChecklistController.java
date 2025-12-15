package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MovedOutChecklistController {

    private static final String BEFORE_MOVING_OUT_TEXT =
            "Før du flytter ud, er det en god idé at have styr på:\n" +
                    "1) Lejekontrakt: Læs den igennem (opsigelse, husorden, ind-/fraflytningssyn).\n" +
                    "2) Forsikringer: Indboforsikring, evt. ulykkes- og ansvarsforsikring.\n" +
                    "3) El og varme: Skal du selv vælge elselskab og aflæse målere?\n" +
                    "4) Internet/TV: Bestil i god tid / flyt abonnement.\n" +
                    "5) Adresseændring: Meld flytning digitalt.\n" +
                    "6) Boligforening/udlejer: Tilmeld dig app, mail eller beboer-portal, så du får beskeder og regler.\n" +
                    "7) Økonomi: Lav et simpelt budget for husleje, el, internet, mad, transport og lidt opsparing.";

    private static final String STARTER_PACK_TEXT =
            "Forslag til en udflytnings-starterpack (basis til hjemmet):\n" +
                    "KØKKEN:\n" +
                    "- Gryde, pande, bradepande\n" +
                    "- 2–4 tallerkener, glas, kopper, bestik\n" +
                    "- Skærebræt, kniv, grydeskeer, piskeris\n" +
                    "- Si, opbevaringsbokse, viskestykker\n" +
                    "- Opvaskemiddel, opvaskebørste, karklude, affaldsposer\n" +
                    "\nRENGØRING:\n" +
                    "- Støvsuger eller kost/fejebakke\n" +
                    "- Gulvmoppe + spand\n" +
                    "- Universalrengøring, toiletrens, glasrens\n" +
                    "- Mikrofiberklude, gummihandsker\n" +
                    "\nBAD/VASK:\n" +
                    "- Håndklæder, vaskeklude\n" +
                    "- Vaskemiddel, tøjkurv, tørrestativ\n" +
                    "\nANDRE BASISTING:\n" +
                    "- Seng/sovesofa, dyne, pude, sengetøj\n" +
                    "- Forlængerledninger, opladere, lamper\n" +
                    "- Lille værktøjssæt (skruetrækker, hammer, søm/skruer)\n" +
                    "- Førstehjælps-ting: plaster, smertestillende, desinfektion.";

    @FXML
    private void onBeforeMovingOut(ActionEvent event) {
        openDetail("Before moving out", BEFORE_MOVING_OUT_TEXT, event);
    }

    @FXML
    private void onStarterPack(ActionEvent event) {
        openDetail("Starter pack", STARTER_PACK_TEXT, event);
    }

    @FXML
    private void onBackToMain(ActionEvent event) {
        openWindow("/mainpage.fxml", 399, 844);
        closeCurrent(event);
    }

    private void openDetail(String title, String text, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/moved_out_detail.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), 399, 844));

            MovedOutDetailController controller = loader.getController();
            controller.setData(title, text);

            stage.show();
            closeCurrent(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWindow(String fxml, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load(), w, h));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrent(ActionEvent event) {
        Stage current = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        current.close();
    }
}
