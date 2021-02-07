package Entity;

import java.io.Serializable;
/**
*������Ϣ�࣬�̳�Message����������������
*/
public class Offmessage extends Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String deskey;  //DES��Կ��������Ϣ�����ܹ���DES��Կ�洢�����ݿ�
	private String sender;  //������Ϣ������

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