package application;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
		// constructing our scene

			 URL url = getClass().getResource("BoardGui.fxml");
			 AnchorPane pane = FXMLLoader.load( url );
			 Scene scene = new Scene( pane );

			 // setting the stage
			 primaryStage.setScene( scene );
			 primaryStage.setTitle( "Reversi - AI" );
			 

			 primaryStage.show();

			 
			 
		} catch(Exception e) {
			e.printStackTrace();
		}
			
	}

	public static void main(String[] args) {
		launch(args);
		
	}
	
	 
	    
	
	
	
}
