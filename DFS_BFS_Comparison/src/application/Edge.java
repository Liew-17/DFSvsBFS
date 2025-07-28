package application;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Edge {
	public Circle nodeA;
	public Circle nodeB;
	public Line line;
	
	public Edge(Circle nodeA, Circle nodeB, Line line) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.line = line;
		
	}
	
	public void updateLine() {
		line.setStartX(nodeA.getCenterX());
		line.setStartY(nodeA.getCenterY());
		line.setEndX(nodeB.getCenterX());
		line.setEndY(nodeB.getCenterY());
		
	}

}
