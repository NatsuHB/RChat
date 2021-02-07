package Entity;
import java.io.Serializable;
import java.util.HashMap;
/**
*用户类，包含了该用户的基本信息
*/
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String user_email;		//邮箱
	private String user_pwd;   		//密码（登录时提交表单，是经过MD5处理的）
	private String user_name;  		//昵称
	private int user_id;      		//User的标识
	private int friends_num = 0;	//朋友数量
	private String pubKey;  		//私钥，本地存储
	private String priKey;  		//公钥
	private HashMap<Integer, Friend> friendList = new HashMap<Integer, Friend>();
	
	public User() {
		
	}
	
	public User(String email, String password) {
		this.user_email = email;
		this.user_pwd = password;
	}
	
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
	
	public void setUserId(int id) {
		this.user_id = id;
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
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
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
	
	public int getFriendsNum() {
		return friends_num;
	}
	public void setFriendsNum(int friends_num) {
		this.friends_num = friends_num;
	}
	
	public void setFriendList(int id, Friend friend) {
		friendList.put(id, friend);
	}
	
	public HashMap<Integer, Friend> getFriendList(){
		return friendList;
	}
	
	public void updateFriendList1(int id, Friend friend) {
		this.friendList.replace(id, friend);
	}
	
	public void updateFriendList2(int id) {
		Friend friend = friendList.get(id);
		friend.setStatus(false);
		this.friendList.replace(id, friend);
	}
}

