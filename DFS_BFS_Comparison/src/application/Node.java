package application;

import java.awt.Point;
import java.util.ArrayList;


public class Node {

	public double x;
	public double y;
    public int order;
    public boolean visited;
    public ArrayList<Node> connectedNodes;
	
	public Node(double x,double y, int order) {
		
		this.x = x;	
		this.y = y;
		this.order = order;
		visited = false;
		connectedNodes = new ArrayList<Node>(); 
		
	}
	
	public void updateLocation(double x,double y) {
		this.x  = x;
		this.y = y;	
	}
	
	public void connect(Node nodeToConnect) {
		if(!connectedNodes.contains(nodeToConnect))
			connectedNodes.add(nodeToConnect);
		
		connectedNodes.sort((a, b) -> Integer.compare(a.order, b.order));
		
		if (!nodeToConnect.connectedNodes.contains(this)) {
            nodeToConnect.connect(this);
        }
		
	}
	
	public void disconnect(Node nodeToDisconnect) {
	    if (connectedNodes.contains(nodeToDisconnect)) {
	        connectedNodes.remove(nodeToDisconnect);
	        
	        connectedNodes.sort((a, b) -> Integer.compare(a.order, b.order));
	        
	        if (nodeToDisconnect.connectedNodes.contains(this)) {
	            nodeToDisconnect.disconnect(this);
	        }
	    }
	}
	
	public void selfDestruct() {
	    for (Node connected : new ArrayList<>(connectedNodes)) {
	        connected.connectedNodes.remove(this); 
	    }
	    connectedNodes.clear(); 
	}
	

	
}
