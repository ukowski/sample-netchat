package com.codecool.networking.threads;

import com.codecool.networking.data.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class PrintIncomingMessages implements Runnable {

    private Thread thread;
    private ObjectInputStream inStream;
    private boolean isRunning = true;

    public PrintIncomingMessages(ObjectInputStream inStream) {

        this.inStream = inStream;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {

        while (isRunning){
            try {
                getAndPrintMessage();
            } catch (IOException | ClassNotFoundException e) {
                isRunning = false;
            }
        }
    }

    private void getAndPrintMessage() throws IOException , ClassNotFoundException {
        Message message = (Message) inStream.readObject();
        if(!message.getContent().equals(".quit!")){
            System.out.println(message.getAuthor() + "> " + message.getContent());
        } else {
            isRunning = false;
        }
    }
}
