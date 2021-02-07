package Entity;

import java.io.Serializable;
/**
*离线消息类，继承Message，增加了两个变量
*/
public class Offmessage extends Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String deskey;  //DES密钥，离线消息将加密过的DES密钥存储到数据库
	private String sender;  //离线信息发送者

	public Offmessage(String message) {
		super(message);
	}
	
	public void setDesKey(String deskey){
		this.deskey = deskey;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDesKey(){
		return deskey;
	}
	
	public String getSender() {
		return sender;
	}
}