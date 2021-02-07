package Frame;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.Toolkit;

import Entity.User;
import Security.MD5;
import Security.RC4;
import UserSocket.ChatTread;
import UserSocket.Client;
import _Util.CommandTranser;
import javax.swing.JOptionPane;
/**
*登陆页面
*/
public class Login implements FocusListener, ActionListener, KeyListener {

	JFrame frmSk;
	private JTextField account;
	private JPasswordField password;
	private JCheckBox checkBox;
	private JButton login;
	private JButton register;
	private JButton forget;
	private static final String _txt_account = "邮箱";
	private static final String _txt_pwd = "密码";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Login window = new Login();
					window.frmSk.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//加入渲染
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
        	System.out.println(e);
        }
		frmSk = new JFrame();
		frmSk.setResizable(false);//设置窗口大小不可调整
		//进行窗口标题、大小、出现位置等设置
		frmSk.setTitle("R.Chat");
		frmSk.setIconImage(Toolkit.getDefaultToolkit().getImage("image\\IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frmSk.setBounds((mywidth-320)/2, (myheight-540)/2, 320, 540);
		frmSk.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmSk.getContentPane().setLayout(null);
		
		//加入一层JPanel，并设置背景图
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/login.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		panel.setBounds(0, 0, 304, 501);
		frmSk.getContentPane().add(panel);
		panel.setLayout(null);
		
		//加入文本框以供用户输入绑定邮箱，文本框中默认字符串为“邮箱”提示用户文本框中需要输入的内容，并加入监听器。
		account = new JTextField(_txt_account);
		account.setForeground(Color.WHITE);
		account.setBounds(55, 248, 190, 30);
		account.addFocusListener(this);
		account.setBackground(new Color(0,0,0,0));
		account.addKeyListener(this);
		panel.add(account);
		account.setColumns(10);
		
		//加入文本框以供用户输入密码，文本框中默认字符串为“密码”提示用户文本框中需要输入的内容，并加入监听器。
		password = new JPasswordField(_txt_pwd);
		password.setForeground(Color.WHITE);
		password.setEchoChar('\0');
		password.setBounds(55, 293, 190, 30);
		password.setBackground(new Color(0,0,0,0));
		password.addFocusListener(this);
		password.addKeyListener(this);
		panel.add(password);
		
		//登录按钮，加入监听器，点击即可登录
		login = new JButton("登录");
		login.setBackground(new Color(70, 100, 160));
		login.setForeground(Color.WHITE);
		login.setBounds(100, 379, 100, 38);
		login.setFocusPainted(false);
		login.addActionListener(this);
		panel.add(login);
		
		//点击可跳转至注册界面
		register = new JButton("注册>>");
		register.setContentAreaFilled(false);
		register.setBorderPainted(false);
		register.setForeground(new Color(248, 248, 255));
		register.setHorizontalAlignment(SwingConstants.RIGHT);
		register.setBounds(213, 468, 81, 23);
		register.addActionListener(this);
		panel.add(register);
	}
	
	//用户登录函数，在Login按钮处发生事件时调用此函数
	public void user_login() {
		//如果点击了登录按钮 首先判断帐号或者密码是否为空,然后封装为CommandTranser对象向服务器发送数据,服务器通过与数据库的比对来验证帐号密码。
		String user_email = account.getText().trim();
		String user_pwd = new String(password.getPassword()).trim();
		if("".equals(user_email) || user_email == null || _txt_account.equals(user_email)) {
			JOptionPane.showMessageDialog(null, "请输入邮箱！！");
			return;
		}
		if("".equals(user_pwd) || user_pwd == null || _txt_pwd.equals(user_pwd)) {
			JOptionPane.showMessageDialog(null, "请输入密码！！");
			return;
		}
		
		//密码MD5
		MD5 md5 = new MD5();
		String pwd_md5 = null;
		try {
			pwd_md5 = md5.getMD5(user_pwd);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		User user = new User(null, pwd_md5,user_email);
		CommandTranser cmd = new CommandTranser();
		cmd.setCmd("login");
		cmd.setData(user);
		cmd.setReceiver(user_email);
		cmd.setSender(user_email);
		
		//实例化客户端 连接服务器 发送数据 密码是否正确?
		
		Client client = new Client(); //创建唯一的客户端（用于接受服务器发来的消息， socket接口）， 
		client.sendData(cmd); //发送数据
		cmd = client.getData(); //接受反馈的消息
		
		if(cmd != null) {
			if(cmd.isFlag()) {
				dispose(); //关闭登录页面
				JOptionPane.showMessageDialog(null,  "登录成功");
				user = (User)cmd.getData(); 
				
				RC4 rc4 = new RC4();
				String pri_key = null;
				try {
					pri_key = rc4.decryptWithRC4(user.getPriKey(), user_pwd);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				user.setPriKey(pri_key);
				
				MainPage friendsUI = new MainPage(user, client); //将user的全部信息传到MainPage中，并将唯一与服务器交流的接口传到MainPage中
				ChatTread thread = new ChatTread(client, user, friendsUI); //这里传client为了收消息，客户端与服务器的交互用一个 ChatTread，一个client 
				thread.start();
			}else {
				JOptionPane.showMessageDialog(frmSk, cmd.getResult());
			}
		}		
	
	}
	@Override
	
	public void actionPerformed(ActionEvent e){
		
		//处理登录(login)页面，若按钮login被点击则在此进行处理
		if(e.getSource() == login){
			user_login();
		}
		
		//处理注册(register)页面，如果点击了注册账号就弹出注册页面, 信息填写完整后连接服务器
		if(e.getSource() == register){
			Register registerUI = new Register(this);
		}

			
	}
	@Override
	@SuppressWarnings("deprecation")
	public void focusGained(FocusEvent e) {
		//处理账号输入框
		//当用户点击文本框，文本框中为默认提示值“邮箱”时，则清空文本框
    	if(e.getSource() == account){
			if(_txt_account.equals(account.getText())){
				account.setText("");
				account.setForeground(Color.WHITE);
			}
		}
    	
		//处理密码输入框
    	//当用户点击此文本框，若文本框中为默认提示值“密码”时，则清空此文本框，并设置用户在此文本框中的输入皆显示为“*”
		if(e.getSource() == password){
			if(_txt_pwd.equals(password.getText())){
				password.setText("");
				password.setEchoChar('*');
				password.setForeground(Color.WHITE);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		//处理账号输入框
		//当用户离开此文本框，若文本框中为空时，则重新显示默认提示值“邮箱”
		if(e.getSource() == account){
			if("".equals(account.getText())){
				account.setForeground(Color.WHITE);
				account.setText(_txt_account);
			}
		}
				//处理密码输入框
		//当用户离开此文本框，若文本框中为空时，则重新显示默认提示值“密码”
		if(e.getSource() == password){
			if("".equals(password.getText())){
				password.setForeground(Color.WHITE);
				password.setText(_txt_pwd);
				password.setEchoChar('\0');
			}
		}
	}
	
	//关闭本页面
	public void dispose() {
		// TODO Auto-generated method stub
		frmSk.dispose();
	}
	
	//
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	//为login按钮设置回车监听，在文本框中输入账号密码后直接点击回车即可登录
	public void keyPressed(KeyEvent e) { //回车监听
		// TODO Auto-generated method stub
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			user_login();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

