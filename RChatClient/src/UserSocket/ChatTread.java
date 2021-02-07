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
* 接收服务器的指令，进行相应处理
* 1.request_add_friend,添加好友请求，选择是否接受好友请求，向服务器返回相关命令
* 2.accept_add_friend，接受好友请求，添加成功，更新好友列表
* 3.back_add_friend，服务器成功发送好友请求的标识
* 4.refuse_add_friend，拒绝好友请求，添加失败
* 5.friend_login，好友上线，更新好友在线状态，关闭离线聊天窗口
* 6.friend_logout，好友离线，更新好友在线状态，关闭在线聊天窗口
* 7.offline_message，离线消息，打印
* 8.delete_friend，删除好友，更新好友列表
*/
public class ChatTread extends Thread{
	private Client client;
	private boolean isOnline = true;
	private User user; //如果同意好友请求， 则刷新好友列表
	private MainPage friendsUI; //刷新好友列表用
	
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
	
	//run()方法是不需要用户来调用的，当通过start方法启动一个线程之后，当线程获得了CPU执行时间，
	//便进入run方法体去执行具体的任务。继承Thread类必须重写run方法，在run方法中定义具体要执行的任务
	@Override
	public void run() {
		if(!isOnline) {
			JOptionPane.showMessageDialog(null,  "unbelievable ！！！");
			return;
		}
		while(isOnline) {
			
			CommandTranser cmd = client.getData();
			//与服务器端相同处理接收到的消息(命令)
			//这里处理来自服务器的消息(命令)
			if(cmd != null) {
				
			 execute(cmd);	
			}
		}
	}
	
	//处理消息(命令)
	private void execute(CommandTranser cmd) {
		
		//添加好友请求
		if("request_add_friend".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			String sendername = cmd.getSender();
			int flag = JOptionPane.showConfirmDialog(null, "是否同意" + sendername + "的好友请求", "好友请求", JOptionPane.YES_NO_OPTION);
			if(flag == 0) {
				cmd.setCmd("accept_add_friend");  //接受好友申请
			} else {
				cmd.setCmd("refuse_add_friend");	//拒绝好友申请		
			}
			client.sendData(cmd);
			return;
		}
		
		//添加好友成功
		if("accept_add_friend".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			Friend friend_added = (Friend) cmd.getData();
			user.setFriendList(friend_added.getID(), friend_added);  //添加到FriendList
			user.setFriendsNum(user.getFriendsNum()+1);
			friendsUI.reflash_after_addfriend(friend_added);   //添加到主界面的好友列表中
			return;		
		}
		
		//服务器好友请求发送反馈
		if("back_add_friend".contentEquals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
		}
		
		//好友添加请求被拒绝
		if("refuse_add_friend".contentEquals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
		}
		
		//好友登陆
		if("friend_login".equals(cmd.getCmd())) {
			Friend friend = (Friend) cmd.getData();
			int id = friend.getID();
			HashMap<Integer,Friend>friendList = user.getFriendList();
			Friend friend_update = friendList.get(id);
			friend_update.setIP(friend.getIP());
			friend_update.setPort(friend.getPort());
			friend_update.setStatus(true);
			user.updateFriendList1(id, friend_update);  //更新FriendList，将好友的IP等信息添加到对应的Friend对象中
			friendList = user.getFriendList();
			//HashMap<Integer,Friend>friendList = user.getFriendList();
			friendsUI.reflash_friendstatus(friend_update);
			
			//关闭与该好友的离线消息窗口
			Chat chatUI = ChatUIList.getChatUI(friend_update.getFriendEmail());
			if(chatUI!=null) {
				JOptionPane.showMessageDialog(null, friend_update.getFriendName()+"已上线，可以进行在线通信");
				chatUI.dispose();
			}
			}
		
		//好友离线
		if("friend_logout".contentEquals(cmd.getCmd())) {
			int id = (int) cmd.getData();
			user.updateFriendList2(id);
			HashMap<Integer,Friend>friendList = user.getFriendList();
			friendsUI.reflash_friendstatus(friendList.get(id));
			
			//关闭与该好友的在线消息窗口
			Friend friend_logout = user.getFriendList().get(id);
			Chat chatUI = ChatUIList.getChatUI(friend_logout.getFriendEmail());
			if(chatUI!=null) {
				JOptionPane.showMessageDialog(null, friend_logout.getFriendName()+"已离线，请发送离线消息");
				chatUI.dispose();
			}
		}
		
		//接收到离线信息通知
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
					off_mess.setMessage(message + "请注意，这条信息被篡改");
				}
				else {
					off_mess.setMessage(message);
				}
			}
			//离线消息解密与验证结束
			
			//输出离线消息
			for(Offmessage off_mess : off_list) {
				String tem = null;
				tem = off_mess.getDate() + "\n" + off_mess.getSender() + "\n" + off_mess.getMessage() + "\n";
			}
			OfflineMessage off = new OfflineMessage(off_list);
		}
		
		//被删除好友
		if("delete_friend".equals(cmd.getCmd())) {
			friendsUI.delete_friends((int)cmd.getData());
			user.getFriendList().remove((int)cmd.getData());
		}
		return;
	}
	
}

