package UserSocket;
/**
 * 这个类用于建立与服务器或其他用户的交流，发送信息
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import _Util.CommandTranser;

public class Client {
	private int port = 2222; //服务器端口
	private String Sever_address = "192.168.43.25"; //服务器主机ip
	private Socket socket;//用于连接的socket
	
	//实例化， 建立与服务器的连接
	public Client(){
		try {
			socket = new Socket(Sever_address, port);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}
	}
	
	//实例化，主动发起通话时，通过ip 建立与其他用户的连接
	public Client(String Friend_address) {
		try {
			socket = new Socket(Friend_address, port+1);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "好友连接失败");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "好友连接失败");
		}
		
	}
	
	//实例化，被动接受通话时，直接将接收到的socket传入
	public Client(Socket Friend_socket) {
		socket = Friend_socket;
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	} 
	
	//向服务端或用户发送数据
	public void sendData(CommandTranser cmd) {
		ObjectOutputStream oos = null; //主要的作用是用于写入对象信息与读取对象信息。 对象信息一旦写到文件上那么对象的信息就可以做到持久化了
		try {
			if(socket == null) {
				return;
			}
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(cmd);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "IO错误");
		}
	}
	
	//接受服务端或用户发送的消息
	public CommandTranser getData() {
		ObjectInputStream ois = null;
		CommandTranser cmd = null;
		if(socket == null) {
			//System.out.println("weishenme");
			return null;
		}
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			cmd = (CommandTranser) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		/*
		 * 这里有用吗？-----------------------------------------------------------------------------------应该没用只是在测使输出
		 */
//		if("message".equals(cmd.getCmd())) {
//			System.out.println((String)cmd.getData());
//		}
		
		return cmd;
	}
	

}
