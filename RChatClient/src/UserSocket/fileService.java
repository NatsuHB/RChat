package UserSocket;
/**
 * 监听来自其他好友的文件传输请求
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Entity.User;

public class fileService {
	private User user;
	public fileService(User user) {
		this.user = user;
	}
	public void startService() {
		try {
			ServerSocket ss = new ServerSocket(2224);
			Socket socket = null;
			
			//不断监听来自好友的文件传输请求，并为它们建立传输类和线程
			while(true) {
				socket = ss.accept();
				fileClient friend_client_file = new fileClient(socket);
				FileServerThread file_thread = new FileServerThread(friend_client_file, user);
				file_thread.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}