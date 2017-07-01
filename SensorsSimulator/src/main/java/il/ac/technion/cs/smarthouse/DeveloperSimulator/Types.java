package il.ac.technion.cs.smarthouse.DeveloperSimulator;
/**
 * @author Roy Shchory
 * @since Jun 17, 2017
 */
@SuppressWarnings("rawtypes")
public enum Types {
    BOOLEAN("Boolean",Boolean.class),
    INTEGER("Integer",Integer.class),
    DOUBLE("Double",Double.class),
    STRING("String",String.class);
    private final String fieldDescription;
	private final Class Class;
    private Types(final String value,final Class c) {
        fieldDescription = value;
        Class=c;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }
    
    public Class getEClass() {
		return Class;
	}
}
