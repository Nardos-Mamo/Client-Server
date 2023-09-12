
public class Client {
private String hostName;
private int port;
private String userName;

public Client() {
	
}
public Client(String hostName,int port ) {
	this.hostName = hostName;
	this.port = port;
	}

public void setUserName(String userName) {
	this.userName= userName;
	
}
public String getUserName() {
	return this.userName;
	
}
public void setPort(int port) {
	this.port= port;
	
}
public int getport() {
	return this.port;
	
}
public void setHostName(String hostName) {
	this.hostName= hostName;
	
}
public String gethostName() {
	return this.hostName;
	
}}