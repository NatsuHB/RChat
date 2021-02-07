package Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
*��Ϣ�࣬��ʽ�����͵���Ϣ
*/
public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String message; //��Ϣ����
	private String dateString;  //ʱ���
	private String md5;  //��ϢMD5
	public Message(String message) {
		this.message = message;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss"); //����ʱ���
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
