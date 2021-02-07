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
*�����棬��Ҫ������
*1.��ʾ�����б�ͺ�������״̬������̬ˢ������״̬
*2.˫���������������촰��
*3.�Ҽ�������ɾ������
*4.��Ӻ��ѣ���ת����Ӻ��ѽ���
*5.�������������������ֱ�ӵ�����Ӧ���ѵ����촰��
*6.��¼ʱ��ʾ������Ϣ
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
	private HashMap<Integer,JLabel> labelfriendList = new HashMap<Integer,JLabel>();//��������б�
	private HashMap<Integer,Friend> friendList;//��������б�
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
		
		new startServerThread(owner).start();   //����socket������������������ÿһ�����촰�ڶ���Ӧһ���߳�
		new startFileServerThread(owner).start();  //�����ļ����䣬ÿ���ļ����ն���Ӧһ���߳�
		//����ҳ�棬����ҳ���С������λ�õȲ���
		frame = new JFrame();
		frame.setTitle("R.Chat");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		//����ҳ���С���ɱ�
		frame.setResizable(false);
		frame.setBounds((mywidth-314)/2, (myheight-671)/2, 320, 700);
		//���ùرմ�ҳ�漴�ر������߳�
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		//���һ��JPanel������ҳ�汳��
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
		
		
		ImageIcon head = new ImageIcon("image/friendsui/3.jpg");//�����û�ͷ��
		head_label = new JLabel(head);
		head_label.setBounds(20, 20, 80, 80);//�����û�ͷ��λ�����С
		head_label.setFocusable(true);
		panel.add(head_label);
		
		nickname = new JLabel(owner.getUsername());//�û��ǳ�
		nickname.setBounds(140, 30, 130, 25);
		nickname.setFont(new Font("����", Font.BOLD, 22));//������������
		nickname.setOpaque(false);
		nickname.setForeground(Color.WHITE);//����������ɫ
		panel.add(nickname);
		
		//���һ��JLabel�����ɲ�������Ӻ��ѵĹ�����������ҵ����Ѻ�ֱ�ӽ�����ú��ѵ����촰��
		searchlabel = new JLabel();
		searchlabel.setBounds(0, 120, 314, 30);
		panel.add(searchlabel);
		
		//�����ı��򣬴˴���������������Բ�������ӵĺ��ѣ�������¼������������������ѹ�س����ɲ�������Ӻ���
		searchFriend = new JTextField("���������Բ��Һ���");//���Һ����ı���
		searchFriend.setHorizontalAlignment(SwingConstants.CENTER);
		searchFriend.setForeground(new Color(255, 255, 255));
		searchFriend.setForeground(Color.WHITE);
		searchFriend.setBounds(0, 0, 250, 30);
		searchFriend.setBackground(new Color(0,0,0,0));
		searchFriend.addFocusListener(this);
		searchFriend.addKeyListener(this);
		searchlabel.add(searchFriend);
		searchFriend.setColumns(10);
		
		//����������ť����searchFriend�ı��������������������ð�ť������������Ӻ��ѵ��б����Ƿ��иú���
		search_button = new JButton("����");//�������Һ��Ѱ�ť
		search_button.setHorizontalAlignment(SwingConstants.LEFT);
		search_button.setBackground(new Color(70, 130, 180));
		search_button.setForeground(Color.WHITE);
		search_button.setBorderPainted(false);
		search_button.setBounds(250, 0, 64, 30);
		search_button.addActionListener(this);
		searchlabel.add(search_button);
		
		//�������ɺ����б������
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
		//���ò���ģʽΪBoxLayoutʹÿ�����Ѱ�������
		Friends.setLayout(new BoxLayout(Friends,BoxLayout.Y_AXIS));
		//����һ��JScrollPane��FriendsǶ����룬����������Ҫʱ����ʾ��״�����������ṩ���������
		friends = new JScrollPane(Friends);
		friends.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//���ú����б�߿�
		friends.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 191, 255), new Color(173, 216, 230), new Color(70, 130, 180), new Color(100, 149, 237)));
		friends.setBounds(20, 160, 294, 480);
		friends.setOpaque(false);
		panel.add(friends);

		//�˳���¼��ť�������¼�����������ð�ť������������͵ǳ���Ϣ
		logout = new JButton("�˳���¼");
		logout.setForeground(new Color(255, 255, 255));
		logout.setContentAreaFilled(false);
		logout.setBounds(207, 638, 97, 23);
		logout.addActionListener(this);
		panel.add(logout);

		//��Ӻ��ѣ������¼�����������ð�ť������Ӻ���ҳ��
		search_for_newfriend = new JButton("��Ӻ���");
		search_for_newfriend.setForeground(new Color(255, 255, 255));
		search_for_newfriend.setContentAreaFilled(false);
		search_for_newfriend.setBounds(110, 638, 87, 23);
		search_for_newfriend.addActionListener(this);
		panel.add(search_for_newfriend);
		
		//�������ϰ�ť������󵯳�ҳ����ʾ�û�˽Կ��Ϣ
		personalinformation = new JButton("��������");
		personalinformation.setForeground(new Color(255, 255, 255));
		personalinformation.setContentAreaFilled(false);
		personalinformation.setBounds(3, 638, 97, 23);
		personalinformation.addActionListener(this);
		panel.add(personalinformation);
		
		//���ƺ����б�
		reflashFriendList(owner);
		
		//���������Ĵ��ڹرգ��ڹر�ҳ��ʱ������������͵ǳ���Ϣ
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//���ȹر��������촰��
				for(Chat value:ChatUIList.getAllChat().values()) {
					value.chat_end();
				}
				//����������͵ǳ�����logout
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				owner.setPriKey(null);
				cmd.setData(owner);
				//ɾ��pri_key
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
				//ɾ��pri_key
				cmd.setSender(owner.getUseremail());
				client.sendData(cmd);
				frame.dispose();
			}
		});
	}
	
	//ɾ�����Ѻ������ں����б���ѡ�к��ѵ������Ҽ�����ɾ������
	public void delete_friends(int ID) {
		JLabel FriendLabel = labelfriendList.get(ID);
		JPanel FriendPanel = friends_list.get(FriendLabel);
		Friends.remove(FriendPanel);
		Friends.updateUI();
	}
	
	//�ڵ�¼����������غ����б����ô˺�����ӡ��ʾ�����б�
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
		//�������������صĺ����б����������б��ӡ��ʾ
		for (Friend value: owner.getFriendList().values()) {
			int a = value.getID();
			
			insert = value.getFriendName();
			if(value.getStatus()==true) {
				status = "����";
			}
			else {
				status = "����";
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
			friends_list_label.setFont(new Font("����",Font.BOLD,16));
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
	
	//�ں����б�����Ӻ��Ѻ󣬵��ô˺���ˢ�º����б�
	public void reflash_after_addfriend(Friend Friend) {
		friends_list_panel  = new JPanel();
		friends_list_panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		String insert = Friend.getFriendName();
		String status = new String();
		if(Friend.getStatus()) {
			status="����";
		}
		else {
			status="����";
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
		friends_list_label.setFont(new Font("����",Font.BOLD,16));
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
	
	//�ں�������״̬�ı�ʱ�����º�������״̬
	public void reflash_friendstatus(Friend friend) {
		JLabel FriendLabel = labelfriendList.get(friend.getID());
		String nameAndstatusText = FriendLabel.getText();
		String friendstatus = new String();
		if(friend.getStatus()) {
			friendstatus="����";
		}
		else {
			friendstatus="����";
		}
		FriendLabel.setText((nameAndstatusText.substring(0, nameAndstatusText.length()-2))+friendstatus);
	}
	
	//�˺����ɲ�������ӵĺ��ѣ����������촰��
	public void search_myfriends() {
		boolean flag=false;
		String email = new String(searchFriend.getText()).trim();
		if(email.equals("") || email == null || "���������Բ��Һ���".equals(email)) {
			JOptionPane.showMessageDialog(null, "����������Ҫ���ҵĺ������󶨵�����");
		}
		else {
			for(Friend value: owner.getFriendList().values()) {
				if(email.equals(value.getFriendEmail())) {
					//���ú���������ú��ѵ��������
					chat_friend(value);
				}
			}
		}
	}
	
	class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			//˫���ҵĺ��ѵ�����ú��ѵ������
			if(e.getClickCount() == 2&&e.getButton()==MouseEvent.BUTTON1) {
				JLabel label = (JLabel)e.getSource(); //getSource()���ص���Object,
				
				int friendid = label.getDisplayedMnemonic();
				Friend friend = owner.getFriendList().get(friendid);
				//���ú���������ú��ѵ��������
				chat_friend(friend);
			}	
			//����Ҽ�������ѣ�������ʾ���Ƿ�ɾ�����ѡ��������ȷ������ɾ���ú��ѣ��������������ɾ���ú��ѵ���Ϣ
			if(e.getButton()==MouseEvent.BUTTON3) {
				JLabel label = (JLabel)e.getSource();
				int friendid = label.getDisplayedMnemonic();
				int flag = JOptionPane.showConfirmDialog(frame, "�Ƿ�ɾ���ú���", "ɾ������", JOptionPane.YES_NO_OPTION);
				if(flag == 0) {
					//��
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
		
		//������Ϣ����
		if(friend_chat.getStatus() == false) {
			//����DES��Կ
			DES des = new DES();
			String des_key = null;
			try {
				des_key = des.getkey();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//�����ѵ�RSA��Կ����DES��Կ
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
		//���ˣ�������Ϣ������½���
		
		//������Ϣ����
		if(friend_chat.getStatus() == true) {
			//�½�һ��socket����
			Client friend_client = new Client(friend_chat.getIP());
			Chat chatUI = ChatUIList.getChatUI(friendname);  //�鿴�Ƿ񴴽�����ú��ѵ����촰��
			if(chatUI == null) {  //δ������
				//����DES��Կ
				DES des = new DES();
				String des_key = null;
				try {
					des_key = des.getkey();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//�����ѵ�RSA��Կ����DES��Կ
				RSA rsa = new RSA();
				String des_kye_encrypted = null;
				try {
					des_kye_encrypted = rsa.encode(des_key, friend_chat.getPubKey());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//������������key_chat
				CommandTranser cmd = new CommandTranser();
				cmd.setSender(owner.getUseremail());
				cmd.setReceiver(friend_chat.getFriendEmail());
				cmd.setCmd("key_chat");
				cmd.setData(des_kye_encrypted);
				friend_client.sendData(cmd);
				//���ܷ�����Ϣback_key_chat
				cmd = friend_client.getData();
				if("back_key_chat".equals(cmd.getCmd())){
					ServerThread thread = new ServerThread(friend_client, owner);  //�½��߳����ڽ��ܸú��ѵ���Ϣ
					thread.start();
					chatUI = new Chat(owner, friend_chat, friend_client, des_key);  //�½����촰��
					ChatUIEntity chatUIentity = new ChatUIEntity(chatUI, friendname);
					ChatUIList.addChatUI(chatUIentity);  //���뵽ChatUIList
				}
				
			}
			else {
				chatUI.show(); //��������������ʾ��
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
				//���ú���������Ӻ����б��в����Ƿ��и���������Ӧ�ĺ��ѣ�������Ӹú����򵯳���ú��ѵ����촰��
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
			search_myfriends();  //���Һ��Ѱ�ť��Ӧ
		}
		if(e.getSource()==search_for_newfriend) {
			Addfriend newfriend = new Addfriend(owner,client);  //��Ӻ���
		}
		if(e.getSource() == logout) {   //�˳���¼
			//�ر��������촰��
			for(Chat value:ChatUIList.getAllChat().values()) {
				value.chat_end();
			}
			//����������͵ǳ�����logout
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("logout");
			owner.setPriKey(null);
			cmd.setData(owner);
			//ɾ��pri_key
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
			if("���������Բ��Һ���".equals(searchFriend.getText())){
				searchFriend.setText("");
				searchFriend.setForeground(Color.WHITE);
			}
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == searchFriend) {
			if("".equals(searchFriend.getText())){
				searchFriend.setText("���������Բ��Һ���");
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
		//����socket
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
		//����socket
		fileService s = new fileService(user);
		s.startService();
	}
}
