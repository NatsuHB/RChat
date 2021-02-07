package Frame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Entity.User;
import Security.MD5;
import Security.RC4;
import Security.RSA;
import UserSocket.Client;
import _Util.CommandTranser;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
/**
*注册界面，功能有
*1.邮箱格式检查
*2.输入邮箱、昵称、密码进行注册
*/
public class Register implements ActionListener, FocusListener {
	private Login login;
	private JFrame frame;
	private JTextField nickname;
	private JTextField QQemail;
	private JPasswordField passwordField;
	private JTextField user_question;
	private JTextField user_answer;
	private JButton registerbutton;
	private JButton button;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public Register(Login login) {
		this.login = login;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//建立页面并设置页面大小、标题、出现位置等细节
		frame = new JFrame();
		frame.setTitle("注册");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("/image/IconImage.png"));frame.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frame.setBounds((mywidth-320)/2, (myheight-540)/2, 320, 540);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//加入一层JPanel，并设置背景与大小
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/register.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		panel.setBounds(0, 0, 314, 511);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		//添加文本框以供用户输入昵称注册
		nickname = new JTextField("请设定你的昵称");
		nickname.setForeground(Color.WHITE);
		nickname.setBounds(66, 80, 188, 30);
		nickname.setBackground(new Color(0,0,0,0));
		nickname.addFocusListener(this);
		panel.add(nickname);
		nickname.setColumns(10);
		
		//添加文本框以供用户输入绑定邮箱
		QQemail = new JTextField("请输入你想绑定的邮箱");
		QQemail.setForeground(Color.WHITE);
		QQemail.setBounds(66, 130, 188, 30);
		QQemail.setBackground(new Color(0,0,0,0));
		QQemail.addFocusListener(this);
		panel.add(QQemail);
		QQemail.setColumns(10);
		
		//添加文本框以供用户输入密码
		passwordField = new JPasswordField("请设定你的密码");
		passwordField.setForeground(Color.WHITE);
		passwordField.setBounds(66, 180, 188, 30);
		passwordField.setBackground(new Color(0,0,0,0));
		passwordField.setEchoChar('\0');
		passwordField.addFocusListener(this);
		panel.add(passwordField);
		
		//添加“注册”按钮，添加事件监听，点击注册按钮以注册
		registerbutton = new JButton("注册");
		registerbutton.setForeground(Color.WHITE);
		registerbutton.setBackground(new Color(70, 130, 180));
		registerbutton.setBounds(66, 400, 188, 30);
		registerbutton.addActionListener(this);
		panel.add(registerbutton);
		
		//添加“返回”按钮，添加事件监听，点击该按钮即可关闭注册界面
		button = new JButton("<<返回");
		button.setForeground(Color.WHITE);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setBounds(10, 470, 100, 23);
		button.addActionListener(this);
		panel.add(button);
		
		frame.setVisible(true);	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == registerbutton) {
			//检测用户输入是否合法
			String username = nickname.getText().trim();
			String password =  passwordField.getText().trim();
			String email = QQemail.getText().trim();
			if ("".equals(email) || email == null) {
				JOptionPane.showMessageDialog(null, "请输入帐号！！");
				return;
			}
			if ("".equals(password) || password == null) {
				JOptionPane.showMessageDialog(null, "请输入密码！！");
				return;
			}
			//邮箱格式检查
			Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
			Matcher m = p.matcher(email);
			if(!m.matches()) {
				JOptionPane.showMessageDialog(null, "请输入正确的邮箱地址");
				return;
			}
			
			//密码MD5
			MD5 md5 = new MD5();
			String pwd_md5 = null;
			try {
				pwd_md5 = md5.getMD5(password);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			User user = new User(username, pwd_md5, email);
			//向服务器发送注册请求register
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("register");
			cmd.setData(user);
			cmd.setReceiver(email);
			cmd.setSender(email);
			
			// 实例化客户端，并且发送数据，这个client客户端直到进程死亡，否则一直存在
			Client client = new Client(); //创建唯一的客户端（用于接受服务器发来的消息， socket接口）， 
			client.sendData(cmd); //发送数据
			cmd = client.getData(); //接受反馈的消息
			
			if(cmd!=null) {
				if(cmd.isFlag()) {  //通过服务器验证，该用户没有被注册
					//生成密钥并发送给服务器
					cmd.setCmd("send_key");
					RSA rsa = new RSA();
					try {
						rsa.GenerateKey();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String pub_key = rsa.getPubKey();
					String pri_key = rsa.getPriKey();
					String pri_key_en = null;
					RC4 rc4 = new RC4();
					try {
						pri_key_en = rc4.encryptWithRC4(pri_key, password);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					user.setPubKey(pub_key);
					user.setPriKey(pri_key_en);
					cmd.setData(user);
					
					client.sendData(cmd);
					cmd = client.getData();//接收反馈消息
					
					if(cmd != null) {
						if(cmd.isFlag()) {
							frame.dispose(); //关闭注册页面
							JOptionPane.showMessageDialog(null,  "注册成功，请登陆");
						}
						else {
							JOptionPane.showMessageDialog(frame, cmd.getResult());
						}
					}	
				}
				else {
					JOptionPane.showMessageDialog(frame, cmd.getResult());
				}
			}
			
				
		}
		if (e.getSource() == button) {
			//点击返回按钮，关闭注册界面
			frame.dispose();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == nickname){
			//当鼠标点击nickname文本框，若文本框中为默认提示值“请设定你的昵称”时，将文本框中的默认提示值清空
			if("请设定你的昵称".equals(nickname.getText())){
				nickname.setForeground(Color.WHITE);
				nickname.setText("");
			}
		}
		    	
		if(e.getSource() == passwordField){
			//当鼠标点击passwordField文本框，若文本框中为默认提示值“请设定你的密码”时，将文本框中的默认提示值清空
			if("请设定你的密码".equals(passwordField.getText())){
				passwordField.setForeground(Color.WHITE);
				passwordField.setText("");
				passwordField.setEchoChar('*');
			}
		}
		if(e.getSource() == QQemail){
			//当鼠标点击QQemail文本框，若文本框中为默认提示值“请输入你想绑定的邮箱”时，将文本框中的默认提示值清空
			if("请输入你想绑定的邮箱".equals(QQemail.getText())){
				QQemail.setForeground(Color.WHITE);
				QQemail.setText("");
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == nickname){
			if("".equals(nickname.getText())){
				nickname.setForeground(Color.WHITE);
				nickname.setText("请设定你的昵称");
			}
		}
		if(e.getSource() == QQemail){
			if("".equals(QQemail.getText())){
				QQemail.setForeground(Color.WHITE);
				QQemail.setText("请输入你想绑定的邮箱");
			}
		}
		if(e.getSource() == passwordField){
			if("".equals(passwordField.getText())){
				passwordField.setForeground(Color.WHITE);
				passwordField.setText("请设定你的密码");
				passwordField.setEchoChar('\0');
			}
		}
	}
}
