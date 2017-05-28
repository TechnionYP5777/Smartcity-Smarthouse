package il.ac.technion.cs.smarthouse.simulator.model;

import il.ac.technion.cs.smarthouse.simulator.view.SensorLabel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SensorData {
    static int id_counter;
    private String name;
    private ObservableList<SensorField> fields = FXCollections.observableArrayList();
    private SensorLabel label;
    private Location location;
    private int myId;

    public SensorData(String name, SensorLabel label, Location location) {
        this.myId = id_counter++;
        this.name = name;
        this.label = label;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<SensorField> getFields() {
        return fields;
    }

    public void addField(SensorField ¢) {
        if (!fields.contains(¢))
            this.fields.add(¢);
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
