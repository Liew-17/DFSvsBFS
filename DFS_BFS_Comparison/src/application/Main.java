package application;
	
import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
			
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("DFS vs BFS");
			
			primaryStage.setResizable(false);			
			
			primaryStage.setScene(scene);
			
			primaryStage.setFullScreenExitHint("");
			
			primaryStage.show();
		
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
