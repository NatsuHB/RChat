package Frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Entity.User;
import UserSocket.Client;
import _Util.CommandTranser;
/**
*添加好友界面，通过邮箱查找并添加好友
*/
public class Addfriend implements ActionListener, FocusListener {

	private JFrame frame;
	private JTextField newfriend_email;
	private JButton search_add;
	private JTextField textField;
	private User user;
	private Client client;

	public Addfriend(User user,Client client) {
		this.user = user;
		this.client = client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("R.Chat添加好友");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/IconImage.png"));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frame.setResizable(false);
		frame.setBounds((mywidth-320)/2, (myheight-400)/2, 320, 400);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JPanel addfriend = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/addfriend.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		addfriend.setBounds(0, 0, 314, 371);
		addfriend.setFocusable(true);
		frame.getContentPane().add(addfriend);
		addfriend.setLayout(null);
		
		newfriend_email = new JTextField("请输入所添加好友的绑定邮箱");
		newfriend_email.setHorizontalAlignment(SwingConstants.CENTER);
		newfriend_email.setBackground(Color.WHITE);
		newfriend_email.setForeground(Color.DARK_GRAY);
		newfriend_email.setBounds(65, 110, 184, 30);
		newfriend_email.addFocusListener(this);
		addfriend.add(newfriend_email);
		newfriend_email.setColumns(10);
		
		search_add = new JButton("\u67E5\u627E\u6DFB\u52A0");
		search_add.setForeground(Color.WHITE);
		search_add.setBackground(new Color(70, 130, 180));
		search_add.setBounds(82, 220, 150, 30);
		search_add.addActionListener(this);
		addfriend.add(search_add);
		
	}
	
	//事件响应
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==search_add) {
			String email = newfriend_email.getText().trim();
			if ("".equals(email) || email == null) {
				JOptionPane.showMessageDialog(null, "请输入所添加好友邮箱！");  //保证输入不为空
				return;
			}
			else {
				//发送添加好友请求request_add_friend
				CommandTranser cmd = new CommandTranser();
				cmd.setReceiver(email);
				cmd.setSender(user.getUseremail());
				cmd.setData(email);
				cmd.setCmd("request_add_friend");
				client.sendData(cmd);
				frame.dispose();
			}
		}
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==newfriend_email) {
			if("请输入所添加好友的绑定邮箱".equals(newfriend_email.getText())) {
				newfriend_email.setText("");
				newfriend_email.setForeground(Color.DARK_GRAY);
			}
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==newfriend_email) {
			if("".equals(newfriend_email)) {
				newfriend_email.setForeground(Color.DARK_GRAY);
				newfriend_email.setText("请输入所添加好友的绑定邮箱");
			}
		}
	}
}
