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
*��½ҳ��
*/
public class Login implements FocusListener, ActionListener, KeyListener {

	JFrame frmSk;
	private JTextField account;
	private JPasswordField password;
	private JCheckBox checkBox;
	private JButton login;
	private JButton register;
	private JButton forget;
	private static final String _txt_account = "����";
	private static final String _txt_pwd = "����";

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
		//������Ⱦ
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
		frmSk.setResizable(false);//���ô��ڴ�С���ɵ���
		//���д��ڱ��⡢��С������λ�õ�����
		frmSk.setTitle("R.Chat");
		frmSk.setIconImage(Toolkit.getDefaultToolkit().getImage("image\\IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frmSk.setBounds((mywidth-320)/2, (myheight-540)/2, 320, 540);
		frmSk.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmSk.getContentPane().setLayout(null);
		
		//����һ��JPanel�������ñ���ͼ
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
		
		//�����ı����Թ��û���������䣬�ı�����Ĭ���ַ���Ϊ�����䡱��ʾ�û��ı�������Ҫ��������ݣ��������������
		account = new JTextField(_txt_account);
		account.setForeground(Color.WHITE);
		account.setBounds(55, 248, 190, 30);
		account.addFocusListener(this);
		account.setBackground(new Color(0,0,0,0));
		account.addKeyListener(this);
		panel.add(account);
		account.setColumns(10);
		
		//�����ı����Թ��û��������룬�ı�����Ĭ���ַ���Ϊ�����롱��ʾ�û��ı�������Ҫ��������ݣ��������������
		password = new JPasswordField(_txt_pwd);
		password.setForeground(Color.WHITE);
		password.setEchoChar('\0');
		password.setBounds(55, 293, 190, 30);
		password.setBackground(new Color(0,0,0,0));
		password.addFocusListener(this);
		password.addKeyListener(this);
		panel.add(password);
		
		//��¼��ť�������������������ɵ�¼
		login = new JButton("��¼");
		login.setBackground(new Color(70, 100, 160));
		login.setForeground(Color.WHITE);
		login.setBounds(100, 379, 100, 38);
		login.setFocusPainted(false);
		login.addActionListener(this);
		panel.add(login);
		
		//�������ת��ע�����
		register = new JButton("ע��>>");
		register.setContentAreaFilled(false);
		register.setBorderPainted(false);
		register.setForeground(new Color(248, 248, 255));
		register.setHorizontalAlignment(SwingConstants.RIGHT);
		register.setBounds(213, 468, 81, 23);
		register.addActionListener(this);
		panel.add(register);
	}
	
	//�û���¼��������Login��ť�������¼�ʱ���ô˺���
	public void user_login() {
		//�������˵�¼��ť �����ж��ʺŻ��������Ƿ�Ϊ��,Ȼ���װΪCommandTranser�������������������,������ͨ�������ݿ�ıȶ�����֤�ʺ����롣
		String user_email = account.getText().trim();
		String user_pwd = new String(password.getPassword()).trim();
		if("".equals(user_email) || user_email == null || _txt_account.equals(user_email)) {
			JOptionPane.showMessageDialog(null, "���������䣡��");
			return;
		}
		if("".equals(user_pwd) || user_pwd == null || _txt_pwd.equals(user_pwd)) {
			JOptionPane.showMessageDialog(null, "���������룡��");
			return;
		}
		
		//����MD5
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
		
		//ʵ�����ͻ��� ���ӷ����� �������� �����Ƿ���ȷ?
		
		Client client = new Client(); //����Ψһ�Ŀͻ��ˣ����ڽ��ܷ�������������Ϣ�� socket�ӿڣ��� 
		client.sendData(cmd); //��������
		cmd = client.getData(); //���ܷ�������Ϣ
		
		if(cmd != null) {
			if(cmd.isFlag()) {
				dispose(); //�رյ�¼ҳ��
				JOptionPane.showMessageDialog(null,  "��¼�ɹ�");
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
				
				MainPage friendsUI = new MainPage(user, client); //��user��ȫ����Ϣ����MainPage�У�����Ψһ������������Ľӿڴ���MainPage��
				ChatTread thread = new ChatTread(client, user, friendsUI); //���ﴫclientΪ������Ϣ���ͻ�����������Ľ�����һ�� ChatTread��һ��client 
				thread.start();
			}else {
				JOptionPane.showMessageDialog(frmSk, cmd.getResult());
			}
		}		
	
	}
	@Override
	
	public void actionPerformed(ActionEvent e){
		
		//�����¼(login)ҳ�棬����ťlogin��������ڴ˽��д���
		if(e.getSource() == login){
			user_login();
		}
		
		//����ע��(register)ҳ�棬��������ע���˺ž͵���ע��ҳ��, ��Ϣ��д���������ӷ�����
		if(e.getSource() == register){
			Register registerUI = new Register(this);
		}

			
	}
	@Override
	@SuppressWarnings("deprecation")
	public void focusGained(FocusEvent e) {
		//�����˺������
		//���û�����ı����ı�����ΪĬ����ʾֵ�����䡱ʱ��������ı���
    	if(e.getSource() == account){
			if(_txt_account.equals(account.getText())){
				account.setText("");
				account.setForeground(Color.WHITE);
			}
		}
    	
		//�������������
    	//���û�������ı������ı�����ΪĬ����ʾֵ�����롱ʱ������մ��ı��򣬲������û��ڴ��ı����е��������ʾΪ��*��
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
		//�����˺������
		//���û��뿪���ı������ı�����Ϊ��ʱ����������ʾĬ����ʾֵ�����䡱
		if(e.getSource() == account){
			if("".equals(account.getText())){
				account.setForeground(Color.WHITE);
				account.setText(_txt_account);
			}
		}
				//�������������
		//���û��뿪���ı������ı�����Ϊ��ʱ����������ʾĬ����ʾֵ�����롱
		if(e.getSource() == password){
			if("".equals(password.getText())){
				password.setForeground(Color.WHITE);
				password.setText(_txt_pwd);
				password.setEchoChar('\0');
			}
		}
	}
	
	//�رձ�ҳ��
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
	//Ϊlogin��ť���ûس����������ı����������˺������ֱ�ӵ���س����ɵ�¼
	public void keyPressed(KeyEvent e) { //�س�����
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

