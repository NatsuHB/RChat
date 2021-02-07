package Entity;

import java.io.Serializable;

public class Friend implements Serializable{
	private static final long serialVersionUID = 1L;
	private String friend_name;
	private String friend_email;
	private String pubKey;
	private String IP;
	private int port;
	private int id;
	private boolean status = false;
	
	public Friend() {}
	
	public Friend(String friend_name, String friend_email) {
		this.friend_name = friend_name;
		this.friend_email = friend_email;
	}
	
	public String getFriendName() {
		return friend_name;
	}
	
	public String getFriendEmail() {
		return friend_email;
	}
	
	public String getPubKey() {
		return pubKey;
	}
	
	public String getIP() {
		return IP;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public int getId() {
		return id;
	}
	
	public void setIP(String IP) {
		this.IP = IP;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setFriendName(String friend_name) {
		this.friend_name = friend_name;
	}
	
	public void setFriendEmail(String friend_email) {
		this.friend_email = friend_email;
	}
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
}
