package il.ac.technion.cs.smarthouse.simulator.model;

public enum Types {
    BOOLEAN("Boolean"),
    INTEGER("Integer"),
    DOUBLE("Double"),
    STRING("String");
    private final String fieldDescription;

    private Types(final String value) {
        fieldDescription = value;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }
}
