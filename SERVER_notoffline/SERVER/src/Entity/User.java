package Entity;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String user_email;
	private String user_pwd;
	private String user_name;
	private String pubKey;
	private String priKey;
	private int friends_num = 0;
	private int user_id = 0;
	private HashMap<Integer, Friend> friendList = new HashMap<Integer, Friend>();
	
	public User() {}
	
	public User(String username, String password, String email) {
		this.user_name = username;
		this.user_email = email;
		this.user_pwd = password;
	}
	
	public String getUsername() {
		return user_name;
	}
	
	public String getUserpwd() {
		return user_pwd;
	}
	
	public String getUseremail() {
		return user_email;
	}
	
	public int getUserId(){
		return user_id;
	}
	
	public String getPubKey() {
		return pubKey;
	}
	
	public String getPriKey() {
		return priKey;
	}
	
	public int getFriendsNum() {
		return friends_num;
	}
	
	public HashMap<Integer, Friend> getFriendList(){
		return friendList;
	}
	
	public String setUsername(String username) {
		return this.user_name = username;
	}
	
	public String setUserpwd(String password) {
		return this.user_pwd = password;
	}
	
	public String setUseremail(String email) {
		return this.user_email = email;
	}
	
	public void setFriendsNum(int friends_num) {
		this.friends_num = friends_num;
	}
	
	public void setFriendList(int id, Friend friend) {
		friendList.put(id, friend);
	}
	
	public void setUserId(int id) {
		this.user_id = id;
	}
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}
}
