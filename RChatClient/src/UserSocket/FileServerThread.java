package UserSocket;
/**
 * Ϊ���ļ����䲻��ͻ���½��ļ������߳�
 */
import javax.swing.JOptionPane;

import Entity.Friend;
import Entity.User;

public class FileServerThread extends Thread{
	private fileClient client;
	private Friend frdSender = null;
	private Friend frdReceiver = null;
	private User user;
	
	public FileServerThread(fileClient friend_client, User user) {
		System.out.println("�ļ������̳߳�ʼ��");
		this.client = friend_client;
		this.user = user;
	}
	
	public void setFrdSender(Friend frdSender) {
		this.frdSender = frdSender;
	}
	
	public void setFrdReceiver(Friend frdReceiver) {
		this.frdReceiver = frdReceiver;
	}
	
	@Override
	//��дrun����
	public void run() {
		while(client.getSocket()!=null) {
			execute(client);
		}
	
	}
	
	//��run�����ж������Ҫִ�е�����
	private void execute(fileClient client) {
		String size = client.FileGet("C:\\Users\\dell\\Desktop\\�����ļ�");
		JOptionPane.showMessageDialog(null, "������ϣ��ļ���СΪ"+size);
		//Get_File get_file = new Get_File(client);
		client.setSocket(null);
	}
}