package Entity;

import java.net.Socket;

//将每一个socket取个名字便于寻找，这就是SocketEntity的作用
public class SocketEntity {
	private Socket socket;
	private String email;
	
	public SocketEntity() {
		super();
	}
	
	public SocketEntity(Socket socket, String email) {
		super();
		this.socket = socket;
		this.email = email;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
