package UserSocket;
/**
 * ��������ڽ�����������������û��Ľ�����������Ϣ
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import _Util.CommandTranser;

public class Client {
	private int port = 2222; //�������˿�
	private String Sever_address = "192.168.43.25"; //����������ip
	private Socket socket;//�������ӵ�socket
	
	//ʵ������ �����������������
	public Client(){
		try {
			socket = new Socket(Sever_address, port);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}
	}
	
	//ʵ��������������ͨ��ʱ��ͨ��ip �����������û�������
	public Client(String Friend_address) {
		try {
			socket = new Socket(Friend_address, port+1);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������ʧ��");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "��������ʧ��");
		}
		
	}
	
	//ʵ��������������ͨ��ʱ��ֱ�ӽ����յ���socket����
	public Client(Socket Friend_socket) {
		socket = Friend_socket;
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	} 
	
	//�����˻��û���������
	public void sendData(CommandTranser cmd) {
		ObjectOutputStream oos = null; //��Ҫ������������д�������Ϣ���ȡ������Ϣ�� ������Ϣһ��д���ļ�����ô�������Ϣ�Ϳ��������־û���
		try {
			if(socket == null) {
				return;
			}
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(cmd);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "IO����");
		}
	}
	
	//���ܷ���˻��û����͵���Ϣ
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
		 * ����������-----------------------------------------------------------------------------------Ӧ��û��ֻ���ڲ�ʹ���
		 */
//		if("message".equals(cmd.getCmd())) {
//			System.out.println((String)cmd.getData());
//		}
		
		return cmd;
	}
	

}
