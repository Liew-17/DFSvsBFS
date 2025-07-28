package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class BFS_Bot {

	public static int maxQueueSize;

	public static List<Node> runBFS(Node start,Node goal,List<Node> allNodes){
		for (Node node : allNodes) {
	        node.visited = false;
	    }
		
		maxQueueSize=0;
		
		Queue<List<Node>> queue = new LinkedList<>();
		
		List<Node> startPath = new ArrayList<>();	
		start.visited = true;
		startPath.add(start);	
		
		queue.add(startPath);
		
		while(!queue.isEmpty()) {
			
			List<Node> currentPath = queue.peek();
						
			Node lastNode = currentPath.get(currentPath.size()-1);
			
			for(Node neighbor: lastNode.connectedNodes) {
				if(!neighbor.visited) {
					List<Node> newPath = new ArrayList<>(currentPath); 	
					neighbor.visited = true;
					newPath.add(neighbor); 
					
					if(neighbor == goal)
						return newPath;
					
					queue.add(newPath);
					maxQueueSize = Math.max(maxQueueSize, queue.size()); //record the maximun amount of path explored at one time
					
				}
				
			}		
			
			//released the node once all neighbor have been search
			queue.poll();
			
		}
		
		
		return new ArrayList<>();	
		
	}
	
}
