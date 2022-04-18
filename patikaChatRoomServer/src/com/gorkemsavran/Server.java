package com.gorkemsavran;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        while (true) {

            try(ServerSocket serverSocket = new ServerSocket(5056)) {

                socket = serverSocket.accept();

                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                Thread t = new Client(socket, input, output);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
