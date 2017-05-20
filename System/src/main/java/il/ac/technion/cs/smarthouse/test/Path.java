package il.ac.technion.cs.smarthouse.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Path {
	public static enum PathType {
		COMPLETE, ONLY_CHILDREN, ALL_SUB_TREE, NONE
		//TODO: Ron, change the names
	}
	
	public static final String DELIMITER = ".";
	public static final String WILD_CHAR = "*";
	public static final String WILD_PATH_CHAR = "**";
	
	private List<String> pathNodes;
	private PathType pathType;
	
	public Path(String... pathNodes) {
		this.pathNodes = Arrays.asList(String.join(DELIMITER, pathNodes).split(DELIMITER));
	}
	
	public String getPathAsString() {
		return String.join(DELIMITER, pathNodes);
	}
	
	public List<String> getPathAsList() {
		return Collections.unmodifiableList(pathNodes);
	}
	
	public PathType getPathType() {
		if (pathType != null)
			return pathType;
		
		if (pathNodes.isEmpty())
			return pathType = PathType.NONE;
		
		int concretePartSize = pathNodes.size();
		PathType returnType = PathType.COMPLETE;
		
		if (pathNodes.size() > 1) {
			String lastStr = pathNodes.get(pathNodes.size() - 1);
			
			if (lastStr.equals(WILD_CHAR)) {
				--concretePartSize;
				returnType = PathType.ONLY_CHILDREN;
			} else if (lastStr.equals(WILD_PATH_CHAR)) {
				--concretePartSize;
				returnType = PathType.ALL_SUB_TREE;
			}
		}
		
		return pathType = pathNodes.subList(0, concretePartSize).stream().allMatch(v -> v.matches("\\w+")) ? returnType : PathType.NONE; //[a-zA-Z_0-9]+
	}
	
	public static boolean match(Path completePath, Path template) {
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + getPathAsString() + "]";
	}
}
