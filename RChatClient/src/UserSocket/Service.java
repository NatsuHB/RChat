package UserSocket;
/**
 * 监听来自其他好友的聊天请求
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Entity.User;

public class Service {
	private User user;
	public Service(User user) {
		this.user = user;
	}
	public void startService() {
		try {
			ServerSocket ss = new ServerSocket(2223);
			Socket socket = null;
			
			//不断接收来自好友的聊天请求，并为它们建立线程
			while(true) {
				socket = ss.accept();
				Client friend_client = new Client(socket);
				ServerThread thread = new ServerThread(friend_client, user);
				thread.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}