package il.ac.technion.cs.smarthouse.simulator.model;

import il.ac.technion.cs.smarthouse.simulator.view.SensorLabel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SensorData {
    static int id_counter;
    private String name;
    private final ObservableList<SensorField> fields = FXCollections.observableArrayList();
    private SensorLabel label;
    private Location location;
    private final int myId;

    public SensorData(final String name, final SensorLabel label, final Location location) {
        myId = id_counter++;
        this.name = name;
        this.label = label;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ObservableList<SensorField> getFields() {
        return fields;
    }

    public void addField(final SensorField ¢) {
        if (!fields.contains(¢))
            fields.add(¢);
    }

    public Location getLocation() {
        return location;
    }

    public SensorLabel getLabel() {
        return label;
    }

    public void setLabel(final SensorLabel ¢) {
        label = ¢;
    }

    public void setLocation(final Location ¢) {
        location = ¢;
    }

    public int getMyId() {
        return myId;
    }

    @Override
    public int hashCode() {
        return 31 * (myId + 31
                        * ((location == null ? 0 : location.hashCode()) + 31 * ((label == null ? 0 : label.hashCode())
                                        + 31 * ((fields == null ? 0 : fields.hashCode()) + 31))))
                        + (name == null ? 0 : name.hashCode());
    }

    @Override
    public boolean equals(final Object ¢) {
        return ¢ instanceof SensorData && myId == ((SensorData) ¢).getMyId();
    }
}
