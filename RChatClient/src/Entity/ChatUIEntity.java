package Entity;

import Frame.Chat;

/**
*�������ĸ����࣬���㽫���촰�ڼ��뵽ChatUIList��
*/
public class ChatUIEntity {
	private Chat chatUI;
	private String name;
	
	public ChatUIEntity() {
		super();
	}
	
	public ChatUIEntity(Chat chatUI, String name) {
		super();
		this.chatUI = chatUI;
		this.name = name;
	}
	
	public Chat getChatUI() {
		return chatUI;
	}
	
	public void setChatUI(Chat chatUI) {
		this.chatUI = chatUI;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

