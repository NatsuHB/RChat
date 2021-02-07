package _Util;

import java.util.HashMap;

import Entity.ChatUIEntity;
import Frame.Chat;

/**
*  窗口控制器 每个聊天界面都在这里"注册"
*/
public class ChatUIList {
	private static HashMap<String, Chat> map = new HashMap<String, Chat>();
	
	//向map里面“注册”
	public static void addChatUI(ChatUIEntity chatUIEntity) {
		map.put(chatUIEntity.getName(), chatUIEntity.getChatUI());	
	}
	
	//关闭窗口后要删除
	public static void deletChatUI(String chatUIName) {
		
		//删除之前查看是否有这个窗口, 防止出错
		if(map.get(chatUIName) != null) {
			map.remove(chatUIName);
		}
		
	}
	
	//通过昵称返回窗口
	public static Chat getChatUI(String name) {
		return map.get(name);
	}
	
	//返回窗口列表
	public static HashMap<String, Chat>  getAllChat(){
		return map;
	}
}

