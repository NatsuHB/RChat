package UserSocket;
/**
 * 为了文件传输不冲突，新建文件传输线程
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
		System.out.println("文件传输线程初始化");
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
	//重写run方法
	public void run() {
		while(client.getSocket()!=null) {
			execute(client);
		}
	
	}
	
	//在run方法中定义具体要执行的任务
	private void execute(fileClient client) {
		String size = client.FileGet("C:\\Users\\dell\\Desktop\\传输文件");
		JOptionPane.showMessageDialog(null, "接受完毕，文件大小为"+size);
		//Get_File get_file = new Get_File(client);
		client.setSocket(null);
	}
}