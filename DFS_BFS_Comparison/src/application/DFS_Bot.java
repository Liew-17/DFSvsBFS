package application;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

public class DFS_Bot {
	
	public static int maxStackSize;
	
	public static List<Node> runDFS(Node start,Node goal,List<Node> allNodes){
		for (Node node : allNodes) {
	        node.visited = false;
	    }
		
		maxStackSize = 0; 
		
		Stack<Node> stack = new Stack<>();
		
		start.visited = true;
		stack.push(start);
		
	    while (!stack.isEmpty()) {
	    	maxStackSize = Math.max(maxStackSize, stack.size()); //record longest path explored
	    	
	    	Node current = stack.peek();
	    	
	    	if(current == goal)
	    		return stack;
	    	
	    	boolean hasUnvisitedNeighbor = false;
	    	
	    	for (Node neighbor : current.connectedNodes) {
	            if (!neighbor.visited) {
	            	neighbor.visited = true;
	            	stack.push(neighbor);
	            	hasUnvisitedNeighbor = true;
	            	break;
	            }
	            
	        }
			
	    	if(!hasUnvisitedNeighbor)
	    		stack.pop();
	    }
		
		
		
		return new ArrayList<>();	
	}


}
