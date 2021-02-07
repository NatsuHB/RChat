package Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
*消息类，格式化发送的消息
*/
public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String message; //消息内容
	private String dateString;  //时间戳
	private String md5;  //消息MD5
	public Message(String message) {
		this.message = message;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss"); //生成时间戳
		dateString = dateFormat.format(date);
	}
	
	public void setMD5(String md5) {
		this.md5 = md5;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDate() {
		return dateString;
	}
	
	public String getMD5() {
		return md5;
	}

}
