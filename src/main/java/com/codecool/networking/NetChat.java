package com.codecool.networking;

import com.codecool.networking.modes.Client;
import com.codecool.networking.modes.Server;

import java.util.Scanner;

public class NetChat {
    public static void main(String[] args) {


        if(args.length == 1){
            String name = getName();
            Server server = new Server(Integer.parseInt(args[0]), name);
            server.start();
        }
        if(args.length == 2){
            String name = getName();
            Client client = new Client(args[0], Integer.parseInt(args[1]), name);
            client.start();
        }

    }

    private static String getName() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("What's your name: ");
        String name = scanner.nextLine();
        return name;
    }
}
