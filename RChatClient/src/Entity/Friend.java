package Entity;

import java.io.Serializable;
/**
*�����࣬���������ѵĻ�����Ϣ
*/
public class Friend implements Serializable{
	private static final long serialVersionUID = 1L;
	private String friend_name;  //�ǳ�
	private String friend_email;  //����
	private String pubKey;   //��Կ
	private boolean status = false;  //����״̬
	private String IP;   //IP��ַ
	private int port;    //�˿ں�
	private int id;   //id,�����ݿ�洢ʱ��Ψһ��ʶ�����������б�ʱʹ��
	
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
