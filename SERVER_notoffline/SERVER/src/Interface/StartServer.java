package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import _Socket.Service;

public class StartServer extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton startServer_btn;
	private JButton endServer_btn;
	
	public StartServer() {
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		startServer_btn = new JButton("START");
		endServer_btn = new JButton("CLOSE");
		
		f.add(p);
		p.add(startServer_btn);
		p.add(endServer_btn);
		f.setSize(300,80);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		startServer_btn.addActionListener(this);
		endServer_btn.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startServer_btn) {
			new startServerThread().start();
			JOptionPane.showMessageDialog(null, "The server is ready!");
		}
		if(e.getSource() == endServer_btn) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		StartServer start_server = new StartServer();
	}
}

class startServerThread extends Thread{
	@Override
	public void run() {
		//´´½¨socket
		Service s = new Service();
		s.startService();
	}
}
