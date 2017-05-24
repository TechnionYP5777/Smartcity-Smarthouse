package il.ac.technion.cs.smarthouse.simulator.model;

import java.util.HashMap;
import java.util.Map;
import il.ac.technion.cs.smarthouse.simulator.view.SensorLabel;

public class SensorData {
    static int id_counter;
    private String name;
    private Map<String, Types> fields;
    private SensorLabel label;
    private Location location;
    private int myId;

    public SensorData(String name, SensorLabel label, Location location) {
        this.myId = id_counter++;
        this.name = name;
        this.fields = new HashMap<>();
        this.label = label;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Types> getFields() {
        return fields;
    }

    public void addField(String fieldName, Types t) {
        this.fields.put(fieldName, t);
    }

    public Location getLocation() {
        return location;
    }

    public SensorLabel getLabel() {
        return label;
    }

    public void setLabel(SensorLabel ¢) {
        this.label = ¢;
    }

    public void setLocation(Location ¢) {
        this.location = ¢;
    }

    public int getMyId() {
        return myId;
    }

    @Override
    public int hashCode() {
        return 31 * (myId + 31 * (((location == null) ? 0 : location.hashCode())
                        + 31 * (((label == null) ? 0 : label.hashCode())
                                        + 31 * (((fields == null) ? 0 : fields.hashCode()) + 31))))
                        + ((name == null) ? 0 : name.hashCode());
    }

    @Override
    public boolean equals(Object ¢) {
        return ¢ instanceof SensorData && this.myId == ((SensorData) ¢).getMyId();
    }
}
