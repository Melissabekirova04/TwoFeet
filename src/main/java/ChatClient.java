package main.java;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class ChatClient extends Application {

    private static final DatagramSocket socket;
    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InetAddress address;
    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String identifier = "Fader";
    private static final int SERVER_PORT = 8000;

    @FXML private TextArea messageArea;
    @FXML private TextField inputBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatClient.class.getResource("/chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 399, 844);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void initialize() {
        ClientThread clientThread = new ClientThread(socket, messageArea);
        clientThread.start();

        byte[] uuid = ("init;" + identifier).getBytes();
        DatagramPacket initizalize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        try {
            socket.send(initizalize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Form for action event
        inputBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                //Tekstformatet til hvordan det ser ud
                String temp = identifier + ": " + inputBox.getText();
                messageArea.appendText(inputBox.getText() + "\n");
                byte[] msg = temp.getBytes();
                inputBox.clear();

                DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                try {
                    socket.send(send);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
