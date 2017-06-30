package il.ac.technion.cs.smarthouse.gui.controllers.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * This class represent a house and meant to be used for the house mapping GUI
 * 
 * @author Sharon Kuninin
 * @since 27-01-2017
 */
public class House {
    @Expose private final List<Room> rooms = new ArrayList<>();

    /**
     * @return the list of rooms in the house
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Adds a room to the house
     * 
     * @param ¢
     *            the room to be added
     */
    public void addRoom(final Room ¢) {
        rooms.add(¢);
    }
}
