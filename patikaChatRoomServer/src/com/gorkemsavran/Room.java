package com.gorkemsavran;

import java.util.*;

public class Room {

    private final String roomName;
    private final List<Client> clients;

    public Room(final String roomName) {
        this.roomName = roomName;
        this.clients = new ArrayList<>();
    }

    void addClient(final Client client) {
        if (clients.contains(client))
            return;
        clients.add(client);
        for (final Client c : clients) {
            c.sendToClient("(" + roomName + ")" + client.getUsername() + " joined the room.");
        }
    }

    void removeClient(final Client client) {
        if (!clients.contains(client))
            return;
        clients.remove(client);
    }

    void sendMessage(final Client messageSender, final String message) {
        if (!clients.contains(messageSender))
            return;
        for (final Client client : clients) {
            client.sendToClient("(" + roomName + ")" + messageSender.getUsername() + ": " + message);
        }
    }

}
