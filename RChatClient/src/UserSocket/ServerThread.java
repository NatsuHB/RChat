package UserSocket;
/**
 * Ϊ�˶��û�ͨ������ͻ��Ϊÿһ��ͨ���½��߳�
 */
import java.util.HashMap;
import javax.swing.JOptionPane;

import Entity.ChatUIEntity;
import Entity.Friend;
import Entity.Message;
import Entity.User;
import Frame.Chat;
import Frame.Send_File;
import Security.RSA;
import _Util.ChatUIList;
import _Util.CommandTranser;

public class ServerThread extends Thread{
	private Client client;
	private Friend frdSender = null;
	private Friend frdReceiver = null;
	private User user;
	
	public ServerThread(Client friend_client, User user) {
		System.out.println("�̳߳�ʼ��");
		this.client = friend_client;
		this.user = user;
	}
	
	public void setFrdSender(Friend frdSender) {
		this.frdSender = frdSender;
	}
	
	public void setFrdReceiver(Friend frdReceiver) {
		this.frdReceiver = frdReceiver;
	}
	
	//��дrun����
	@Override
	public void run() {
		while(client.getSocket()!=null) {
			
		CommandTranser cmd = client.getData();
		if (cmd == null) return;
			try {
				execute(cmd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	//����ִ������
	private void execute(CommandTranser cmd) throws Exception {
		//������ܵ���ԿЭ������
		if("key_chat".equals(cmd.getCmd())&&cmd!=null) {
			//�Ӵ������л�ȡ������Կ������
			String des_key_encrypted = (String)cmd.getData();
			System.out.println("�յ���DES��Կ"+des_key_encrypted);
			System.out.println("�ҵ�˽Կ"+user.getPriKey());
			RSA rsa = new RSA();
			String des_key = rsa.decode(des_key_encrypted, user.getPriKey());
			
			System.out.println("���ܺ��DES��Կ"+des_key);
			
			//�ҵ���������ĺ��ѣ��½������
			HashMap<Integer, Friend> friendList = user.getFriendList();
			Friend friend;
			Chat chatUI;
			for(Friend value:friendList.values()) {
				if(client.getSocket().getInetAddress().getHostAddress().equals(value.getIP())) {
					chatUI = new Chat(user, value, client, des_key);
					ChatUIEntity chatUIentity = new ChatUIEntity(chatUI, value.getFriendEmail());
					ChatUIList.addChatUI(chatUIentity);
				}
			}
			cmd.setFlag(true);
			cmd.setCmd("back_key_chat");
			client.sendData(cmd);
		}
		
		//������ܵ����ѷ�������Ϣ
		if("message".equals(cmd.getCmd())&&cmd!=null) {
			//��ü��ܺ����Ϣ���ݣ����������
			String friendename = cmd.getSender();
			Chat chatUI2 = ChatUIList.getChatUI(friendename);
			HashMap<Integer, Friend> friendList = user.getFriendList();
			/* if(chatUI2==null) {
				for(Friend value:friendList.values()) {
					if(client.getSocket().getInetAddress().getHostAddress()==value.getIP()) {
						chatUI2 = new Chat(user, value, client);
					}
				}
				ChatUIEntity chatUIentity = new ChatUIEntity(chatUI2, friendemail);
				ChatUIList.addChatUI(chatUIentity);
			}
			else {
				chatUI2.show();
				Message message = (Message) cmd.getData();
				chatUI2.Receive_Message(message);
			}*/
			chatUI2.show();
			Message message = (Message) cmd.getData();
			chatUI2.Receive_Message(message);
		}
		
		//���ܵ�ͨ���������ر������
		if("chat_end".equals(cmd.getCmd())&&cmd!=null) {
			client.setSocket(null);
			System.out.println("chat_end");
			Chat chatUI = ChatUIList.getChatUI(cmd.getSender());
			chatUI.dispose();
			ChatUIList.deletChatUI(cmd.getSender());
		}
		
		//���ܵ��ļ���������
		if("file".equals(cmd.getCmd())&&cmd!=null) {
			Chat chatUI = ChatUIList.getChatUI(cmd.getSender());
			chatUI.show();
			//JOptionPane.showMessageDialog(null, cmd.getSender()+"�������������ļ�");
			//ѯ���Ƿ�ͬ������ļ�
			int flag = JOptionPane.showConfirmDialog(null, cmd.getSender()+"�����������ļ����Ƿ�ͬ��", "�ļ���������", JOptionPane.YES_NO_OPTION);
			//ͬ������ļ�
			if(flag == 0) {
				CommandTranser cmd2 = new CommandTranser();
				cmd2.setCmd("file_back");
				cmd2.setSender(user.getUseremail());
				cmd2.setReceiver(cmd.getSender());
				cmd2.setData(cmd.getData());
				client.sendData(cmd2);
			}
			//�ܾ������ļ�
			else {
				CommandTranser cmd2 = new CommandTranser();
				cmd2.setCmd("file_refuse");
				cmd2.setSender(user.getUseremail());
				cmd2.setReceiver(cmd.getSender());
				client.sendData(cmd2);
			}
			//String file_len = client.FileGet("C:/Users/dell/Desktop/�����ļ�");
			//Message mess = new Message(file_len);
			//chatUI.Receive_Message(mess);
		}
		
		//���ܵ��ܾ��ļ�����
		if("file_refuse".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getSender()+"�ܾ��������ļ���������");
		}
		
		//���ܵ�ͬ���ļ����䣬�����ļ�ѡ���
		if("file_back".equals(cmd.getCmd())) {
			String friend_ip = (String)cmd.getData();
			Send_File file_send = new Send_File(friend_ip);
		}
			
		}
	}
