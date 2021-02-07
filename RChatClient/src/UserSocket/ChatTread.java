package UserSocket;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import Entity.Friend;
import Entity.Offmessage;
import Entity.User;
import Frame.Chat;
import Frame.MainPage;
import Frame.OfflineMessage;
import Security.DES;
import Security.MD5;
import Security.RSA;
import _Util.ChatUIList;
import _Util.CommandTranser;

/**
* ���շ�������ָ�������Ӧ����
* 1.request_add_friend,��Ӻ�������ѡ���Ƿ���ܺ�������������������������
* 2.accept_add_friend�����ܺ���������ӳɹ������º����б�
* 3.back_add_friend���������ɹ����ͺ�������ı�ʶ
* 4.refuse_add_friend���ܾ������������ʧ��
* 5.friend_login���������ߣ����º�������״̬���ر��������촰��
* 6.friend_logout���������ߣ����º�������״̬���ر��������촰��
* 7.offline_message��������Ϣ����ӡ
* 8.delete_friend��ɾ�����ѣ����º����б�
*/
public class ChatTread extends Thread{
	private Client client;
	private boolean isOnline = true;
	private User user; //���ͬ��������� ��ˢ�º����б�
	private MainPage friendsUI; //ˢ�º����б���
	
	public ChatTread(Client client, User user, MainPage friendsUI) {
		this.client = client;
		this.user = user;
		this.friendsUI = friendsUI;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		 this.isOnline = true; 
	}
	
	//run()�����ǲ���Ҫ�û������õģ���ͨ��start��������һ���߳�֮�󣬵��̻߳����CPUִ��ʱ�䣬
	//�����run������ȥִ�о�������񡣼̳�Thread�������дrun��������run�����ж������Ҫִ�е�����
	@Override
	public void run() {
		if(!isOnline) {
			JOptionPane.showMessageDialog(null,  "unbelievable ������");
			return;
		}
		while(isOnline) {
			
			CommandTranser cmd = client.getData();
			//�����������ͬ������յ�����Ϣ(����)
			//���ﴦ�����Է���������Ϣ(����)
			if(cmd != null) {
				
			 execute(cmd);	
			}
		}
	}
	
	//������Ϣ(����)
	private void execute(CommandTranser cmd) {
		
		//��Ӻ�������
		if("request_add_friend".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			String sendername = cmd.getSender();
			int flag = JOptionPane.showConfirmDialog(null, "�Ƿ�ͬ��" + sendername + "�ĺ�������", "��������", JOptionPane.YES_NO_OPTION);
			if(flag == 0) {
				cmd.setCmd("accept_add_friend");  //���ܺ�������
			} else {
				cmd.setCmd("refuse_add_friend");	//�ܾ���������		
			}
			client.sendData(cmd);
			return;
		}
		
		//��Ӻ��ѳɹ�
		if("accept_add_friend".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			Friend friend_added = (Friend) cmd.getData();
			user.setFriendList(friend_added.getID(), friend_added);  //��ӵ�FriendList
			user.setFriendsNum(user.getFriendsNum()+1);
			friendsUI.reflash_after_addfriend(friend_added);   //��ӵ�������ĺ����б���
			return;		
		}
		
		//���������������ͷ���
		if("back_add_friend".contentEquals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
		}
		
		//����������󱻾ܾ�
		if("refuse_add_friend".contentEquals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
		}
		
		//���ѵ�½
		if("friend_login".equals(cmd.getCmd())) {
			Friend friend = (Friend) cmd.getData();
			int id = friend.getID();
			HashMap<Integer,Friend>friendList = user.getFriendList();
			Friend friend_update = friendList.get(id);
			friend_update.setIP(friend.getIP());
			friend_update.setPort(friend.getPort());
			friend_update.setStatus(true);
			user.updateFriendList1(id, friend_update);  //����FriendList�������ѵ�IP����Ϣ��ӵ���Ӧ��Friend������
			friendList = user.getFriendList();
			//HashMap<Integer,Friend>friendList = user.getFriendList();
			friendsUI.reflash_friendstatus(friend_update);
			
			//�ر���ú��ѵ�������Ϣ����
			Chat chatUI = ChatUIList.getChatUI(friend_update.getFriendEmail());
			if(chatUI!=null) {
				JOptionPane.showMessageDialog(null, friend_update.getFriendName()+"�����ߣ����Խ�������ͨ��");
				chatUI.dispose();
			}
			}
		
		//��������
		if("friend_logout".contentEquals(cmd.getCmd())) {
			int id = (int) cmd.getData();
			user.updateFriendList2(id);
			HashMap<Integer,Friend>friendList = user.getFriendList();
			friendsUI.reflash_friendstatus(friendList.get(id));
			
			//�ر���ú��ѵ�������Ϣ����
			Friend friend_logout = user.getFriendList().get(id);
			Chat chatUI = ChatUIList.getChatUI(friend_logout.getFriendEmail());
			if(chatUI!=null) {
				JOptionPane.showMessageDialog(null, friend_logout.getFriendName()+"�����ߣ��뷢��������Ϣ");
				chatUI.dispose();
			}
		}
		
		//���յ�������Ϣ֪ͨ
		if("offline_message".equals(cmd.getCmd())) {
			ArrayList<Offmessage> off_list = (ArrayList<Offmessage>)cmd.getData();
			for(Offmessage off_mess : off_list) {
				RSA rsa = new RSA();
				DES des = new DES();
				MD5 md5 = new MD5();
				String des_key = off_mess.getDesKey();
				String message = null;
				String mess_md5 = null;
				String my_mess_md5 = null;
				
				try {
					des_key = rsa.decode(des_key, user.getPriKey());
					message = des.decryptDES(off_mess.getMessage(), des_key);
					mess_md5 = des.decryptDES(off_mess.getMD5(), des_key);
					my_mess_md5 = md5.getMD5(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(my_mess_md5.equals(mess_md5)==false) {
					off_mess.setMessage(message + "��ע�⣬������Ϣ���۸�");
				}
				else {
					off_mess.setMessage(message);
				}
			}
			//������Ϣ��������֤����
			
			//���������Ϣ
			for(Offmessage off_mess : off_list) {
				String tem = null;
				tem = off_mess.getDate() + "\n" + off_mess.getSender() + "\n" + off_mess.getMessage() + "\n";
			}
			OfflineMessage off = new OfflineMessage(off_list);
		}
		
		//��ɾ������
		if("delete_friend".equals(cmd.getCmd())) {
			friendsUI.delete_friends((int)cmd.getData());
			user.getFriendList().remove((int)cmd.getData());
		}
		return;
	}
	
}

