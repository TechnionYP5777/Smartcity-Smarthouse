package il.ac.technion.cs.smarthouse.simulator.model;

public class SensorField {
    private String name;
    private Types type;

    public SensorField(String name, Types type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Types getType() {
        return type;
    }
}
