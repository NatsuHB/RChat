package Frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import Entity.Offmessage;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
/**
*离线消息打印界面
*/
public class OfflineMessage {

	private JFrame frame;
	private JPanel panel;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private JScrollBar scrollBar = new JScrollBar();

	/**
	 * Create the application.
	 */
	public OfflineMessage(ArrayList<Offmessage> off_list) {
		initialize(off_list);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ArrayList<Offmessage> off_list) {
		frame = new JFrame();
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ds = tk.getScreenSize();
		int mywidth = ds.width;
		int myheight = ds.height;
		frame.setBounds((mywidth-323)/2, (myheight-528)/2, 323, 528);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.getContentPane().add(panel);
		panel_1=new JPanel(){
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon back_image = new ImageIcon("image/offlinemessage.jpg");
				g.drawImage(back_image.getImage(), 0, 0, null);
			}
		};
		panel_1.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		panel_1.setOpaque(false);
		scrollPane = new JScrollPane(panel_1);
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		panel.add(scrollPane);
		int i=0;
		for(Offmessage off_mess : off_list) {
			JPanel send = new JPanel();
			send.setOpaque(false);
			send.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
			JPanel send_1 = new JPanel();
			send_1.setOpaque(false);
			send_1.setLayout(new BoxLayout(send_1,BoxLayout.Y_AXIS));
			JPanel time_panel = new JPanel();
			time_panel.setOpaque(false);
			time_panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
			JPanel sender_panel = new JPanel();
			sender_panel.setOpaque(false);
			sender_panel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
			JPanel sender_panel_1 = new JPanel();
			sender_panel_1.setOpaque(false);
			sender_panel_1.setLayout(new BoxLayout(sender_panel_1,BoxLayout.X_AXIS));
			sender_panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
			sender_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			//打印时间戳
			JLabel time_label = new JLabel(off_mess.getDate(),SwingConstants.LEFT);
			time_label.setForeground(Color.WHITE);
			time_label.setFont(new Font("楷体",Font.PLAIN,10));
			time_panel.add(time_label);
			
			JPanel mymessage_panel = new JPanel();
			mymessage_panel.setOpaque(false);
			mymessage_panel.setAlignmentY(Component.TOP_ALIGNMENT);
			mymessage_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
			mymessage_panel.setLayout(new BoxLayout(mymessage_panel,BoxLayout.Y_AXIS));
			JPanel myhead_panel = new JPanel();
			myhead_panel.setOpaque(false);
			myhead_panel.setAlignmentY(Component.TOP_ALIGNMENT);
			myhead_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
			//打印消息部分
			String message = off_mess.getMessage();
			int column = message.length()/30;
			int j;
			String new_message = "<html>";
			for(j = 0;j<column;j++) {
				new_message = new_message + message.substring(30*j, 30*(j+1)) + "<br>";
			}
			new_message = new_message + message.substring(30*j, message.length()) + "</html>";
			JLabel mymessage_label = new JLabel(new_message);
			mymessage_label.setForeground(Color.WHITE);
			mymessage_label.setFont(new Font("楷体",Font.PLAIN,12));
			mymessage_label.setVerticalTextPosition(SwingConstants.CENTER);
			mymessage_label.setHorizontalTextPosition(SwingConstants.CENTER);
			mymessage_label.setBackground(new Color(0, 10, 20));
			mymessage_label.setBorder(BorderFactory.createLineBorder(new Color(0, 10, 20),5));
			mymessage_label.setOpaque(true);
			JLabel adjust_label = new JLabel(" ");
			mymessage_panel.add(adjust_label);
			mymessage_panel.add(mymessage_label);
			
			//打印用户头像
			ImageIcon head = new ImageIcon("image/friendsui/"+i%5+".jpg");//绘制用户头像
			head.setImage(head.getImage().getScaledInstance(25, 25,Image.SCALE_DEFAULT));
			JLabel myhead_label = new JLabel(off_mess.getSender(),head,SwingConstants.CENTER);
			myhead_label.setFont(new Font("楷体",Font.BOLD,10));
			myhead_label.setAlignmentY(Component.TOP_ALIGNMENT);
			myhead_label.setVerticalTextPosition(SwingConstants.TOP);
			myhead_label.setHorizontalTextPosition(SwingConstants.CENTER);
			myhead_label.setForeground(Color.WHITE);
			myhead_label.setSize(25,25);
			myhead_panel.add(myhead_label);
			sender_panel_1.add(myhead_panel);
			sender_panel_1.add(mymessage_panel);
			
			sender_panel.add(sender_panel_1);
			send_1.add(time_panel);
			send_1.add(sender_panel);
			send.add(send_1);
			panel_1.add(send);
			panel_1.updateUI();
			
			scrollBar = scrollPane.getVerticalScrollBar();
			scrollBar.setValue(scrollBar.getMaximum());
			i++;
		}
	}

}
