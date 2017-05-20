package il.ac.technion.cs.smarthouse.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesTree {
	public static class MessageNode {
		private static int idCounter;
		
		private int id = idCounter++;
		private String myPath;
		private MessageNode parent;
		private List<EventHandler> eventHandlers_ForMe = new ArrayList<>();
		private List<EventHandler> eventHandlers_ForChildren = new ArrayList<>();
		private List<EventHandler> eventHandlers_ForDescendants = new ArrayList<>();
		private Map<String, MessageNode> children = new HashMap<>();
		private String data;
		
		
		
	}
	
	private MessageNode root = new MessageNode();
	
	public void insertMessageNode(MessageNode m) {
		
	}
	
	public MessageNode get(String path) {
		return null;
	}
}
