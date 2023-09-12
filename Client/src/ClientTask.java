import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import java.net.*;
import java.io.*;

public class ClientTask extends Task<Void>
{
	private BufferedReader reader;
    private Socket socket;
    private Client client;
	
	
	public ClientTask()
	{
	} 
	
	public ClientTask(Socket socket, Client client)
	{
		this.socket = socket;
		this.client = client;
		
		try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
	} 
	@Override
	protected Void call()
	{
		 while (true) {
	            try {
	                String response = reader.readLine();
	                System.out.println("\n" + response);

	                this.updateMessage(response);
	                if (client.getUserName() != null) {
	                    System.out.print("[" + client.getUserName() + "]: ");
	                }
	            } catch (IOException ex) {
	                System.out.println("Error reading from server: " + ex.getMessage());
	                ex.printStackTrace();
	                break;
	            }
	        }
		 return null;
	}
	
	@Override
	protected void cancelled()
	{
	super.cancelled();
	updateMessage("The task was cancelled.");
	
	}
	
	@Override
	protected void failed()
	{
		super.failed();
		updateMessage("The task failed.");
	}
@Override
public void succeeded()
{
super.succeeded();
updateMessage("The task finished successfully.");
}
	

}
