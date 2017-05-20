package il.ac.technion.cs.smarthouse.test;

public class PathHandler {
	public static enum PathType {
		COMPLETE, ONLY_CHILDREN, ALL_SUB_TREE, NONE
	}
	
	static final String DELIMITER = "@";
	static final String WILD_CHAR = "*";
	static final String WILD_PATH_CHAR = "**";
	
	public static String buildPath(String... nodes) {
		return String.join(DELIMITER, nodes);
	}
	
	public static PathType getPathType(String path) {
		
	}
}
