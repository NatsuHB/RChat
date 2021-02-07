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
				
				//执行命令来自客户端的请求
				cmd = execute(cmd);
				
				//以下为返回服务器执行之后的结果
				//消息对话请求，服务器将sender发来的消息发送给receiver
				if("message".equals(cmd.getCmd())) {
					//如果 msg.ifFlag即 服务器处理成功 可以向朋友发送信息 如果服务器处理信息失败 信息发送给发送者本人
					if(cmd.isFlag()) {
						//System.out.println("对方在线");
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					} else {
						//System.out.println("对方未在线");
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
				//登录请求 将数据发送给sender
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
						System.out.println("登录消息发给"+value.getFriendEmail());
						System.out.println("登录消息发给"+value.getFriendName());
						if(sock!=null){
							oos3 = new ObjectOutputStream(sock.getOutputStream());
							oos3.writeObject(newcmd);
							System.out.println("登录请求已经发过去了");
						}
					}
					System.out.println("登录成功！");
					System.out.println(oos1==null);
				}
				
				//注册请求 将数据发送给sender
				if ("register".equals(cmd.getCmd())) {
					System.out.println("把注册请求发向sender");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//存储密钥请求 将用户数据插入数据库并返回客户端结果
				if("send_key".equals(cmd.getCmd())) {
					System.out.println("告诉sender数据插入成功");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//添加好友请求将数据发送给 receiver
				if ("request_add_friend".equals(cmd.getCmd())) {
					//在线，将请求发给receiver
					String email = (String)cmd.getData();
					CommandTranser newcmd =  new CommandTranser();
					newcmd.setCmd("back_add_friend");
					if(cmd.isFlag()) {
						newcmd.setFlag(true);
						newcmd.setResult("对方收到了您的好友请求");
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
						//不管在不在线都要向发送方提示消息发送成功
						newcmd.setFlag(false);
						newcmd.setResult("当前用户不在线或该用户不存在");
						//oos1 = new ObjectOutputStream(SocketList.getSocket(email).getOutputStream());
						oos3 = new ObjectOutputStream(socket.getOutputStream());
						oos3.writeObject(newcmd);
					}
				}
				
				//同意添加好友请求将数据发送给 receiver和sender
				if ("accept_add_friend".equals(cmd.getCmd())) {
					CommandTranser newcmd =  new CommandTranser();
					newcmd.setReceiver(cmd.getReceiver());
					newcmd.setSender(cmd.getSender());
					newcmd.setCmd("accept_add_friend");
					newcmd.setFlag(cmd.isFlag());
					newcmd.setResult(cmd.getResult());
					newcmd.setData(frdReceiver);
					cmd.setData(frdSender);
					//无论是否成功插入数据库都要将结果反馈，但有可能最初请求的客户下线了
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
				
				//拒绝添加好友请求将数据发送给 receiver
				if ("refuse_add_friend".equals(cmd.getCmd())) {
					//被拒绝方在线
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getSender()).getOutputStream());
					}else { //被拒方不在线则向拒绝方发送消息
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				/**
				//修改资料请求 发送给sender 功能暂未实现
				if ("changeinfo".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//修改密码请求 将数据发送给sender
				if ("changepwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//忘记密码 发送给sender
				if ("forgetpwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}*/
				
				//用户下线
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
					System.out.println("oos1成功发送！");
				}
				if(oos2 != null) {
					oos2.writeObject(cmd);	
					System.out.println("oos2成功发送！");
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
		//登录请求
		if("login".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.checkUser(user));
			
			//如果登录成功，将该客户端加入已经连接成功的map集合里面 并且开启此用户的接受线程
			if(cmd.isFlag()) {
				// 将该线程加入连接成功的map集合
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setEmail(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				
				//从数据库获取其好友列表并将其好友列表发送至客户端
				cmd.setData(userservice.getFriendsList(user));
				cmd.setResult("登录成功");
			} else {
				cmd.setResult("密码错误或用户未注册");
			}
		}
		
		//注册请求
		if("register".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(!userservice.isExist(user));
			//如果注册成功
			if(cmd.isFlag()) {
				/**
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//刚注册的肯定没有好友 */
				cmd.setResult("允许注册，请发送用户公私钥");
			} else {
				cmd.setResult("注册失败，该邮箱已被注册");
			}
		}
		
		//存储用户信息
		if("send_key".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.registerUser(user));
			//如果存储成功
			if(cmd.isFlag()) {
				/**
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//刚注册的肯定没有好友 */
				cmd.setResult("注册时存储用户信息成功");
			} else {
				cmd.setResult("注册存储用户信息失败");
			}
		}
		
		/**
		//修改资料请求 功能暂未实现
		if("changeInfo".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changeInfo(user));
			if(cmd.isFlag()) {
				cmd.setResult("修改信息成功");
			} else {
				cmd.setResult("修改信息失败");
			}
		}*/
		
		//添加好友
		if("request_add_friend".equals(cmd.getCmd())) {
			//检查用户是否在线
			String email = (String)cmd.getData();
			if(SocketList.getSocket(email) != null) {
				cmd.setFlag(true);
				cmd.setResult("对方收到了您的好友请求");
			} else {
				cmd.setFlag(false);
				cmd.setResult("当前用户不在线或该用户不存在");
			}
		}
		
		//同意添加好友请求
		if("accept_add_friend".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			cmd.setFlag(userservice.addFriend(cmd.getReceiver(), cmd.getSender()));
			frdSender = userservice.getFrd1();
			frdReceiver = userservice.getFrd2();
			if(cmd.isFlag()) {
				cmd.setResult("好友添加成功");
			} else {
				cmd.setResult("服务器故障导致添加好友失败或者您们已经为好友");
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
		
		//拒绝添加好友
		if("refuse_add_friend".equals(cmd.getCmd())) {
			//检查是否在线
			if(SocketList.getSocket(cmd.getSender()) != null) {
				cmd.setFlag(true);
				cmd.setResult("您被 " + cmd.getReceiver() +  " 拒绝了");
			} else {
				cmd.setFlag(false);
				cmd.setResult("对方不在线不知道你拒绝了他的好友请求");
			}
		}
		
		//发送消息指令
		if("message".equals(cmd.getCmd())) {
			//检查是否在线
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				//cmd.setResult("对方成功收到您的消息");
			} else {
				cmd.setFlag(false);
				cmd.setResult("当前用户不在线");
			}
		}
		
		/**
		if("WordChat".equals(cmd.getCmd())) {
			cmd.setFlag(true);
		}
		//忘记密码指令 这里最后要讲用户的问题和答案返回
		if("forgetpwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			user = userservice.getUser(user);
			//如果用户存在
			if(user != null ) {
				cmd.setResult("查询成功");
				cmd.setData(user);
				cmd.setFlag(true);
			} else {
				cmd.setResult("用户可能不存在");
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
				cmd.setResult("修改密码成功");
			}else {
				cmd.setResult("修改密码失败");
			}
		}*/
		
		if("logout".equals(cmd.getCmd())) {
			//UserService userservice = new UserService();
			SocketList.deleteSocket(cmd.getSender());
			System.out.println("登出请求！");
			//cmd.setCmd("sender_logout");
			//userservice.logOut(cmd);
		}
		
		return cmd;
	}
}
