package il.ac.technion.cs.eldery.system.gui.mapping;

import java.util.ArrayList;
import java.util.List;

public class House {
    private List<Room> rooms = new ArrayList<>();
    
    public List<Room> getRooms() {
        return this.rooms;
    }
    
    public void addRoom(Room ¢) {
        this.rooms.add(¢);
    }
}
