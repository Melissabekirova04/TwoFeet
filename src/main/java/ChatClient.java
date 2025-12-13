package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.*;

public class ChatClient extends Application {

    private static final DatagramSocket socket;

    static {
        try{
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InetAddress address;

    static {
        try{
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String identifier = "John";

    private static final int SERVER_PORT = 8000;

    private static final TextArea messageArea = new TextArea();

    private static final TextField inputBox = new TextField();

    public static void main(String[] args)throws IOException {
        ClientThread clientThread = new ClientThread(socket, messageArea);
        clientThread.start();

        byte[] uuid = ("init;" + identifier).getBytes();
        DatagramPacket initizalize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        socket.send(initizalize);

        launch();
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        messageArea.setMaxWidth(399);
        messageArea.setEditable(false);

        inputBox.setMaxWidth(399);
        inputBox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                String temp = identifier + ";" + inputBox.getText();
                messageArea.setText(messageArea.getText() + inputBox.getText() + "\n");
                byte[] msg = temp.getBytes();
                inputBox.setText("");

                //Create packet and send
                DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                try{
                    socket.send(send);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Scene scene = new Scene(new VBox(35, messageArea, inputBox), 399, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
