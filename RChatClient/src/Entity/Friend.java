package Entity;

import java.io.Serializable;
/**
*朋友类，包含了朋友的基本信息
*/
public class Friend implements Serializable{
	private static final long serialVersionUID = 1L;
	private String friend_name;  //昵称
	private String friend_email;  //邮箱
	private String pubKey;   //公钥
	private boolean status = false;  //在线状态
	private String IP;   //IP地址
	private int port;    //端口号
	private int id;   //id,是数据库存储时的唯一标识，构建好友列表时使用
	
	public Friend(String friend_name, String friend_email) {
		this.friend_name = friend_name;
		this.friend_email = friend_email;
	}
	
	public String getFriendName() {
		return friend_name;
	}
	
	public String getFriendEmail() {
		return friend_email;
	}
	
	public String getIP() {
		return IP;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public int getID() {
		return id;
	}
	
	public String getPubKey() {
		return pubKey;
	}
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	
	public void setIP(String IP) {
		this.IP = IP;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	public void setID(int id) {
		this.id = id;
	}
}
