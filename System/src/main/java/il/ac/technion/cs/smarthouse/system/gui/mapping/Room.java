package il.ac.technion.cs.smarthouse.system.gui.mapping;

import il.ac.technion.cs.smarthouse.system.SensorLocation;

public class Room {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final SensorLocation location;

    public Room(final int x, final int y, final int width, final int height, final SensorLocation location) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.location = location;
    }
}
