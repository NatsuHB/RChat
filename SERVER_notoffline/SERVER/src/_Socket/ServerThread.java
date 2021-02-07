package _Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Entity.Friend;
import Entity.SocketEntity;
import Entity.User;
import _Service.UserService;
import _Util.CommandTranser;
import _Util.SocketList;

public class ServerThread extends Thread{
	private Socket socket;
	private Friend frdSender = null;
	private Friend frdReceiver = null;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void setFrdSender(Friend frdSender) {
		this.frdSender = frdSender;
	}
	
	public void setFrdReceiver(Friend frdReceiver) {
		this.frdReceiver = frdReceiver;
	}
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		ObjectOutputStream oos1 = null;
		ObjectOutputStream oos2 = null;
		ObjectOutputStream oos3 = null;
		
		while(socket != null) {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				CommandTranser cmd = (CommandTranser) ois.readObject();
				oos1 = null;
				oos2 = null;
				oos3 = null;
				
				//ִ���������Կͻ��˵�����
				cmd = execute(cmd);
				
				//����Ϊ���ط�����ִ��֮��Ľ��
				//��Ϣ�Ի����󣬷�������sender��������Ϣ���͸�receiver
				if("message".equals(cmd.getCmd())) {
					//��� msg.ifFlag�� ����������ɹ� ���������ѷ�����Ϣ ���������������Ϣʧ�� ��Ϣ���͸������߱���
					if(cmd.isFlag()) {
						//System.out.println("�Է�����");
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					} else {
						//System.out.println("�Է�δ����");
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				if ("WorldChat".equals(cmd.getCmd())) {
					HashMap<String, Socket> map = SocketList.getMap();
					Iterator<Map.Entry<String, Socket>> it = map.entrySet().iterator();
					while(it.hasNext()) {
						Map.Entry<String, Socket> entry = it.next();
						if(!entry.getKey().equals(cmd.getSender())) {
							oos1 = new ObjectOutputStream(entry.getValue().getOutputStream());
							oos1.writeObject(cmd);
						}
					}
					continue;
					
				}
				//��¼���� �����ݷ��͸�sender
				if ("login".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
					User user = (User)cmd.getData();
					
					CommandTranser newcmd =  new CommandTranser();
					Friend friend = new Friend();
					friend.setId(user.getUserId());
					friend.setIP(socket.getInetAddress().getHostAddress());
					friend.setPort(socket.getPort());
					friend.setStatus(true);
					newcmd.setData(friend);
					newcmd.setCmd("friend_login");
					
					for(Friend value : user.getFriendList().values()) {
						Socket sock = SocketList.getSocket(value.getFriendEmail());
						System.out.println("��¼��Ϣ����"+value.getFriendEmail());
						System.out.println("��¼��Ϣ����"+value.getFriendName());
						if(sock!=null){
							oos3 = new ObjectOutputStream(sock.getOutputStream());
							oos3.writeObject(newcmd);
							System.out.println("��¼�����Ѿ�����ȥ��");
						}
					}
					System.out.println("��¼�ɹ���");
					System.out.println(oos1==null);
				}
				
				//ע������ �����ݷ��͸�sender
				if ("register".equals(cmd.getCmd())) {
					System.out.println("��ע��������sender");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�洢��Կ���� ���û����ݲ������ݿⲢ���ؿͻ��˽��
				if("send_key".equals(cmd.getCmd())) {
					System.out.println("����sender���ݲ���ɹ�");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//��Ӻ����������ݷ��͸� receiver
				if ("request_add_friend".equals(cmd.getCmd())) {
					//���ߣ������󷢸�receiver
					String email = (String)cmd.getData();
					CommandTranser newcmd =  new CommandTranser();
					newcmd.setCmd("back_add_friend");
					if(cmd.isFlag()) {
						newcmd.setFlag(true);
						newcmd.setResult("�Է��յ������ĺ�������");
						oos1 = new ObjectOutputStream(SocketList.getSocket(email).getOutputStream());
						oos3 = new ObjectOutputStream(socket.getOutputStream());
						oos3.writeObject(newcmd);
						System.out.println("yingyingying");
//						oos3 = new ObjectOutputStream(socket.getOutputStream());
//						CommandTranser newcmd =  new CommandTranser();
//						newcmd = cmd;
//						newcmd.setCmd("successful");
//						oos3.writeObject(newcmd);
					} else {
						//�����ڲ����߶�Ҫ���ͷ���ʾ��Ϣ���ͳɹ�
						newcmd.setFlag(false);
						newcmd.setResult("��ǰ�û������߻���û�������");
						//oos1 = new ObjectOutputStream(SocketList.getSocket(email).getOutputStream());
						oos3 = new ObjectOutputStream(socket.getOutputStream());
						oos3.writeObject(newcmd);
					}
				}
				
				//ͬ����Ӻ����������ݷ��͸� receiver��sender
				if ("accept_add_friend".equals(cmd.getCmd())) {
					CommandTranser newcmd =  new CommandTranser();
					newcmd.setReceiver(cmd.getReceiver());
					newcmd.setSender(cmd.getSender());
					newcmd.setCmd("accept_add_friend");
					newcmd.setFlag(cmd.isFlag());
					newcmd.setResult(cmd.getResult());
					newcmd.setData(frdReceiver);
					cmd.setData(frdSender);
					//�����Ƿ�ɹ��������ݿⶼҪ��������������п����������Ŀͻ�������
					oos1 = new ObjectOutputStream(socket.getOutputStream());
					if(SocketList.getSocket(cmd.getSender()) != null) {
						oos3 = new ObjectOutputStream(SocketList.getSocket(cmd.getSender()).getOutputStream());
						if(oos3 != null) {
							oos3.writeObject(newcmd);	
						}
					}
				}
				
//				if("updatefriendlist".equals(cmd.getCmd())) {
//					oos1 = new ObjectOutputStream(socket.getOutputStream());
//				}
				
				//�ܾ���Ӻ����������ݷ��͸� receiver
				if ("refuse_add_friend".equals(cmd.getCmd())) {
					//���ܾ�������
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getSender()).getOutputStream());
					}else { //���ܷ�����������ܾ���������Ϣ
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				/**
				//�޸��������� ���͸�sender ������δʵ��
				if ("changeinfo".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�޸��������� �����ݷ��͸�sender
				if ("changepwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�������� ���͸�sender
				if ("forgetpwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}*/
				
				//�û�����
				if("logout".equals(cmd.getCmd())) {
					User user = (User)cmd.getData();
					
					CommandTranser newcmd =  new CommandTranser();
					int id = user.getUserId();
					newcmd.setData(id);
					newcmd.setCmd("friend_logout");
					
					for(Friend value : user.getFriendList().values()) {
						Socket sock = SocketList.getSocket(value.getFriendEmail());
						if(sock!=null){
							oos3 = new ObjectOutputStream(sock.getOutputStream());
							oos3.writeObject(newcmd);
						}
					}
				}
				
				if(oos1 != null) {
					oos1.writeObject(cmd);	
					System.out.println("oos1�ɹ����ͣ�");
				}
				if(oos2 != null) {
					oos2.writeObject(cmd);	
					System.out.println("oos2�ɹ����ͣ�");
				}
			} catch(IOException e) {
				socket = null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private CommandTranser execute(CommandTranser cmd) {
		//��¼����
		if("login".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.checkUser(user));
			
			//�����¼�ɹ������ÿͻ��˼����Ѿ����ӳɹ���map�������� ���ҿ������û��Ľ����߳�
			if(cmd.isFlag()) {
				// �����̼߳������ӳɹ���map����
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setEmail(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				
				//�����ݿ��ȡ������б���������б������ͻ���
				cmd.setData(userservice.getFriendsList(user));
				cmd.setResult("��¼�ɹ�");
			} else {
				cmd.setResult("���������û�δע��");
			}
		}
		
		//ע������
		if("register".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(!userservice.isExist(user));
			//���ע��ɹ�
			if(cmd.isFlag()) {
				/**
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//��ע��Ŀ϶�û�к��� */
				cmd.setResult("����ע�ᣬ�뷢���û���˽Կ");
			} else {
				cmd.setResult("ע��ʧ�ܣ��������ѱ�ע��");
			}
		}
		
		//�洢�û���Ϣ
		if("send_key".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.registerUser(user));
			//����洢�ɹ�
			if(cmd.isFlag()) {
				/**
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//��ע��Ŀ϶�û�к��� */
				cmd.setResult("ע��ʱ�洢�û���Ϣ�ɹ�");
			} else {
				cmd.setResult("ע��洢�û���Ϣʧ��");
			}
		}
		
		/**
		//�޸��������� ������δʵ��
		if("changeInfo".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changeInfo(user));
			if(cmd.isFlag()) {
				cmd.setResult("�޸���Ϣ�ɹ�");
			} else {
				cmd.setResult("�޸���Ϣʧ��");
			}
		}*/
		
		//��Ӻ���
		if("request_add_friend".equals(cmd.getCmd())) {
			//����û��Ƿ�����
			String email = (String)cmd.getData();
			if(SocketList.getSocket(email) != null) {
				cmd.setFlag(true);
				cmd.setResult("�Է��յ������ĺ�������");
			} else {
				cmd.setFlag(false);
				cmd.setResult("��ǰ�û������߻���û�������");
			}
		}
		
		//ͬ����Ӻ�������
		if("accept_add_friend".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			cmd.setFlag(userservice.addFriend(cmd.getReceiver(), cmd.getSender()));
			frdSender = userservice.getFrd1();
			frdReceiver = userservice.getFrd2();
			if(cmd.isFlag()) {
				cmd.setResult("������ӳɹ�");
			} else {
				cmd.setResult("���������ϵ�����Ӻ���ʧ�ܻ��������Ѿ�Ϊ����");
			}
		}
		
//		if("updatefriendlist".equals(cmd.getCmd())) {
//			UserService userservice = new UserService();
//			User user = (User)cmd.getData();
//			user = userservice.getFriendsList(user);
//			if(user.getFriendsNum() == 0) {
//				cmd.setFlag(false);
//			} else {
//				cmd.setData(user);
//			}
//		}
		
		//�ܾ���Ӻ���
		if("refuse_add_friend".equals(cmd.getCmd())) {
			//����Ƿ�����
			if(SocketList.getSocket(cmd.getSender()) != null) {
				cmd.setFlag(true);
				cmd.setResult("���� " + cmd.getReceiver() +  " �ܾ���");
			} else {
				cmd.setFlag(false);
				cmd.setResult("�Է������߲�֪����ܾ������ĺ�������");
			}
		}
		
		//������Ϣָ��
		if("message".equals(cmd.getCmd())) {
			//����Ƿ�����
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				//cmd.setResult("�Է��ɹ��յ�������Ϣ");
			} else {
				cmd.setFlag(false);
				cmd.setResult("��ǰ�û�������");
			}
		}
		
		/**
		if("WordChat".equals(cmd.getCmd())) {
			cmd.setFlag(true);
		}
		//��������ָ�� �������Ҫ���û�������ʹ𰸷���
		if("forgetpwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			user = userservice.getUser(user);
			//����û�����
			if(user != null ) {
				cmd.setResult("��ѯ�ɹ�");
				cmd.setData(user);
				cmd.setFlag(true);
			} else {
				cmd.setResult("�û����ܲ�����");
				cmd.setFlag(false);
			}
		}	
		
		
		if ("changepwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changePassword(user));
			System.out.println("there 111 ");
			System.out.println(user.getUsername());
			if(cmd.isFlag()) {
				cmd.setResult("�޸�����ɹ�");
			}else {
				cmd.setResult("�޸�����ʧ��");
			}
		}*/
		
		if("logout".equals(cmd.getCmd())) {
			//UserService userservice = new UserService();
			SocketList.deleteSocket(cmd.getSender());
			System.out.println("�ǳ�����");
			//cmd.setCmd("sender_logout");
			//userservice.logOut(cmd);
		}
		
		return cmd;
	}
}
