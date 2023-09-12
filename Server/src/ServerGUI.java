import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import java.net.*;
import java.io.*;
import java.util.*;
import javafx.concurrent.Worker;

public class ServerGUI extends Application {
	private int port;
	private Set<String> userNames = new HashSet<>();
	
	private Label portLabel = new Label("Port:");
	private TextField portTextField = new TextField();
	private Button launchBtn = new Button("Launch");
	private Button shutDownBtn = new Button("Shut Down");
	private Label incomingText = new Label();
	private TextArea serverInfo = new TextArea();
	
	private Server server = new Server();
	
	 @Override // Override the start method in the Application class
	    public void start(Stage primaryStage) {
	        // generate login GUI
	        HBox hbox1 = new HBox();
	        hbox1.setPadding(new Insets(15, 12, 15, 12));
	        hbox1.setSpacing(10);
	        hbox1.setAlignment(Pos.CENTER);
	      
	        hbox1.getChildren().addAll(portLabel, portTextField,launchBtn, shutDownBtn);
	        VBox vbox1 = new VBox();
	        vbox1.setPadding(new Insets(0,12,15,12));
	        vbox1.getChildren().addAll(hbox1,serverInfo);
	        
	        launchBtn.setOnAction(e -> {
	        	
	        	port = Integer.valueOf(portTextField.getText());
	        	server.setPort(port);
	        	ServerTask servertask = new ServerTask(server, port);
	        	
	        	startServerTask(servertask);
	        	
	        	
	        	
	 });
	        Scene scene = new Scene(vbox1);
	        primaryStage.setTitle("chat room"); // Set the stage title
	        primaryStage.setResizable(false); // disable resize
	        primaryStage.setScene(scene); // Place the scene in the stage
	        primaryStage.show(); // Display the stage
	    }

	 public void startServerTask(ServerTask task)
	    {
	    	Thread backgroundThread = new Thread(task);
	    	backgroundThread.setDaemon(true);
	    	backgroundThread.start();
	    }
	    
public static void main(String[] args) {
	launch(args);
}
}

	
	


