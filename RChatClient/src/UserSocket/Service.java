package UserSocket;
/**
 * ���������������ѵ���������
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
			
			//���Ͻ������Ժ��ѵ��������󣬲�Ϊ���ǽ����߳�
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