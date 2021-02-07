package _Service;

//import java.io.IOException;
//import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
//import java.util.ArrayList;
import java.net.Socket;

import Entity.User;
import Entity.Friend;
import _Util.*;

public class UserService {
	private Friend frdSender = null;
	private Friend frdReceiver = null;
	
	
	public void setFrd1(Friend frd1) {
		this.frdSender = frd1;
	}
	
	public void setFrd2(Friend frd2) {
		this.frdReceiver = frd2;
	}
	
	public Friend getFrd1() {
		return frdSender;
	}
	
	public Friend getFrd2() {
		return frdReceiver;
	}
	
	//login��֤�˺�����
	public boolean checkUser(User user) {
		PreparedStatement stmt = null; //PreparedStatement������ִ��SQL��ѯ����API֮һ
		Connection conn = null; //���ض����ݿ�����ӣ��Ự������������������ִ�� SQL ��䲢���ؽ��
		ResultSet rs = null; //�������в�ѯ������ص�һ�ֶ��󣬿���˵�������һ���洢��ѯ����Ķ��󣬵��ǽ���������������д洢�Ĺ��ܣ���ͬʱ�����в������ݵĹ��ܣ�������ɶ����ݵĸ��µ�
		conn = DBHelper.getConnection();
		String sql = "select * from users_table where email =? and user_pwd =?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUseremail());
			stmt.setString(2, user.getUserpwd());
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//ע��ʱ��֤�����Ƿ����
	public boolean isExist(User user) {
		PreparedStatement stmt = null; //PreparedStatement������ִ��SQL��ѯ����API֮һ
		Connection conn = null; //���ض����ݿ�����ӣ��Ự������������������ִ�� SQL ��䲢���ؽ��
		ResultSet rs = null; //�������в�ѯ������ص�һ�ֶ��󣬿���˵�������һ���洢��ѯ����Ķ��󣬵��ǽ���������������д洢�Ĺ��ܣ���ͬʱ�����в������ݵĹ��ܣ�������ɶ����ݵĸ��µ�
		conn = DBHelper.getConnection();
		String sql = "select * from users_table where email =?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUseremail());
			rs = stmt.executeQuery();
			if(rs.next()) {
				return true;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//��¼����ͻ��˷���������б�
	public User getFriendsList(User user) {
		PreparedStatement stmt1 = null; 
		PreparedStatement stmt2 = null; 
		PreparedStatement stmt3 = null; 
		Connection conn = null; 
		ResultSet rs = null; 
		ResultSet rs_ = null; 
		Friend aFriend = null;
		conn = DBHelper.getConnection();
		String sql1 = "select id,user_name,prikey from users_table where email=?";
		String sql2 = "select friend_id from friends_table where user_id=?";
		String sql3 = "select user_name,email,pubKey from users_table where id=?";
		String sql3_ = sql3;
		
		try {
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, user.getUseremail());
			rs = stmt1.executeQuery();
			rs.next();	//�鵽��ǰ�û���Ϣ
			
			int id = rs.getInt(1);
			String user_name = rs.getString(2);
			String priKey = rs.getString(3);
			user.setUsername(user_name);
			user.setUserId(id);	//���õ�ǰ��¼�û���id
			user.setPriKey(priKey);	//���õ�ǰ��¼�û���priKey

			stmt2 = conn.prepareStatement(sql2);
			stmt2.setInt(1, id);
			rs = stmt2.executeQuery();
			
			int count = 0;
			while(rs.next()) {
				id = rs.getInt(1);
				stmt3 = conn.prepareStatement(sql3_);
				stmt3.setInt(1, id);
				rs_ = stmt3.executeQuery();
				rs_.next();

				boolean statusFlag = (SocketList.getSocket(rs_.getString(2)) != null);
				aFriend = new Friend(rs_.getString(1), rs_.getString(2));
				aFriend.setPubKey(rs_.getString(3));
				aFriend.setStatus(statusFlag);
				aFriend.setId(id);
				user.setFriendList(id, aFriend);
				if(statusFlag) {
					Socket socket = SocketList.getSocket(rs_.getString(2));
					aFriend.setIP(socket.getInetAddress().getHostAddress());
					aFriend.setPort(socket.getPort());
				}
				count++;
			}
			user.setFriendsNum(count);
			
			return user;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(rs_ != null) {
					rs_.close();
				}
				if(stmt1 != null) {
					stmt1.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	/**
	//�ǳ�����ÿ�����ߺ��ѵ�socket�㲥sender_logout
	public void logOut(CommandTranser cmd) {
		ObjectOutputStream oos1 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rs_ = null;
		conn = DBHelper.getConnection();
		String sql1 = "select id from users_table where email=" + cmd.getSender();
		String sql2 = "select friend_id from friends_table where user_id=";
		String sql3 = "select email from users_table where id=";
		String sql3_ = sql3;
		
		try {
			stmt1 = conn.prepareStatement(sql1);
			rs = stmt1.executeQuery();
			int id = rs.getInt(1);
			User user = (User)cmd.getData();
			user.setUserId(id);
			cmd.setData(user);
			
			sql2 = sql2+Integer.toString(id);
			stmt2 = conn.prepareStatement(sql2);
			rs = stmt2.executeQuery();
			while(rs.next()) {
				id = rs.getInt(1);
				sql3_ = sql3 + Integer.toString(id);
				stmt3 = conn.prepareStatement(sql3_);
				rs_ = stmt3.executeQuery();
				
				Socket sock = SocketList.getSocket(rs_.getString(1));	
				if(sock!=null) {
					try {
						oos1 = new ObjectOutputStream(sock.getOutputStream());
						oos1.writeObject(cmd);	
					}catch(IOException e) {
						sock = null;
					}
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt1 != null) {
					stmt1.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	*/
	
	//�û�ע��
	public boolean registerUser(User user) {
		PreparedStatement stmt1 = null; //PreparedStatement������ִ��SQL��ѯ����API֮һ
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		Connection conn = null; 
		ResultSet rs = null; 
		int insertFlag = 0;
		conn = DBHelper.getConnection();
		//String sql = "select * from users_table where email =?";
		String insertusersql = "insert into users_table (id, user_name, user_pwd, email, pubkey, prikey) values(?, ?, ?, ?, ?, ?)";
		
		try {/**
			stmt1 = conn.prepareStatement(sql);
			stmt1.setString(1, user.getUseremail());
			rs = stmt1.executeQuery();
			if(rs.next()) {
				System.out.println("���û��Ѵ���" + user.getUseremail() + "***");
				//�û��ѱ�ע��
				return false;
			}
			else {
			System.out.println("���û�������" + user.getUseremail() + "***");*/
			//���û����������
			stmt2 = conn.prepareStatement(insertusersql);
			stmt2.setNull(1, Types.INTEGER);
			stmt2.setString(2, user.getUsername());
			stmt2.setString(3, user.getUserpwd());
			stmt2.setString(4, user.getUseremail());
			stmt2.setString(5, user.getPubKey());
			stmt2.setString(6, user.getPriKey());
			insertFlag = stmt2.executeUpdate();
			System.out.println("����в�������" + user.getUseremail() + "***" + insertFlag);
			
			if(insertFlag == 1) {
				return true;
			}
				
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt1 != null) {
					stmt1.close();
				}
				if(stmt2 != null) {
					stmt2.close();
				}
				if(stmt3 != null) {
					stmt3.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//��Ӻ���
	public boolean addFriend(String receiver, String sender) {
		Friend frd_receiver = new Friend();
		Friend frd_sender = new Friend();
		PreparedStatement stmt3 = null; 
		PreparedStatement stmt4 = null; 
		PreparedStatement stmt5 = null;
		Connection conn = null; 
		ResultSet rs3 = null; 
		ResultSet rs4 = null; 
		ResultSet rs5 = null;
		int senderId = 0;
		int receiverId = 0;
		int updateResult1 = 0;
		int updateResult2 = 0;
		String senderName = null;
		String receiverName = null;
		
		conn = DBHelper.getConnection();
		String sql3 = "select id,user_name from users_table where email=?";
		String sql4 = "select id,user_name from users_table where email=?";
		String sql5 = "insert into friends_table (rela_id, user_id, friend_id) value(?,?,?)";
		try {
			stmt3 = conn.prepareStatement(sql3);
			stmt3.setString(1, sender);
			rs3 = stmt3.executeQuery();
			rs3.next();
			senderId = rs3.getInt(1);
			senderName = rs3.getString(2);
			frd_sender.setId(senderId);
			frd_sender.setStatus(true);
			frd_sender.setFriendEmail(sender);
			frd_sender.setFriendName(senderName);
			Socket socket = SocketList.getSocket(sender);
			frd_sender.setIP(socket.getInetAddress().getHostAddress());
			frd_sender.setPort(socket.getPort());
			setFrd1(frd_sender);
			
			stmt4 = conn.prepareStatement(sql4);
			stmt4.setString(1, receiver);
			rs4 = stmt4.executeQuery();
			rs4.next();
			receiverId = rs4.getInt(1);
			receiverName = rs4.getString(2);
			frd_receiver.setId(receiverId);
			frd_receiver.setStatus(true);
			frd_receiver.setFriendEmail(receiver);
			frd_receiver.setFriendName(receiverName);
			socket = SocketList.getSocket(receiver);
			frd_receiver.setIP(socket.getInetAddress().getHostAddress());
			frd_receiver.setPort(socket.getPort());
			setFrd2(frd_receiver);
			
			stmt5 = conn.prepareStatement(sql5);
			stmt5.setNull(1, Types.INTEGER);
			stmt5.setInt(2, receiverId);
			stmt5.setInt(3, senderId);
			updateResult1 = stmt5.executeUpdate();
			
			stmt5 = conn.prepareStatement(sql5);
			stmt5.setNull(1, Types.INTEGER);
			stmt5.setInt(2, senderId);
			stmt5.setInt(3, receiverId);
			updateResult2 = stmt5.executeUpdate();
			
			if(updateResult1 == 1 && updateResult2 == 1) {
				return true;
			}
			else {
				//ϣ���ܲ��룬 ������벻�ɹ��Ļ���Ӧ�ý�����ɹ���ɾ��....���ﲻ��������
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(stmt3 != null) {
					stmt3.close();
				}
				if(stmt4 != null) {
					stmt4.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//�޸���Ϣ
	public boolean changeInfo(User user) {
		return false;
	}
	
	//�޸����� ��������
	public boolean changePassword(User user) {
		PreparedStatement stmt1 = null; //PreparedStatement������ִ��SQL��ѯ����API֮һ
		PreparedStatement stmt2 = null; //PreparedStatement������ִ��SQL��ѯ����API֮һ
		Connection conn = null; //���ض����ݿ�����ӣ��Ự������������������ִ�� SQL ��䲢���ؽ��
		ResultSet rs = null; //�������в�ѯ������ص�һ�ֶ��󣬿���˵�������һ���洢��ѯ����Ķ��󣬵��ǽ���������������д洢�Ĺ��ܣ���ͬʱ�����в������ݵĹ��ܣ�������ɶ����ݵĸ��µ�
		int updateFlag = 0;
		conn = DBHelper.getConnection();
		//String sql = "select * from tb_user where user_question =? and user_ans =?";
		String updatesql = "update tb_user set user_pwd =? where user_name = ?";
		
		try {
			//stmt1 = conn.prepareStatement(sql);
			//stmt1.setString(1, user.getUserQuestion());
			//stmt1.setString(2, user.getUserAnswer());
			//rs = stmt1.executeQuery();
			
			//if(rs.next()) {
				
				stmt2 = conn.prepareStatement(updatesql);
				stmt2.setString(1, user.getUserpwd());
				stmt2.setString(2, user.getUsername());
				updateFlag = stmt2.executeUpdate();
				if(updateFlag == 1)
				  return true;
			//}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt1 != null) {
					stmt1.close();
				}
				if(stmt2 != null) {
					stmt2.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//����û��������Ϣ
	public User getUser(User user) {
		PreparedStatement stmt1 = null; 
		PreparedStatement stmt2 = null; 
		Connection conn = null; 
		ResultSet rs = null; 
		conn = DBHelper.getConnection();
		String sql = "select * from tb_user where user_name =?";
		try {
			
			stmt1 = conn.prepareStatement(sql);
			stmt1.setString(1, user.getUsername());
			rs = stmt1.executeQuery();
			if(rs.next()) {
				user.setUsername(rs.getString("user_name"));
				return user; 
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt1 != null) {
					stmt1.close();
				}
				if(stmt2 != null) {
					stmt2.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}

