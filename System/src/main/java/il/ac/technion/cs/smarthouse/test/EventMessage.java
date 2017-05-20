package il.ac.technion.cs.smarthouse.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EventMessage {
	private Path path;
	private String data;
	
	public EventMessage(Path path, String data) {
		this.path = path;
		this.data = data;
	}
	
	public Path getPath() {
		return path;
	}
	
	public String getData() {
		return data;
	}
	
	@Override
	public String toString() {
		return "[" + path + " = " + data + "]";
	}
}
