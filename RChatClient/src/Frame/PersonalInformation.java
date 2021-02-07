package Frame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class PersonalInformation implements ActionListener {

	private JFrame frame;
	private JButton button;
	private JPanel panel;
	private JLabel label;
	private JTextArea textArea;

	/**
	 * Create the application.
	 */
	public PersonalInformation(String text) {
		initialize(text);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String text) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		label = new JLabel("\u60A8\u7684\u79C1\u94A5\u4E3A\uFF1A");
		label.setAlignmentX(label.CENTER_ALIGNMENT);
		panel.add(label);
		
		textArea = new JTextArea(text);
		textArea.setEditable(false);
		JScrollPane scrollpane = new JScrollPane(textArea);
		panel.add(scrollpane);
		
		button = new JButton("È·¶¨");
		button.setAlignmentX(button.CENTER_ALIGNMENT);
		button.addActionListener(this);
		panel.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==button) {
			frame.dispose();
		}
	}
	

}
