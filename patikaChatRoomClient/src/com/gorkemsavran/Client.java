package com.gorkemsavran;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket(ip, 5056);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        try {

            while (true) {
                Thread receivingMessages = new Thread(() -> {
                    String received;
                    try {
                        while (true) {
                            Thread.sleep(1000);
                            while (input.available() > 0) {
                                received = input.readUTF();
                                System.out.println(received);
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Connection closed");
                    }
                });
                receivingMessages.start();
                String toSend = scanner.nextLine();

                output.writeUTF(toSend);
                if (toSend.equals("q")) {
                    System.out.println("Closing this connection : " + socket);
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                System.out.println(input.readUTF());
            }
        } catch (Exception e) {
            System.out.println("Connection closed");
        }
    }
}
