package Frame;

import java.awt.Color;
import java.awt.Dimension;
//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Entity.ChatUIEntity;
import Entity.Friend;
import Entity.User;
import Security.DES;
import Security.RSA;
//import Interface.startServerThread;
import UserSocket.Client;
import UserSocket.ServerThread;
import UserSocket.Service;
import UserSocket.fileService;
import _Util.ChatUIList;
import _Util.CommandTranser;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;
/**
*主界面，主要功能有
*1.显示好友列表和好友在线状态，并动态刷新在线状态
*2.双击好友栏弹出聊天窗口
*3.右键好友栏删除好友
*4.添加好友，跳转到添加好友界面
*5.搜索栏，输入好友邮箱直接弹出对应好友的聊天窗口
*6.登录时显示离线消息
*/
public class MainPage implements KeyListener, ActionListener, FocusListener {

	private JFrame frame;
	private static User owner;
	private static Client client;
	private JLabel nickname;
	private JTextField searchFriend;
	private JButton search_button;
	private JLabel head_label;
	private JLabel searchlabel;
	private JPanel Friends;
	private JScrollPane friends;
	private JButton logout;
	private int friends_num;
	private int panel_target=0;
	private JButton search_for_newfriend;
	private JButton personalinformation;
	private HashMap<Integer,JLabel> labelfriendList = new HashMap<Integer,JLabel>();//读入好友列表
	private HashMap<Integer,Friend> friendList;//读入好友列表
	private JLabel friends_list_label = new JLabel();
	private JPanel friends_list_panel;
	private HashMap<JLabel,JPanel> friends_list = new HashMap<JLabel,JPanel>();
	
	public MainPage(User owner,Client client) {
		MainPage.owner=owner;
		MainPage.client=client;
		friendList = new HashMap<Integer,Friend>(owner.getFriendList());
		friends_num = owner.getFriendsNum();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		new startServerThread(owner).start();   //创建socket监听，处理聊天请求，每一个聊天窗口都对应一个线程
		new startFileServerThread(owner).start();  //处理文件传输，每个文件接收都对应一个线程
		//建立页面，设置页面大小、出现位置等参数
		frame = new JFrame();
		frame.setTitle("R.Chat");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		//设置页面大小不可变
		frame.setResizable(false);
		frame.setBounds((mywidth-314)/2, (myheight-671)/2, 320, 700);
		//设置关闭此页面即关闭整个线程
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		//添加一层JPanel，设置页面背景
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/mainpage.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		panel.setBounds(0, 0, 314, 671);
		panel.setOpaque(false);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		ImageIcon head = new ImageIcon("image/friendsui/3.jpg");//绘制用户头像
		head_label = new JLabel(head);
		head_label.setBounds(20, 20, 80, 80);//设置用户头像位置与大小
		head_label.setFocusable(true);
		panel.add(head_label);
		
		nickname = new JLabel(owner.getUsername());//用户昵称
		nickname.setBounds(140, 30, 130, 25);
		nickname.setFont(new Font("楷书", Font.BOLD, 22));//设置文字字体
		nickname.setOpaque(false);
		nickname.setForeground(Color.WHITE);//设置文字颜色
		panel.add(nickname);
		
		//添加一个JLabel，容纳查找已添加好友的功能组件，查找到好友后直接进入与该好友的聊天窗口
		searchlabel = new JLabel();
		searchlabel.setBounds(0, 120, 314, 30);
		panel.add(searchlabel);
		
		//加入文本框，此处可输入好友邮箱以查找已添加的好友，并添加事件监听，在输入邮箱后按压回车即可查找已添加好友
		searchFriend = new JTextField("输入邮箱以查找好友");//查找好友文本框
		searchFriend.setHorizontalAlignment(SwingConstants.CENTER);
		searchFriend.setForeground(new Color(255, 255, 255));
		searchFriend.setForeground(Color.WHITE);
		searchFriend.setBounds(0, 0, 250, 30);
		searchFriend.setBackground(new Color(0,0,0,0));
		searchFriend.addFocusListener(this);
		searchFriend.addKeyListener(this);
		searchlabel.add(searchFriend);
		searchFriend.setColumns(10);
		
		//加入搜索按钮，在searchFriend文本框中输入好友邮箱后点击该按钮即可搜索已添加好友的列表中是否有该好友
		search_button = new JButton("搜索");//搜索查找好友按钮
		search_button.setHorizontalAlignment(SwingConstants.LEFT);
		search_button.setBackground(new Color(70, 130, 180));
		search_button.setForeground(Color.WHITE);
		search_button.setBorderPainted(false);
		search_button.setBounds(250, 0, 64, 30);
		search_button.addActionListener(this);
		searchlabel.add(search_button);
		
		//建立容纳好友列表的容器
		Friends = new JPanel(){
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/friendlist.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		Friends.setBackground(new Color(255, 255, 255));
		Friends.setBounds(20, 160, 294, 480);
		Friends.setOpaque(false);
		//设置布局模式为BoxLayout使每个好友按列排列
		Friends.setLayout(new BoxLayout(Friends,BoxLayout.Y_AXIS));
		//建立一个JScrollPane将Friends嵌入进入，并设置在需要时可显示竖状滚动条，不提供横向滚动条
		friends = new JScrollPane(Friends);
		friends.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//设置好友列表边框
		friends.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 191, 255), new Color(173, 216, 230), new Color(70, 130, 180), new Color(100, 149, 237)));
		friends.setBounds(20, 160, 294, 480);
		friends.setOpaque(false);
		panel.add(friends);

		//退出登录按钮，加入事件监听，点击该按钮即向服务器发送登出消息
		logout = new JButton("退出登录");
		logout.setForeground(new Color(255, 255, 255));
		logout.setContentAreaFilled(false);
		logout.setBounds(207, 638, 97, 23);
		logout.addActionListener(this);
		panel.add(logout);

		//添加好友，加入事件监听，点击该按钮弹出添加好友页面
		search_for_newfriend = new JButton("添加好友");
		search_for_newfriend.setForeground(new Color(255, 255, 255));
		search_for_newfriend.setContentAreaFilled(false);
		search_for_newfriend.setBounds(110, 638, 87, 23);
		search_for_newfriend.addActionListener(this);
		panel.add(search_for_newfriend);
		
		//个人资料按钮，点击后弹出页面显示用户私钥信息
		personalinformation = new JButton("个人资料");
		personalinformation.setForeground(new Color(255, 255, 255));
		personalinformation.setContentAreaFilled(false);
		personalinformation.setBounds(3, 638, 97, 23);
		personalinformation.addActionListener(this);
		panel.add(personalinformation);
		
		//绘制好友列表
		reflashFriendList(owner);
		
		//检测主界面的窗口关闭，在关闭页面时，向服务器发送登出消息
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//首先关闭所有聊天窗口
				for(Chat value:ChatUIList.getAllChat().values()) {
					value.chat_end();
				}
				//向服务器发送登出请求logout
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				owner.setPriKey(null);
				cmd.setData(owner);
				//删掉pri_key
				cmd.setSender(owner.getUseremail());
				client.sendData(cmd);
				frame.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				for(Chat value:ChatUIList.getAllChat().values()) {
					value.chat_end();
				}
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				owner.setPriKey(null);
				cmd.setData(owner);
				//删掉pri_key
				cmd.setSender(owner.getUseremail());
				client.sendData(cmd);
				frame.dispose();
			}
		});
	}
	
	//删除好友函数，在好友列表中选中好友点击鼠标右键即可删除好友
	public void delete_friends(int ID) {
		JLabel FriendLabel = labelfriendList.get(ID);
		JPanel FriendPanel = friends_list.get(FriendLabel);
		Friends.remove(FriendPanel);
		Friends.updateUI();
	}
	
	//在登录后服务器返回好友列表，调用此函数打印显示好友列表
	public void reflashFriendList(User owner) {
		Friends.removeAll();
		ImageIcon friends_head[] = new ImageIcon[5];
		for(int i = 0;i<5;i++) {
			friends_head[i] = new ImageIcon("image/friendsui/"+i+".jpg");
			friends_head[i].setImage(friends_head[i].getImage().getScaledInstance(55, 55,
					Image.SCALE_DEFAULT));
		}
		String insert = new String();
		String status = new String();
		int headindex = 0;
		//遍历服务器返回的好友列表，并将好友列表打印显示
		for (Friend value: owner.getFriendList().values()) {
			int a = value.getID();
			
			insert = value.getFriendName();
			if(value.getStatus()==true) {
				status = "在线";
			}
			else {
				status = "离线";
			}
			while(insert.length()<5) {
				insert = " "+insert;
			}
			while(insert.length() < 20) {
				insert = insert + " ";
			}
			friends_list_panel  = new JPanel();
			friends_list_panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
			friends_list_label = new JLabel(insert+status, friends_head[headindex%5], SwingConstants.LEFT);
			friends_list_label.setFont(new Font("楷体",Font.BOLD,16));
			friends_list_label.setForeground(Color.WHITE);
			friends_list_label.setBackground(Color.LIGHT_GRAY);
			friends_list_label.setHorizontalAlignment(SwingConstants.LEFT);
			friends_list_label.setSize(294, 55);
			friends_list_label.addMouseListener(new MyMouseListener());
			friends_list_label.setDisplayedMnemonic(a);
			friends_list_panel.add(friends_list_label);
			friends_list_panel.setOpaque(false);
			Friends.add(friends_list_panel);
			labelfriendList.put(a, friends_list_label);	
			friends_list.put(friends_list_label,friends_list_panel);
			headindex++;
		}
	}
	
	//在好友列表中添加好友后，调用此函数刷新好友列表
	public void reflash_after_addfriend(Friend Friend) {
		friends_list_panel  = new JPanel();
		friends_list_panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		String insert = Friend.getFriendName();
		String status = new String();
		if(Friend.getStatus()) {
			status="在线";
		}
		else {
			status="离线";
		}
		while(insert.length()<5) {
			insert = " "+insert;
		}
		while(insert.length() < 20) {
			insert = insert + " ";
		}
		int a = Friend.getID();
		ImageIcon friends_head = new ImageIcon();
		friends_head = new ImageIcon("image/friendsui/"+friends_num%5+".jpg");
		friends_head.setImage(friends_head.getImage().getScaledInstance(55, 55,Image.SCALE_DEFAULT));
		friends_list_label = new JLabel(insert+status, friends_head, SwingConstants.LEFT);
		friends_list_label.setFont(new Font("楷体",Font.BOLD,16));
		friends_list_label.setForeground(Color.WHITE);
		friends_list_label.setBackground(Color.LIGHT_GRAY);
		friends_list_label.setHorizontalAlignment(SwingConstants.LEFT);
		friends_list_label.setSize(294, 55);
		friends_list_label.addMouseListener(new MyMouseListener());
		friends_list_label.setDisplayedMnemonic(a);
		friends_list_panel.add(friends_list_label);
		friends_list_panel.setOpaque(false);
		Friends.add(friends_list_panel);
		Friends.updateUI();
		labelfriendList.put(a, friends_list_label);
		friends_list.put(friends_list_label,friends_list_panel);
	}
	
	//在好友在线状态改变时，更新好友在线状态
	public void reflash_friendstatus(Friend friend) {
		JLabel FriendLabel = labelfriendList.get(friend.getID());
		String nameAndstatusText = FriendLabel.getText();
		String friendstatus = new String();
		if(friend.getStatus()) {
			friendstatus="在线";
		}
		else {
			friendstatus="离线";
		}
		FriendLabel.setText((nameAndstatusText.substring(0, nameAndstatusText.length()-2))+friendstatus);
	}
	
	//此函数可查找已添加的好友，并弹出聊天窗口
	public void search_myfriends() {
		boolean flag=false;
		String email = new String(searchFriend.getText()).trim();
		if(email.equals("") || email == null || "输入邮箱以查找好友".equals(email)) {
			JOptionPane.showMessageDialog(null, "请输入您所要查找的好友所绑定的邮箱");
		}
		else {
			for(Friend value: owner.getFriendList().values()) {
				if(email.equals(value.getFriendEmail())) {
					//调用函数弹出与该好友的聊天界面
					chat_friend(value);
				}
			}
		}
	}
	
	class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			//双击我的好友弹出与该好友的聊天框
			if(e.getClickCount() == 2&&e.getButton()==MouseEvent.BUTTON1) {
				JLabel label = (JLabel)e.getSource(); //getSource()返回的是Object,
				
				int friendid = label.getDisplayedMnemonic();
				Friend friend = owner.getFriendList().get(friendid);
				//调用函数弹出与该好友的聊天界面
				chat_friend(friend);
			}	
			//鼠标右键点击好友，弹出提示框“是否删除好友”，若点击确定，则删除该好友，并向服务器返回删除该好友的消息
			if(e.getButton()==MouseEvent.BUTTON3) {
				JLabel label = (JLabel)e.getSource();
				int friendid = label.getDisplayedMnemonic();
				int flag = JOptionPane.showConfirmDialog(frame, "是否删除该好友", "删除好友", JOptionPane.YES_NO_OPTION);
				if(flag == 0) {
					//是
					delete_friends(friendid);
					
					Friend dele_fri = owner.getFriendList().get(friendid);
					CommandTranser cmd = new CommandTranser();
					cmd.setCmd("delete_friend");
					cmd.setData(owner.getUserId());
					cmd.setReceiver(dele_fri.getFriendEmail());
					cmd.setSender(owner.getUseremail());
					client.sendData(cmd);
					owner.getFriendList().remove(friendid);
					return;
				} else {
					return;
				}
			}
		}
	}
	
	public void chat_friend(Friend friend_chat)
	{
		String friendname = friend_chat.getFriendEmail();
		
		//离线信息处理
		if(friend_chat.getStatus() == false) {
			//生成DES密钥
			DES des = new DES();
			String des_key = null;
			try {
				des_key = des.getkey();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//用朋友的RSA公钥加密DES密钥
			RSA rsa = new RSA();
			String des_kye_encrypted = null;
			try {
				des_kye_encrypted = rsa.encode(des_key, friend_chat.getPubKey());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Chat chatUI = new Chat(owner, friend_chat, client, des_key);
			ChatUIEntity chatUIentity = new ChatUIEntity(chatUI, friendname);
			ChatUIList.addChatUI(chatUIentity);
		}
		//至此，离线信息处理更新结束
		
		//在线消息发送
		if(friend_chat.getStatus() == true) {
			//新建一条socket连接
			Client friend_client = new Client(friend_chat.getIP());
			Chat chatUI = ChatUIList.getChatUI(friendname);  //查看是否创建过与该好友的聊天窗口
			if(chatUI == null) {  //未创建过
				//生成DES密钥
				DES des = new DES();
				String des_key = null;
				try {
					des_key = des.getkey();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//用朋友的RSA公钥加密DES密钥
				RSA rsa = new RSA();
				String des_kye_encrypted = null;
				try {
					des_kye_encrypted = rsa.encode(des_key, friend_chat.getPubKey());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//发送聊天请求key_chat
				CommandTranser cmd = new CommandTranser();
				cmd.setSender(owner.getUseremail());
				cmd.setReceiver(friend_chat.getFriendEmail());
				cmd.setCmd("key_chat");
				cmd.setData(des_kye_encrypted);
				friend_client.sendData(cmd);
				//接受返回信息back_key_chat
				cmd = friend_client.getData();
				if("back_key_chat".equals(cmd.getCmd())){
					ServerThread thread = new ServerThread(friend_client, owner);  //新建线程用于接受该好友的消息
					thread.start();
					chatUI = new Chat(owner, friend_chat, friend_client, des_key);  //新建聊天窗口
					ChatUIEntity chatUIentity = new ChatUIEntity(chatUI, friendname);
					ChatUIList.addChatUI(chatUIentity);  //加入到ChatUIList
				}
				
			}
			else {
				chatUI.show(); //创建过，顶层显示它
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			if(e.getSource() == searchFriend) {
				//调用函数在已添加好友列表中查找是否有该邮箱所对应的好友，若已添加该好友则弹出与该好友的聊天窗口
				search_myfriends();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==search_button) {
			search_myfriends();  //查找好友按钮响应
		}
		if(e.getSource()==search_for_newfriend) {
			Addfriend newfriend = new Addfriend(owner,client);  //添加好友
		}
		if(e.getSource() == logout) {   //退出登录
			//关闭所有聊天窗口
			for(Chat value:ChatUIList.getAllChat().values()) {
				value.chat_end();
			}
			//向服务器发送登出请求logout
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("logout");
			owner.setPriKey(null);
			cmd.setData(owner);
			//删掉pri_key
			cmd.setSender(owner.getUseremail());
			client.sendData(cmd);
			frame.dispose();
		}
		if(e.getSource() == personalinformation) {
			PersonalInformation PI = new PersonalInformation(owner.getPriKey());
		}
	}
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == searchFriend) {
			if("输入邮箱以查找好友".equals(searchFriend.getText())){
				searchFriend.setText("");
				searchFriend.setForeground(Color.WHITE);
			}
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == searchFriend) {
			if("".equals(searchFriend.getText())){
				searchFriend.setText("输入邮箱以查找好友");
				searchFriend.setForeground(Color.WHITE);
			}
		}
	}
}
class startServerThread extends Thread{
	private User user;
	public startServerThread(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	@Override
	public void run() {
		//创建socket
		Service s = new Service(user);
		s.startService();
	}
}

class startFileServerThread extends Thread{
	private User user;
	public startFileServerThread(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	@Override
	public void run() {
		//创建socket
		fileService s = new fileService(user);
		s.startService();
	}
}
