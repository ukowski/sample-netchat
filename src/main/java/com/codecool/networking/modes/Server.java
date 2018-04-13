package com.codecool.networking.modes;

import com.codecool.networking.data.Message;
import com.codecool.networking.threads.PrintIncomingMessages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket = null;
    private Socket clientSocket;
    private String userName;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private BufferedReader bufferedReader;
    private boolean isRunning = true;
    private boolean isConnected = false;


    public Server(int port, String userName) {

        try {
            this.serverSocket = new ServerSocket(port);
            this.userName = userName;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start(){

        try{
            while (isRunning) {
                System.out.println("Waiting for a client on port " + serverSocket.getLocalPort() + "...");
                clientSocket = serverSocket.accept();
                setConnection();
                System.out.println("Client connected! Chat starts... \n");
                System.out.println("Type your message:");

                String message = "";

                isConnected = true;
                new PrintIncomingMessages(inStream);
                try{
                    while (isConnected) {
                        message = bufferedReader.readLine();
                        if (!message.equals(".quit!")) {
                            sendData(message);
                        } else {
                            isConnected = false;
                            isRunning = false;
                            closeConnection();
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Client has disconnected!");
                    isConnected = false;
                }
            }
        } catch (IOException ex) {
            isConnected = false;
        }

    }


    private void setConnection() throws IOException {
        inStream = new ObjectInputStream(clientSocket.getInputStream());
        outStream = new ObjectOutputStream(clientSocket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void sendData(String textToSend) throws IOException {

        Message message = new Message(textToSend, userName);
        outStream.writeObject(message);
    }

    private void closeConnection(){

        try {
            inStream.close();
            outStream.flush();
            outStream.close();
            bufferedReader.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected(){
        return clientSocket.isClosed();
    }
}
