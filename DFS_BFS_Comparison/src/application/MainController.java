package application;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainController {
	
	int i = 1;
	
	@FXML
	private Pane mainPane;
	
	@FXML
	private ScrollPane scrollPane;
	
	private Circle selectedCircle = null;
	
	public Map<Circle, Label> circleLabelMap;
	
	public Map<Circle, Node> circleNodeMap;
	
	public ArrayList<Edge> edges;
	
	public Circle startCircle;
	
	public Circle goalCircle;
	
	@FXML
	public void initialize() {
		edges = new ArrayList<Edge>() ;
		circleLabelMap = new HashMap<>();
		circleNodeMap = new HashMap<>();
		mainPane.setPrefHeight(1000);
		mainPane.setPrefWidth(1000);
		//move to center
		Platform.runLater(() -> {
            scrollPane.setHvalue(0.5);
            scrollPane.setVvalue(0.5);
        });
	}
	
	@FXML
	public void addNode() {
			removeSelection();
			double angle = Math.random() * 2 * Math.PI;
			double distance = Math.random() * 100;
			double x = mainPane.getWidth()/2 + Math.cos(angle) * distance;
			double y = mainPane.getHeight()/2 + Math.sin(angle) * distance;
		 	
			Node node = new Node(x,y,i++);
					
			//draw circle
		 	Circle circle = new Circle(x,y,20);		 	
	        circle.setFill(Color.LIGHTGRAY);	        
	        circle.setStroke(Color.BLACK);       
	        circle.setStrokeWidth(1);
	        
	        //add label
	        Label label = new Label(Integer.toString(node.order));
	        
	        //update the label position after the label width and height have been set
	        label.widthProperty().addListener((obs, oldVal, newVal) -> {
	            label.setLayoutX(x - newVal.doubleValue() / 2);
	        });
	        label.heightProperty().addListener((obs, oldVal, newVal) -> {
	            label.setLayoutY(y - newVal.doubleValue() / 2);
	        });
	
	        label.setMouseTransparent(true);
		             
	        //record node and it's UI
	        circleNodeMap.put(circle, node);
	        circleLabelMap.put(circle, label);
	             
	        final double[] offset = new double[2];
	        
	        //on selected function
	        circle.setOnMousePressed(event -> {
	        	
	        	 if (event.getButton() == MouseButton.PRIMARY ){
	                offset[0] = event.getSceneX() - circle.getCenterX();
	                offset[1] = event.getSceneY() - circle.getCenterY();
	        	 }
	        	 
	        	 //selecting node     	 
	        	 if (event.getButton() == MouseButton.SECONDARY ){
	        		 if(selectedCircle == null) {
	        			 selectedCircle = circle;
	        			 selectedCircle.setStroke(Color.ORANGE);  
	        			 selectedCircle.setStrokeWidth(3);
	        		 }	        		
	        		 else if(selectedCircle==circle) {
	        			 removeSelection();
	        		 }
	        			
	        		 else {
	        			//connecting/disconnecting node
	        			 Edge existEdge = searchEdge(circle,selectedCircle,edges);
	        			 if(existEdge==null) {
	        				 
	        				 Line line =new Line();
		        			 line.setStrokeWidth(2);		 
		        			 mainPane.getChildren().add(0,line);
		        			 
		        			 Edge edge = new Edge(circle,selectedCircle,line);
		        			 edge.updateLine();       			 
		        			 edges.add(edge);
		        			 
		        			 node.connect(circleNodeMap.get(selectedCircle));   
		        			 
	        			 }else {
	        				 node.disconnect(circleNodeMap.get(selectedCircle));
	        				 edges.remove(existEdge);
	        				 mainPane.getChildren().remove(existEdge.line);        				 
	        			 }	        			 
	        			 	removeSelection();    			 	        			
	        		 }     			   		         		 
	        	 }      	         
	        });

	        //on drag function
	        circle.setOnMouseDragged(event -> {
	            if (event.getButton() == MouseButton.PRIMARY) {
	                double newX = event.getSceneX() - offset[0];
	                double newY = event.getSceneY() - offset[1];
	                // Update circle
	                circle.setCenterX(newX);
	                circle.setCenterY(newY);

	                // Update label to follow circle
	                label.setLayoutX(newX - label.getWidth()/2);
	                label.setLayoutY(newY - label.getHeight()/2);
	                
	                // Update lines to follow circle
	                updateLine(circle);	                
	                node.updateLocation(circle.getCenterX(), circle.getCenterY());                
	            }            
	        });
	        
	        mainPane.getChildren().add(circle);
	        mainPane.getChildren().add(label);
	        
	        
	}

	@FXML
	public void setStartNode() {
	    if (selectedCircle == null) 
	        return;
	    
	    if (selectedCircle == startCircle) 
	        return;
	    
	    if (startCircle != null) 
	        startCircle.setFill(Color.LIGHTGRAY); 
	  	
	    startCircle = selectedCircle;
	    if (startCircle == goalCircle) {
	    	goalCircle.setFill(Color.LIGHTGRAY);
	        goalCircle = null;
	    }
	    
	    startCircle.setFill(Color.LIGHTGREEN); 
	    removeSelection(); 
	}
	
	@FXML
	public void setGoalNode() {
	    if (selectedCircle == null) 
	        return;
	    
	    if (selectedCircle == goalCircle) 
	        return;
	    
	    if (goalCircle != null) 
	        goalCircle.setFill(Color.LIGHTGRAY);
	    
	    goalCircle = selectedCircle;
	    if (goalCircle == startCircle) {
	        startCircle.setFill(Color.LIGHTGRAY);
	        startCircle = null;
	    }
	    
	    goalCircle.setFill(Color.PINK); 
	    removeSelection(); 
	}
	
	@FXML
	public void deleteNode() {
		if(selectedCircle == null) {
			return;
		}
		
		//remove startCircle and goalCircle if it is the node to be deleted
		if (selectedCircle == startCircle) 
		    startCircle = null;
		    
	    if (selectedCircle == goalCircle) 
	    	goalCircle = null;
		
	    //remove UI
		mainPane.getChildren().remove(selectedCircle);
		mainPane.getChildren().remove(circleLabelMap.get(selectedCircle));
		deleteEdge(selectedCircle);
		
		//remove Node connection
		circleNodeMap.get(selectedCircle).selfDestruct();
			
		//remove the node
		circleNodeMap.remove(selectedCircle);
		circleLabelMap.remove(selectedCircle);
		selectedCircle = null;	
	}
	
	@FXML
	public void clearAll() {
		startCircle = null;
		goalCircle = null;
		selectedCircle = null;
		circleLabelMap.clear();
		circleNodeMap.clear();
		edges.clear();
		mainPane.getChildren().clear();
		i = 1;
		
	}
	
	public void removeSelection() {
		if(selectedCircle!=null) {
			selectedCircle.setStroke(Color.BLACK);  
			selectedCircle.setStrokeWidth(1);
			selectedCircle = null;	
		}
	}
	
	public void updateLine(Circle circle) {
		 for(Edge edge: edges) {
         	if(edge.nodeA == circle || edge.nodeB ==circle) {
         		edge.updateLine();
         	}
         }
	}
	
	public void deleteEdge(Circle circle) {
		List<Edge> toRemove = new ArrayList<>();
		for (Edge edge : edges) {
		    if (edge.nodeA == circle || edge.nodeB == circle) {
		        toRemove.add(edge);
		        mainPane.getChildren().remove(edge.line);
		    }
		}
		edges.removeAll(toRemove);
	}
	
	// Marked static to allow usage from other classes without duplication
	public static Edge searchEdge(Circle circle, Circle selectedCircle, List<Edge> edges) {
		//return the edge if two circle are connected, else return null
		for(Edge edge: edges) {
         	if((edge.nodeA == circle && edge.nodeB ==selectedCircle) ||(edge.nodeA == selectedCircle && edge.nodeB ==circle)) {
         		return edge;
         	}         	
         }		
		return null;	
	}
	
	public void showCurrentGraph() {
		for (Node node : circleNodeMap.values()) {
		    System.out.print("Node " + node.order + " is connected to: ");

		    if (node.connectedNodes != null && !node.connectedNodes.isEmpty()) {
		        for (Node connected : node.connectedNodes) {
		            System.out.print(connected.order + " ");
		        }
		    } else {
		        System.out.print("none");
		    }
		    System.out.println(); 
		}		
		System.out.println();
	}
			
	public void runComparison() {
		if(startCircle == null || goalCircle == null) {
		    Alert alert = new Alert(AlertType.WARNING);
		    alert.setTitle("Invalid action");
		    alert.setContentText("Please select both a start and goal node before proceeding.");
		    alert.showAndWait();
			return;
		}
	
		
		ArrayList<Node> allNodes = new ArrayList<>(circleNodeMap.values());
		
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Report.fxml"));
	        Parent root = loader.load();

	        // Get controller after loading
	        ReportController reportController = loader.getController();
	        
	        // Run algorithmn and generate report
	        long totalTime = 0;
	        //Run DFS 100 time for avg time used   
	        for (int i=0;i<100;i++) {
	        	long startTime = System.nanoTime();        
		        DFS_Bot.runDFS(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes);        
		        long endTime = System.nanoTime();
		        totalTime += endTime - startTime;	 
	        }
	        
	        List<Node> DFSpath = DFS_Bot.runDFS(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes);   	
	  	    reportController.generateReport(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes, DFSpath, ReportController.ReportType.DFS, totalTime/100);
	      
	        //Run BFS 100 time for avg time used  
	  	    totalTime = 0;
	  	    for (int i=0;i<100;i++) {
		        long startTime = System.nanoTime();
		        BFS_Bot.runBFS(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes);
		        long endTime = System.nanoTime();
		        totalTime += endTime - startTime;	
	  	    }
	  	    List<Node> BFSpath = BFS_Bot.runBFS(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes);
	        reportController.generateReport(circleNodeMap.get(startCircle), circleNodeMap.get(goalCircle), allNodes, BFSpath, ReportController.ReportType.BFS, totalTime/100);
	      
	        Stage stage = new Stage();
	        stage.setTitle("Comparison Report");
	        stage.setScene(new Scene(root));
	        stage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	
	public int countVisitedNode(List<Node> allNodes) {
		 int i = 0;
		    for(Node node:allNodes) {
		    	if(node.visited)
		    		i++;
		    }
		  return i;	
	}
}


