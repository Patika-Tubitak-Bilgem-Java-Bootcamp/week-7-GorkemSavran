package com.gorkemsavran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomService {

    private static final RoomService INSTANCE = new RoomService();

    private final Map<String, Room> rooms;

    private RoomService() {
        this.rooms = new HashMap<>();
    }

    public static RoomService getInstance() {
        return INSTANCE;
    }

    public List<String> getAllRoomNames() {
        return new ArrayList<>(rooms.keySet());
    }

    public boolean createRoom(final String roomName) {
        if (rooms.containsKey(roomName))
            return false;
        Room room = new Room(roomName);
        rooms.put(roomName, room);
        return true;
    }

    public boolean enterRoom(final Client client, final String roomName) {
        if (!rooms.containsKey(roomName))
            return false;
        Room room = rooms.get(roomName);
        room.addClient(client);
        return true;
    }

    public boolean removeRoom(final String roomName) {
        if (!rooms.containsKey(roomName))
            return false;
        rooms.remove(roomName);
        return true;
    }

    public void sendMessage(final Client client, final String roomName, final String message) {
        if (!rooms.containsKey(roomName))
            return;
        rooms.get(roomName).sendMessage(client, message);
    }
}
