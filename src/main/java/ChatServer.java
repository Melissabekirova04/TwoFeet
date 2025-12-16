package main.java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ChatServer {
    //Lavet ved hjælp af: https://www.youtube.com/watch?v=7tznk2SWCZs
    private static byte[] incoming = new byte[256];

    private static final int PORT = 8000;

    private static DatagramSocket socket;

    static{
        try{
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Integer> users = new ArrayList<>();

    private static final InetAddress address;

    static {
        try{
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String [] args){
        while(true){
            DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
            try{
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server recieved " + message);

            //Til at initialisere brugere.
            if(message.contains("init;")){
                users.add(packet.getPort());


            }else{
                int userPort = packet.getPort(); //Modtage porten fra packeten
                byte[] byteMessage = message.getBytes(); //Konverter Strengen til bytes igen

                //Send bytes til alle andre brugere end afsenderen
                for(int forward_port : users){
                    if(forward_port != userPort){
                        DatagramPacket forward = new DatagramPacket(byteMessage, byteMessage.length, address, forward_port);
                        try{
                            socket.send(forward);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }


        }



    }

}
