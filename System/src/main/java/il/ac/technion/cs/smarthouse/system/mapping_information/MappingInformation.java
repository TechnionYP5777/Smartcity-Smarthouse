package il.ac.technion.cs.smarthouse.system.mapping_information;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.gui.controllers.mapping.House;
import il.ac.technion.cs.smarthouse.gui.controllers.mapping.Room;

public class MappingInformation {
    
    private static final int ROOM_IN_ROW = 4;
    private static final int MARGIN = 20;
    private static final int HEIGHT = 150;
    private static final int WIDTH = 160;
    @Expose private List<String> allLocations = new ArrayList<>();
    @Expose private final House house = new House();
    @Expose private int roomNumbers;
    
    public void addRoom(String roomName) {
        allLocations.add(roomName);
        house.addRoom(new Room(MARGIN + (roomNumbers % ROOM_IN_ROW) * WIDTH,
                        MARGIN + (roomNumbers / ROOM_IN_ROW) * HEIGHT, WIDTH, HEIGHT, roomName));
        ++roomNumbers;
    }
    
    public List<String> getAllLocations() {
        return allLocations;
    }
    
    public House getHouse() {
        return house;
    }
    
    public int getRoomNumbers() {
        return roomNumbers;
    }
    
    public static int getWidth() {
        return WIDTH;
    }
    
    public static int getRoomInRow() {
        return ROOM_IN_ROW;
    }
    
    public static int getMargin() {
        return MARGIN;
    }
    
    public static int getHeight() {
        return HEIGHT;
    }
    
    public int calcyPlusRoom() {
        return MARGIN + (roomNumbers / ROOM_IN_ROW) * HEIGHT;
    }
    
    public int calcxPlusRoom() {
        return MARGIN + (roomNumbers % ROOM_IN_ROW) * WIDTH;
    }
    
}
