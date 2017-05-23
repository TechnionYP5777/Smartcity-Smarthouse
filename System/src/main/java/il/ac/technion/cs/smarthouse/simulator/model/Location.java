package il.ac.technion.cs.smarthouse.simulator.model;

public enum Location {
    BEDROOM("Bedroom"),
    BATHROOM("Bathroom"),
    LIVINGROOM("Livingroom"),
    KITCHEN("Kitchen");
    private final String fieldDescription;

    private Location(String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }
}
