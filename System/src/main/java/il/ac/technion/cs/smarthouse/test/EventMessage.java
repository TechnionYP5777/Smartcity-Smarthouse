package il.ac.technion.cs.smarthouse.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EventMessage {
	public static enum PathType {
		COMPLETE, ONLY_CHILDREN, ALL_SUB_TREE, NONE
	}
	
	public static final String DELIMITER = ".";
	public static final String WILD_CHAR = "*";
	public static final String WILD_PATH_CHAR = "**";
	
	//------------------------------------

	private String data;
	private String path;
	
	public EventMessage(String data, String... pathNodes) {
		this.data = data;
		this.path = String.join(DELIMITER, pathNodes);
	}
	
	public String getPathAsString() {
		return path;
	}
	
	public List<String> getPathAsList() {
		return Arrays.asList(path.split(DELIMITER));
	}
	
	public String getData() {
		return data;
	}
	
	public PathType getPathType() {
		List<String> p = getPathAsList();
		if (p.isEmpty() || !p.stream().allMatch(EventMessage::isNodeLegal))
			return PathType.NONE;
		
		String lastStr = p.get(p.size() - 1);
		
		if (lastStr.equals(WILD_CHAR))
			return PathType.ONLY_CHILDREN;
	}
	
	private static boolean isNodeLegal(String node) {
		return node.matches("\\w+"); //[a-zA-Z_0-9]+
	}
	
	@Override
	public String toString() {
		return "[" + path + " = " + data + "]";
	}
}
