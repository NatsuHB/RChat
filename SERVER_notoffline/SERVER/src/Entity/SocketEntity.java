package Entity;

import java.net.Socket;

//��ÿһ��socketȡ�����ֱ���Ѱ�ң������SocketEntity������
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
