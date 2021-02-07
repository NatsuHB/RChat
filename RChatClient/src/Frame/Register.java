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
*ע����棬������
*1.�����ʽ���
*2.�������䡢�ǳơ��������ע��
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
		//����ҳ�沢����ҳ���С�����⡢����λ�õ�ϸ��
		frame = new JFrame();
		frame.setTitle("ע��");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("/image/IconImage.png"));frame.setResizable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frame.setBounds((mywidth-320)/2, (myheight-540)/2, 320, 540);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//����һ��JPanel�������ñ������С
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
		
		//����ı����Թ��û������ǳ�ע��
		nickname = new JTextField("���趨����ǳ�");
		nickname.setForeground(Color.WHITE);
		nickname.setBounds(66, 80, 188, 30);
		nickname.setBackground(new Color(0,0,0,0));
		nickname.addFocusListener(this);
		panel.add(nickname);
		nickname.setColumns(10);
		
		//����ı����Թ��û����������
		QQemail = new JTextField("����������󶨵�����");
		QQemail.setForeground(Color.WHITE);
		QQemail.setBounds(66, 130, 188, 30);
		QQemail.setBackground(new Color(0,0,0,0));
		QQemail.addFocusListener(this);
		panel.add(QQemail);
		QQemail.setColumns(10);
		
		//����ı����Թ��û���������
		passwordField = new JPasswordField("���趨�������");
		passwordField.setForeground(Color.WHITE);
		passwordField.setBounds(66, 180, 188, 30);
		passwordField.setBackground(new Color(0,0,0,0));
		passwordField.setEchoChar('\0');
		passwordField.addFocusListener(this);
		panel.add(passwordField);
		
		//��ӡ�ע�ᡱ��ť������¼����������ע�ᰴť��ע��
		registerbutton = new JButton("ע��");
		registerbutton.setForeground(Color.WHITE);
		registerbutton.setBackground(new Color(70, 130, 180));
		registerbutton.setBounds(66, 400, 188, 30);
		registerbutton.addActionListener(this);
		panel.add(registerbutton);
		
		//��ӡ����ء���ť������¼�����������ð�ť���ɹر�ע�����
		button = new JButton("<<����");
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
			//����û������Ƿ�Ϸ�
			String username = nickname.getText().trim();
			String password =  passwordField.getText().trim();
			String email = QQemail.getText().trim();
			if ("".equals(email) || email == null) {
				JOptionPane.showMessageDialog(null, "�������ʺţ���");
				return;
			}
			if ("".equals(password) || password == null) {
				JOptionPane.showMessageDialog(null, "���������룡��");
				return;
			}
			//�����ʽ���
			Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
			Matcher m = p.matcher(email);
			if(!m.matches()) {
				JOptionPane.showMessageDialog(null, "��������ȷ�������ַ");
				return;
			}
			
			//����MD5
			MD5 md5 = new MD5();
			String pwd_md5 = null;
			try {
				pwd_md5 = md5.getMD5(password);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			User user = new User(username, pwd_md5, email);
			//�����������ע������register
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("register");
			cmd.setData(user);
			cmd.setReceiver(email);
			cmd.setSender(email);
			
			// ʵ�����ͻ��ˣ����ҷ������ݣ����client�ͻ���ֱ����������������һֱ����
			Client client = new Client(); //����Ψһ�Ŀͻ��ˣ����ڽ��ܷ�������������Ϣ�� socket�ӿڣ��� 
			client.sendData(cmd); //��������
			cmd = client.getData(); //���ܷ�������Ϣ
			
			if(cmd!=null) {
				if(cmd.isFlag()) {  //ͨ����������֤�����û�û�б�ע��
					//������Կ�����͸�������
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
					cmd = client.getData();//���շ�����Ϣ
					
					if(cmd != null) {
						if(cmd.isFlag()) {
							frame.dispose(); //�ر�ע��ҳ��
							JOptionPane.showMessageDialog(null,  "ע��ɹ������½");
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
			//������ذ�ť���ر�ע�����
			frame.dispose();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == nickname){
			//�������nickname�ı������ı�����ΪĬ����ʾֵ�����趨����ǳơ�ʱ�����ı����е�Ĭ����ʾֵ���
			if("���趨����ǳ�".equals(nickname.getText())){
				nickname.setForeground(Color.WHITE);
				nickname.setText("");
			}
		}
		    	
		if(e.getSource() == passwordField){
			//�������passwordField�ı������ı�����ΪĬ����ʾֵ�����趨������롱ʱ�����ı����е�Ĭ����ʾֵ���
			if("���趨�������".equals(passwordField.getText())){
				passwordField.setForeground(Color.WHITE);
				passwordField.setText("");
				passwordField.setEchoChar('*');
			}
		}
		if(e.getSource() == QQemail){
			//�������QQemail�ı������ı�����ΪĬ����ʾֵ������������󶨵����䡱ʱ�����ı����е�Ĭ����ʾֵ���
			if("����������󶨵�����".equals(QQemail.getText())){
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
				nickname.setText("���趨����ǳ�");
			}
		}
		if(e.getSource() == QQemail){
			if("".equals(QQemail.getText())){
				QQemail.setForeground(Color.WHITE);
				QQemail.setText("����������󶨵�����");
			}
		}
		if(e.getSource() == passwordField){
			if("".equals(passwordField.getText())){
				passwordField.setForeground(Color.WHITE);
				passwordField.setText("���趨�������");
				passwordField.setEchoChar('\0');
			}
		}
	}
}
