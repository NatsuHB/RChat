package UserSocket;
/**
 * ���������������ѵ��ļ���������
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
			
			//���ϼ������Ժ��ѵ��ļ��������󣬲�Ϊ���ǽ�����������߳�
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