package Frame;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;

import Entity.Friend;
import Entity.Message;
import Entity.Offmessage;
import Entity.User;
import Security.DES;
import Security.MD5;
import Security.RSA;
import UserSocket.Client;
import _Util.ChatUIList;
import _Util.CommandTranser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Image;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
/**
*������棬�з���������Ϣ��������Ϣ���ļ�����
*/
public class Chat implements ActionListener{

	private JFrame frame;
	private JButton Send;
	private JButton send_file;
	private String sender;
	private String receiver;
	private String des_key;
	private Client client;
	private User user;
	private Friend friend;
	private JTextArea my_message;
	private JScrollBar scrollBar;
	private JPanel panel;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	//private CommandTranser cmd = new CommandTranser();
	private JPanel panel_2;
	
	public Chat(User user,Friend friend,Client client, String des_key) {
		this.sender = user.getUsername();
		this.receiver = friend.getFriendName();
		this.friend = friend;
		this.user = user;
		this.client = client;
		this.des_key = des_key;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	
	//�������죬����chat_end�����������ر����촰��ʱ����ѷ���ָ��
	public void chat_end() {
		if(friend.getStatus() == false) return;
		ChatUIList.deletChatUI(receiver);
		CommandTranser cmd = new CommandTranser();
		cmd.setCmd("chat_end");
		cmd.setSender(sender);
		cmd.setReceiver(receiver);
		client.sendData(cmd);
	}
	
	private void initialize() {
		//��������ҳ�棬������ҳ����⡢��С������λ�õȲ���
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frame.setResizable(true);
		frame.setBounds((mywidth-600)/2, (myheight-500)/2, 600, 500);
		frame.setTitle(receiver);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		//�༭��Ϣ������ģ��
		panel = new JPanel();
		panel.setBounds(0, 318, 584, 143);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		//������Ϣ����򣬹��û�������Ҫ���͵���Ϣ
		my_message = new JTextArea();
		my_message.setLineWrap(true);
		my_message.setBounds(0, 0, 584, 121);
		
		//���롰���͡���ť�������¼�����������ð�ť���ɽ��ı����е���Ϣ���ͳ�ȥ
		Send = new JButton("����");
		Send.setBounds(487, 120, 97, 23);
		Send.setFocusPainted(false);
		panel.add(Send);
		
		//���롰�����ļ�����ť�������¼�����������ð�ť���ɵ��������ļ�����
		send_file = new JButton("�����ļ�");
		send_file.setBounds(370, 120, 97, 23);
		send_file.setFocusPainted(false);
		send_file.addActionListener(this);
		panel.add(send_file);
		
		scrollPane = new JScrollPane(my_message);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 584, 121);
		panel.add(scrollPane);
		panel.updateUI();
		
		//��ʷ��Ϣ��ʾģ��
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setBounds(0, 0, 584, 320);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 584, 320);
		panel_2.setLayout(new BoxLayout(panel_2,BoxLayout.Y_AXIS));
		
		scrollPane_1 = new JScrollPane(panel_2);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(0, 0, 584, 320);
		panel_1.add(scrollPane_1);
		
		panel_1.updateUI();
		panel_2.updateUI();
		Send.addActionListener(this);
		
		//�رմ����˳�����
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chat_end();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				chat_end();
			}
		});
	}
	
	//��ʾ������Ϣ
	public void Send_Message(Message Message) {
		String message = Message.getMessage();
		JPanel send = new JPanel();
		send.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
		JPanel send_1 = new JPanel();
		send_1.setLayout(new BoxLayout(send_1,BoxLayout.Y_AXIS));
		JPanel time_panel = new JPanel();
		time_panel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
		JPanel sender_panel = new JPanel();
		sender_panel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
		JPanel sender_panel_1 = new JPanel();
		sender_panel_1.setLayout(new BoxLayout(sender_panel_1,BoxLayout.X_AXIS));
		sender_panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		sender_panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		
		//��ӡʱ���
		String time = Message.getDate();
		JLabel time_label = new JLabel(time,SwingConstants.RIGHT);
		time_label.setForeground(Color.BLACK);
		time_label.setFont(new Font("����",Font.PLAIN,10));
		time_panel.add(time_label);
		
		JPanel mymessage_panel = new JPanel();
		mymessage_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		mymessage_panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mymessage_panel.setLayout(new BoxLayout(mymessage_panel,BoxLayout.Y_AXIS));
		JPanel myhead_panel = new JPanel();
		myhead_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		myhead_panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		//��ӡ��Ϣ����
		int column = message.length()/30;
		int i;
		String new_message = "<html>";
		for(i = 0;i<column;i++) {
			new_message = new_message + message.substring(30*i, 30*(i+1)) + "<br>";
		}
		new_message = new_message + message.substring(30*i, message.length()) + "</html>";
		JLabel mymessage_label = new JLabel(new_message);
		mymessage_label.setForeground(Color.BLACK);
		mymessage_label.setFont(new Font("����",Font.PLAIN,12));
		mymessage_label.setVerticalTextPosition(SwingConstants.CENTER);
		mymessage_label.setHorizontalTextPosition(SwingConstants.CENTER);
		mymessage_label.setBackground(new Color(0, 191, 255));
		mymessage_label.setBorder(BorderFactory.createLineBorder(new Color(0, 191, 255),10));
		mymessage_label.setOpaque(true);
		JLabel adjust_label = new JLabel(" ");
		mymessage_panel.add(adjust_label);
		mymessage_panel.add(mymessage_label);
		sender_panel_1.add(mymessage_panel);
		
		//��ӡ�û�ͷ��
		ImageIcon head = new ImageIcon("image/sender.jpg");//�����û�ͷ��
		head.setImage(head.getImage().getScaledInstance(25, 25,Image.SCALE_DEFAULT));
		JLabel myhead_label = new JLabel(sender,head,SwingConstants.CENTER);
		myhead_label.setFont(new Font("����",Font.BOLD,10));
		myhead_label.setAlignmentY(Component.TOP_ALIGNMENT);
		myhead_label.setVerticalTextPosition(SwingConstants.TOP);
		myhead_label.setHorizontalTextPosition(SwingConstants.CENTER);
		myhead_label.setSize(25,25);
		myhead_panel.add(myhead_label);
		sender_panel_1.add(myhead_panel);
		
		sender_panel.add(sender_panel_1);
		send_1.add(time_panel);
		send_1.add(sender_panel);
		send.add(send_1);
		panel_2.add(send);
		panel_2.updateUI();
		
		scrollBar = scrollPane_1.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
	//��ʾ���յ�����Ϣ�����н��ܺ���Ϣ��֤
	public void Receive_Message(Message Message) {
		String message_des = Message.getMessage();
		String mess_md5_des = Message.getMD5();
		String time = Message.getDate();
		DES des = new DES();
		MD5 md5 = new MD5();
		String message = null;
		String mess_md5 = null;
		String my_mess_md5 = null;
		try {
			message = des.decryptDES(message_des, des_key); //������Ϣ
			mess_md5 = des.decryptDES(mess_md5_des, des_key);  //������ϢMD5
			my_mess_md5 = md5.getMD5(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(my_mess_md5.equals(mess_md5)==false) {  //������Ϣ�Ƿ񱻴۸�
			String warn = receiver +"��"+ time + "���͵���Ϣ���۸ģ�";
			JOptionPane.showMessageDialog(frame, warn);
		}
		
		JPanel send = new JPanel();
		send.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		JPanel send_1 = new JPanel();
		send_1.setLayout(new BoxLayout(send_1,BoxLayout.Y_AXIS));
		JPanel time_panel = new JPanel();
		time_panel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		JPanel sender_panel = new JPanel();
		sender_panel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		JPanel sender_panel_1 = new JPanel();
		sender_panel_1.setLayout(new BoxLayout(sender_panel_1,BoxLayout.X_AXIS));
		sender_panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		sender_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		//��ӡʱ���
		JLabel time_label = new JLabel(time,SwingConstants.LEFT);
		time_label.setForeground(Color.BLACK);
		time_label.setFont(new Font("����",Font.PLAIN,10));
		time_panel.add(time_label);
		
		JPanel mymessage_panel = new JPanel();
		mymessage_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		mymessage_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mymessage_panel.setLayout(new BoxLayout(mymessage_panel,BoxLayout.Y_AXIS));
		JPanel myhead_panel = new JPanel();
		myhead_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		myhead_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		//��ӡ��Ϣ����
		int column = message.length()/30;
		int i;
		String new_message = "<html>";
		for(i = 0;i<column;i++) {
			new_message = new_message + message.substring(30*i, 30*(i+1)) + "<br>";
		}
		new_message = new_message + message.substring(30*i, message.length()) + "</html>";
		JLabel mymessage_label = new JLabel(new_message);
		mymessage_label.setForeground(Color.BLACK);
		mymessage_label.setFont(new Font("����",Font.PLAIN,12));
		mymessage_label.setVerticalTextPosition(SwingConstants.CENTER);
		mymessage_label.setHorizontalTextPosition(SwingConstants.CENTER);
		mymessage_label.setBackground(new Color(0, 191, 255));
		mymessage_label.setBorder(BorderFactory.createLineBorder(new Color(0, 191, 255),10));
		mymessage_label.setOpaque(true);
		JLabel adjust_label = new JLabel(" ");
		mymessage_panel.add(adjust_label);
		mymessage_panel.add(mymessage_label);
		
		//��ӡ�û�ͷ��
		ImageIcon head = new ImageIcon("image/sender.jpg");//�����û�ͷ��
		head.setImage(head.getImage().getScaledInstance(25, 25,Image.SCALE_DEFAULT));
		JLabel myhead_label = new JLabel(receiver,head,SwingConstants.CENTER);
		myhead_label.setFont(new Font("����",Font.BOLD,10));
		myhead_label.setAlignmentY(Component.TOP_ALIGNMENT);
		myhead_label.setVerticalTextPosition(SwingConstants.TOP);
		myhead_label.setHorizontalTextPosition(SwingConstants.CENTER);
		myhead_label.setSize(25,25);
		myhead_panel.add(myhead_label);
		sender_panel_1.add(myhead_panel);
		sender_panel_1.add(mymessage_panel);
		
		sender_panel.add(sender_panel_1);
		send_1.add(time_panel);
		send_1.add(sender_panel);
		send.add(send_1);
		panel_2.add(send);
		panel_2.updateUI();
		
		scrollBar = scrollPane_1.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO Auto-generated method stub
		//������Ϣģ��
		if (e.getSource() == Send) {
			if(my_message.getText()==null||"".equals(my_message.getText())) {
				JOptionPane.showMessageDialog(null, "��������Ҫ���͵���Ϣ����");
				return;
			}
			else {
			CommandTranser cmd = new CommandTranser();
			cmd.setSender(user.getUseremail());
			cmd.setReceiver(friend.getFriendEmail());
			String mess = my_message.getText();
			Message message= new Message(mess);
			Send_Message(message);
			
			//������ϢMD5����������Ϣ��MD5
			MD5 md5 = new MD5();
			DES des = new DES();
			String mess_des = null;
			String mess_md5 = null;
			try {
				mess_des = des.encryptDES(mess, des_key);
				message.setMessage(mess_des);
				mess_md5 = md5.getMD5(mess);
				mess_md5 = des.encryptDES(mess_md5, des_key);
				message.setMD5(mess_md5);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//������Ϣ����
			if(friend.getStatus() == false) {
				Offmessage off_mess = new Offmessage(mess_des);
				off_mess.setMD5(mess_md5);
				RSA rsa = new RSA();
				String des_key_encrypted = null;
				try {
					des_key_encrypted = rsa.encode(des_key, friend.getPubKey());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				off_mess.setDesKey(des_key_encrypted);
				off_mess.setSender(user.getUseremail());
				cmd.setCmd("offline_message");
				cmd.setData(off_mess);
				cmd.setReceiver(friend.getFriendEmail());
				cmd.setSender(user.getUseremail());
				client.sendData(cmd);
			}
			//���ˣ�������Ϣ�������
			
			//������Ϣ����
			if(friend.getStatus() == true) {
				cmd.setData(message);
				cmd.setCmd("message");
				client.sendData(cmd);
			}
			
			my_message.setText("");
			}
		}
		//�����ļ�ģ��
		if(e.getSource() == send_file) {
			if(friend.getStatus()) {
				//�����ļ���������
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("file");
				cmd.setReceiver(friend.getFriendEmail());
				cmd.setSender(user.getUseremail());
				cmd.setData(friend.getIP());
				client.sendData(cmd);
			}
			else {
				JOptionPane.showMessageDialog(frame, "�Է������ߣ��޷������ļ�");
			}
			
		}
	}
	public void show() {
		// TODO Auto-generated method stub
		frame.show();
	}
	public void dispose() {
		frame.dispose();
	}
}
