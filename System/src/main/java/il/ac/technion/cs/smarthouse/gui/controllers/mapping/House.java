package il.ac.technion.cs.smarthouse.gui.controllers.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class House {
    @Expose private final List<Room> rooms = new ArrayList<>();

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(final Room ¢) {
        rooms.add(¢);
    }
}
