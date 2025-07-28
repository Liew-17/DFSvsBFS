package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ReportController {
	
	@FXML
	private Pane DFSpane;
	
	@FXML
	private ScrollPane DFSscrollPane;
	
	@FXML
	private Label DFStimeLabel;

	@FXML
	private Label DFSpathLabel;

	@FXML
	private Label DFSlengthLabel;

	@FXML
	private Label DFSvisitedLabel;
	
	@FXML
	private Label DFSdeepestLabel;
	
	@FXML
	private Pane BFSpane;
	
	@FXML
	private ScrollPane BFSscrollPane;
	
	@FXML
	private Label BFStimeLabel;

	@FXML
	private Label BFSpathLabel;

	@FXML
	private Label BFSlengthLabel;

	@FXML
	private Label BFSvisitedLabel;
	
	@FXML
	private Label BFSmaxLabel;
	
	public enum ReportType {
		DFS,
		BFS
	}
	
	public void initialize() {
	
		BFSpane.setPrefHeight(1000);
		BFSpane.setPrefWidth(1000);
		DFSpane.setPrefHeight(1000);
		DFSpane.setPrefWidth(1000);
	
		Platform.runLater(() -> {
			BFSscrollPane.setHvalue(0.5);
			BFSscrollPane.setVvalue(0.5);
			DFSscrollPane.setHvalue(0.5);
			DFSscrollPane.setVvalue(0.5);
        });
	}
	
	public void generateReport(Node start, Node goal,List<Node> allNodes,List<Node> path, ReportType reportType, long duration){
		
		Map<Node, Circle> nodeCircleMap = new HashMap<>();
		
		List<Edge> edges = new ArrayList<Edge>();
		
		//first loop to create circle
		for(Node node: allNodes) {
			
			Circle circle = new Circle(node.x,node.y,20);	
			circle.setStroke(Color.BLACK);
			circle.setFill(Color.LIGHTGRAY); 
			nodeCircleMap.put(node, circle); // The node - circle map will be used to draw line
					
			if(node.visited)
				circle.setFill(Color.LIGHTYELLOW);
			
			if(path.contains(node))
				circle.setFill(Color.LIGHTBLUE);
						
			if(node == start)
				circle.setFill(Color.LIGHTGREEN);
			
			if(node == goal)
				circle.setFill(Color.PINK);
			
			Label label = new Label(Integer.toString(node.order));
			Platform.runLater(() -> {
				label.setLayoutX(node.x - label.getWidth()/2);
		        label.setLayoutY(node.y - label.getHeight()/2);
			});
	        
			
			if(reportType == ReportType.BFS) {
				BFSpane.getChildren().add(circle);	
				BFSpane.getChildren().add(label);
			}							
			else {
				DFSpane.getChildren().add(circle);	
				DFSpane.getChildren().add(label);
			} 										
		}
		
		//sec loop to create edge
		for(Node node: allNodes) {		
			
			Circle circleA = nodeCircleMap.get(node);
			
			for(Node neighbour:node.connectedNodes) {
				
				Circle circleB = nodeCircleMap.get(neighbour);
				
				if(MainController.searchEdge(circleA, circleB, edges)==null) {
					
					Line line =new Line();
		   			line.setStrokeWidth(2);		
		   				
	   				if(reportType == ReportType.BFS) 
						BFSpane.getChildren().add(0,line);				
					else 
						DFSpane.getChildren().add(0,line); 		
		   			
		   			Edge edge = new Edge(circleA,circleB,line);
					edge.updateLine();
					edges.add(edge);			
				}	
			}			
		}
		
		if(path.isEmpty()|| path==null) {
			return;		
		}
		
		//set label
		double durationMillis = duration / 1_000_000.0; // convert to milliseconds
		
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < path.size(); i++) {
	    	sb.append(path.get(i).order);
	        if (i != path.size() - 1) {
	        	sb.append(" -> ");
	        }
	    }
	    
	    int totalVisited = 0;
	    for(Node node:allNodes) {
	    	if(node.visited)
	    		totalVisited++;
	    }
	    
	    if(reportType == ReportType.BFS) {
	    	BFSpathLabel.setText(sb.toString());
	    	BFSlengthLabel.setText(Integer.toString(path.size()));
	    	BFSvisitedLabel.setText(Integer.toString(totalVisited));
	    	BFStimeLabel.setText(String.format("%.3f ms", durationMillis));
	    	BFSmaxLabel.setText(Integer.toString(BFS_Bot.maxQueueSize));
	    	
	    } else { 
	        DFSpathLabel.setText(sb.toString());
	        DFSlengthLabel.setText(Integer.toString(path.size()));
	        DFSvisitedLabel.setText(Integer.toString(totalVisited));
	        DFStimeLabel.setText(String.format("%.3f ms", durationMillis));
	        DFSdeepestLabel.setText(Integer.toString(DFS_Bot.maxStackSize));
	    }

		
	}
	
	


}
