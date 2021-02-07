package UserSocket;
/**
 * 为了多用户通话不冲突，为每一个通话新建线程
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
		System.out.println("线程初始化");
		this.client = friend_client;
		this.user = user;
	}
	
	public void setFrdSender(Friend frdSender) {
		this.frdSender = frdSender;
	}
	
	public void setFrdReceiver(Friend frdReceiver) {
		this.frdReceiver = frdReceiver;
	}
	
	//重写run方法
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
	
	//定义执行任务
	private void execute(CommandTranser cmd) throws Exception {
		//如果接受到密钥协商请求
		if("key_chat".equals(cmd.getCmd())&&cmd!=null) {
			//从传输类中获取加密密钥并解密
			String des_key_encrypted = (String)cmd.getData();
			System.out.println("收到的DES密钥"+des_key_encrypted);
			System.out.println("我的私钥"+user.getPriKey());
			RSA rsa = new RSA();
			String des_key = rsa.decode(des_key_encrypted, user.getPriKey());
			
			System.out.println("解密后的DES密钥"+des_key);
			
			//找到发起聊天的好友，新建聊天框
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
		
		//如果接受到好友发来的消息
		if("message".equals(cmd.getCmd())&&cmd!=null) {
			//获得加密后的消息内容，传入聊天框
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
		
		//接受到通话结束，关闭聊天框
		if("chat_end".equals(cmd.getCmd())&&cmd!=null) {
			client.setSocket(null);
			System.out.println("chat_end");
			Chat chatUI = ChatUIList.getChatUI(cmd.getSender());
			chatUI.dispose();
			ChatUIList.deletChatUI(cmd.getSender());
		}
		
		//接受到文件传输请求
		if("file".equals(cmd.getCmd())&&cmd!=null) {
			Chat chatUI = ChatUIList.getChatUI(cmd.getSender());
			chatUI.show();
			//JOptionPane.showMessageDialog(null, cmd.getSender()+"即将向您发送文件");
			//询问是否同意接收文件
			int flag = JOptionPane.showConfirmDialog(null, cmd.getSender()+"想向您发送文件，是否同意", "文件传输请求", JOptionPane.YES_NO_OPTION);
			//同意接收文件
			if(flag == 0) {
				CommandTranser cmd2 = new CommandTranser();
				cmd2.setCmd("file_back");
				cmd2.setSender(user.getUseremail());
				cmd2.setReceiver(cmd.getSender());
				cmd2.setData(cmd.getData());
				client.sendData(cmd2);
			}
			//拒绝接收文件
			else {
				CommandTranser cmd2 = new CommandTranser();
				cmd2.setCmd("file_refuse");
				cmd2.setSender(user.getUseremail());
				cmd2.setReceiver(cmd.getSender());
				client.sendData(cmd2);
			}
			//String file_len = client.FileGet("C:/Users/dell/Desktop/传输文件");
			//Message mess = new Message(file_len);
			//chatUI.Receive_Message(mess);
		}
		
		//接受到拒绝文件传输
		if("file_refuse".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getSender()+"拒绝了您的文件传输请求");
		}
		
		//接受到同意文件传输，生成文件选择框
		if("file_back".equals(cmd.getCmd())) {
			String friend_ip = (String)cmd.getData();
			Send_File file_send = new Send_File(friend_ip);
		}
			
		}
	}
