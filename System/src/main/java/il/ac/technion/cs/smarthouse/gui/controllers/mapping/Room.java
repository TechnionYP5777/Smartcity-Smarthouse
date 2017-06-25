package il.ac.technion.cs.smarthouse.gui.controllers.mapping;

import com.google.gson.annotations.Expose;

public class Room {
    @Expose public final int x;
    @Expose public final int y;
    @Expose public final int width;
    @Expose public final int height;
    @Expose public final String location;

    public Room(final int x, final int y, final int width, final int height, final String location) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.location = location;
    }
}
