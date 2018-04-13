package com.codecool.networking.modes;

import com.codecool.networking.data.Message;
import com.codecool.networking.threads.PrintIncomingMessages;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private String userName;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private BufferedReader bufferedReader;
    private boolean isRunning = true;

    public Client(String serverName, int port, String userName) {

        try {
            this.socket = new Socket(serverName, port);
            this.userName = userName;
            setConnection();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void start(){
        try{
            System.out.println("Connected to " + this.socket.getRemoteSocketAddress().toString() + "!\n");
            System.out.println("Type your message: ");

            String message;
            new PrintIncomingMessages(inStream);
            while(isRunning){
                message = bufferedReader.readLine();
                if(!message.equals(".quit!")){
                    sendData(message);
                } else {
                    isRunning = false;
                    close();
                    System.out.println("Bye bye!");
                }
            }
        } catch (IOException ex){
            System.out.println("Connection lost...");
        } finally {
            close();
        }
    }


    private void setConnection() throws IOException {
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void sendData(String textToSend) throws IOException {

        Message message = new Message(textToSend, this.userName);
        outStream.writeObject(message);
    }

    private void close() {

        try {
            socket.close();
            inStream.close();
            outStream.close();
            bufferedReader.close();
        } catch (IOException e) {}

    }
}

