package main.java;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientThread extends Thread{

    private DatagramSocket socket;

    private byte[] incoming = new byte[256];

    private TextArea textArea;

    public ClientThread(DatagramSocket socket, TextArea textArea){
        this.socket = socket;
        this.textArea = textArea;
    }

    @Override
    public void run(){
        while(true){
            DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
            try{
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String message = new String(packet.getData(), 0, packet.getLength()) + "\n";
            String current = textArea.getText();
            textArea.setText(current+message);
        }

    }
}
