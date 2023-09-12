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
import javafx.concurrent.Worker;

public class ClientGUI extends Application {
    // IP
    private Label ipLabel = new Label("IP:");
    private TextField ipTextFiled = new TextField("localhost");
    // Port number
    private Label portLabel = new Label("Port:");
    private TextField portTextFiled = new TextField();
    // name
    private Label nameLabel = new Label("User name:");
    private TextField nameTextFiled = new TextField();
    // Connect button
    private Button connectBtn = new Button("Connect");
    
    // chat room chatWindow
    private TextArea chatWindow = new TextArea();
    // incoming text
    private Label incomingText = new Label();
    // outgoing text
    private Label outgoingText = new Label();
    // chat room text field
    private TextField sendTextField = new TextField();
    // char room send button
    private Button sendBtn = new Button("Send");

    private Socket socket = null;
    private Client client = new Client();
    private ClientTask clientTask;
    private PrintWriter writer;
    
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // generate login GUI
        HBox hbox1 = new HBox();
        hbox1.setPadding(new Insets(15, 12, 15, 12));
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);
        portTextFiled.setPrefWidth(70);
        hbox1.getChildren().addAll(ipLabel, ipTextFiled, portLabel, portTextFiled, nameLabel, nameTextFiled,connectBtn);
        
        // connect button event handler
        connectBtn.setOnAction(e -> {
            // TODO ....
            String hostname = ipTextFiled.getText();
            int port = Integer.valueOf(portTextFiled.getText());
            
            try {
            	socket = new Socket(hostname, port);
            	System.out.println("Connected to the chat server");
            	
            	client.setHostName(hostname);
            	client.setPort(port);
            	client.setUserName(nameTextFiled.getText());
            	
            	clientTask = new ClientTask(socket, client);
            	
            	bindClientTaskMessages(clientTask);
            	
            	startTask(clientTask);
            	
            	 try {
                     OutputStream output = socket.getOutputStream();
                     writer = new PrintWriter(output, true);
                 } catch (IOException ex) {
                     System.out.println("Error getting output stream: " + ex.getMessage());
                     ex.printStackTrace();
                 }
            	 
            	 writer.println(client.getUserName());
            	
            	
            	
            } catch (UnknownHostException ex) {
            	System.out.println("Server not found:" +ex.getMessage());
            	
            } catch (IOException ex) {
    			System.out.println("I/O Error: " + ex.getMessage());
    		} finally {
                try {
                    if(socket.isClosed()) {
                        socket.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            // open chat room GUI and close connect GUI
            Scene chatScene = new Scene(addChatRoomGUI());
            Stage chatStage = new Stage();
            chatStage.setTitle("Chat Room");
            chatStage.setScene(chatScene);
            chatStage.setResizable(false);
            // show chat windows
            chatStage.show();
            // close connect windows
            primaryStage.close();
        });
        
        // Create a scene and place it in the stage
        Scene scene = new Scene(hbox1);
        primaryStage.setTitle("Connect Server"); // Set the stage title
        primaryStage.setResizable(false); // disable resize
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
    public void startTask(ClientTask task)
    {
    	Thread backgroundThread = new Thread(task);
    	backgroundThread.setDaemon(true);
    	backgroundThread.start();
    }
    
    public void bindClientTaskMessages(final Worker<Void> worker) {
    	incomingText.textProperty().bind(worker.messageProperty());
    	
    	incomingText.textProperty().addListener(new ChangeListener<String> (){
    		@Override
    		public void changed(ObservableValue<? extends String> ov, String t, String t1) {
    			chatWindow.appendText("\n" + t1);
    		}
    	});
    	
    	
    }
    
    // create GUI for chat room
    public VBox addChatRoomGUI(){
        HBox buttonHBox = new HBox();
        VBox ChatVBox = new VBox();
        
        // set size, space, etc.
        buttonHBox.setAlignment(Pos.CENTER);
        sendTextField.setPrefWidth(470);
        chatWindow.setPrefHeight(400);
        chatWindow.setEditable(false);
        
        chatWindow.textProperty().addListener(new ChangeListener<Object>()
        		{
        	@Override
        	public void changed(ObservableValue<?> observable, Object oldvalue, Object newValue) {
        		chatWindow.setScrollTop(Double.MAX_VALUE);
        
        	}
        		}  );  
        buttonHBox.setSpacing(10);
        buttonHBox.getChildren().addAll(sendTextField, sendBtn);
        
        // send button event handler
        sendBtn.setOnAction(e -> {
            // TODO....
        	String sendMSG = sendTextField.getText();
        	if(sendMSG != "") {
        	writer.println(sendMSG);
        	chatWindow.appendText("\n[" + client.getUserName() + "]:"+sendMSG);
        	sendTextField.setText("");
        	}
        	//extra credit add listener
        });
        
        ChatVBox.setAlignment(Pos.CENTER);
        ChatVBox.setPadding(new Insets(15, 12, 15, 12));
        ChatVBox.setSpacing(10);
        ChatVBox.getChildren().addAll(chatWindow, buttonHBox);
        
        return ChatVBox;
    }
    

public static void main(String[] args) {
	launch(args);
}
}

