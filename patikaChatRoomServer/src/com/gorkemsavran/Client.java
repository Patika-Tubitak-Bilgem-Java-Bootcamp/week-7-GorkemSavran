package com.gorkemsavran;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

class Client extends Thread {

    private final UUID id;
    private String username;
    private boolean isClientInARoom;
    private String roomName;

    private final DataInputStream input;
    private final DataOutputStream output;
    private final Socket socket;
    private final RoomService roomService;

    public Client(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
        this.id = UUID.randomUUID();
        this.isClientInARoom = false;
        this.socket = socket;
        this.input = inputStream;
        this.output = outputStream;
        this.roomService = RoomService.getInstance();
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                if (isClientInARoom) {
                    handleChatting();
                } else {
                    handleRequest();
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    this.input.close();
                    this.output.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    void handleRequest() throws IOException {
        String request;

        output.writeUTF("Please select: ('q' for exit) ");
        output.writeUTF("1) Enter a room: ");
        output.writeUTF("2) Create a room: ");

        request = input.readUTF();

        switch (request) {
            case "q":
                this.socket.close();
                System.out.println("Connection closed");
                break;
            case "1":
                handleEnterRoom();
                break;
            case "2":
                handleCreateRoom();
                break;
            default:
                output.writeUTF("Please enter a valid input");
                break;
        }
    }

    private void handleCreateRoom() throws IOException {
        String request;

        output.writeUTF("Enter your username: ");
        username = input.readUTF();

        output.writeUTF("Enter your room's name: ('q' for exit) ");
        request = input.readUTF();

        if (request.equals("q")) {
            this.socket.close();
            System.out.println("Connection closed");
        } else {
            boolean isRoomCreated = roomService.createRoom(request);
            roomService.enterRoom(this, request);
            if (isRoomCreated) {
                output.writeUTF("Room is created");
                roomName = request;
            } else {
                output.writeUTF("Couldn't create room please try again.");
            }
            isClientInARoom = isRoomCreated;
        }
    }

    private void handleEnterRoom() throws IOException {
        if (roomService.getAllRoomNames().isEmpty()) {
            output.writeUTF("There is no available rooms. Please create one. ");
            return;
        }

        String request;

        output.writeUTF("Select a room: ('q' for exit) ");
        for (final String roomName : roomService.getAllRoomNames()) {
            output.writeUTF(roomName);
        }

        request = input.readUTF();

        if (request.equals("q")) {
            this.socket.close();
            System.out.println("Connection closed");
        } else {
            output.writeUTF("Enter your username: ");
            username = input.readUTF();
            boolean isEnteredRoom = roomService.enterRoom(this, request);
            if (isEnteredRoom) {
                roomName = request;
                output.writeUTF("You entered the room with name " + roomName);
            } else {
                output.writeUTF("Couldn't entered the room, please try again.");
            }
            isClientInARoom = isEnteredRoom;
        }
    }

    private void handleChatting() throws IOException {
        String message;

        message = input.readUTF();

        if (message.equals("q")) {
            this.socket.close();
            System.out.println("Connection closed");
        } else {
            roomService.sendMessage(this, roomName, message);
        }
    }

    void sendToClient(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
