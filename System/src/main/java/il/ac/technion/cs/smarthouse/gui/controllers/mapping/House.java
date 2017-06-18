package il.ac.technion.cs.smarthouse.gui.controllers.mapping;

import java.util.ArrayList;
import java.util.List;

public class House {
    private final List<Room> rooms = new ArrayList<>();

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(final Room ¢) {
        rooms.add(¢);
    }
}
