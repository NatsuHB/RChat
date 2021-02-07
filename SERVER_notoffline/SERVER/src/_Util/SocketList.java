package _Util;

import java.net.Socket;
import java.util.HashMap;

import Entity.SocketEntity;

public class SocketList {
	private static HashMap<String, Socket> map = new HashMap<String, Socket>();
	
	public static void addSocket(SocketEntity socketEntity) {
		map.put(socketEntity.getEmail(), socketEntity.getSocket());	
	}
	
	//通过email返回在线socket 
	public static Socket getSocket(String email) {
		return map.get(email);
	}
	
	public static HashMap<String, Socket> getMap(){
		return map;
	}
	public static void deleteSocket(String email) {
		if(map.get(email) != null) {
			map.remove(email);
		}
		return;
	}
}
