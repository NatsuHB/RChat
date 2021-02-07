package _Util;

import java.util.HashMap;

import Entity.ChatUIEntity;
import Frame.Chat;

/**
*  ���ڿ����� ÿ��������涼������"ע��"
*/
public class ChatUIList {
	private static HashMap<String, Chat> map = new HashMap<String, Chat>();
	
	//��map���桰ע�ᡱ
	public static void addChatUI(ChatUIEntity chatUIEntity) {
		map.put(chatUIEntity.getName(), chatUIEntity.getChatUI());	
	}
	
	//�رմ��ں�Ҫɾ��
	public static void deletChatUI(String chatUIName) {
		
		//ɾ��֮ǰ�鿴�Ƿ����������, ��ֹ����
		if(map.get(chatUIName) != null) {
			map.remove(chatUIName);
		}
		
	}
	
	//ͨ���ǳƷ��ش���
	public static Chat getChatUI(String name) {
		return map.get(name);
	}
	
	//���ش����б�
	public static HashMap<String, Chat>  getAllChat(){
		return map;
	}
}

