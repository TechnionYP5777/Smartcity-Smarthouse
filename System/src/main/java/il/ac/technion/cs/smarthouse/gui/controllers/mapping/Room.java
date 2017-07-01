package il.ac.technion.cs.smarthouse.gui.controllers.mapping;

import com.google.gson.annotations.Expose;

/**
 * This class represents a room in the house and meant to be used for the house
 * mapping
 * 
 * @author Roy Shchory
 * @since 20-01-2017
 */
public class Room {
    @Expose public final int x;
    @Expose public final int y;
    @Expose public final int width;
    @Expose public final int height;
    @Expose public String location;

    public Room(final int x, final int y, final int width, final int height, final String location) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.location = location;
    }
}
