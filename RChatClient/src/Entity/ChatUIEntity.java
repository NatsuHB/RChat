package Entity;

import Frame.Chat;

/**
*聊天界面的辅助类，方便将聊天窗口加入到ChatUIList中
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

