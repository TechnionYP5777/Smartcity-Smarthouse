package il.ac.technion.cs.smarthouse.simulator.model;

public class SensorField {
    private final String name;
    private final Types type;

    public SensorField(final String name, final Types type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Types getType() {
        return type;
    }

    @Override
    public boolean equals(final Object ¢) {
        return ¢ instanceof SensorField && name.equals(((SensorField) ¢).getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
