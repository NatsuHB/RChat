package Frame;

import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import UserSocket.fileClient;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Get_File implements ActionListener {

	
	private JFrame frame;
	private JTabbedPane tabPane = new JTabbedPane();
	private Container con = new Container();
	private JLabel chooseFile = new JLabel("ѡ���ļ�");
	private JButton choose = new JButton("...");
	private JButton send = new JButton("ȷ��");
	private JFileChooser jfc = new JFileChooser();
	private JTextField text = new JTextField();
	private fileClient client;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Send_File window = new Send_File();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public Get_File(fileClient client) {
		frame=new JFrame("�����ļ�");
		this.client = client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		jfc.setCurrentDirectory(new File("d:\\"));//�ļ�ѡ�����ĳ�ʼĿ¼��Ϊd��
        //����������ȡ����Ļ�ĸ߶ȺͿ��
        double lx=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setBounds((int)(lx-300)/2, (int)(ly-130)/2, 300, 130);
        frame.setContentPane(tabPane);//���ò���
       //�����趨��ǩ�ȵĳ���λ�ú͸߿�
        chooseFile.setBounds(10,10,70,25);
        text.setBounds(80,10,120,25);
        choose.setBounds(210,10,50,25);
        send.setBounds(100, 45, 80, 20);
        send.addActionListener(this);
        choose.addActionListener(this);//����¼�����
        con.add(chooseFile);
        con.add(text);
        con.add(choose);
        con.add(send);
        con.add(jfc);
        tabPane.add("Ŀ¼/�ļ�ѡ��",con);//��Ӳ���
        frame.setVisible(true);//���ڿɼ�
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//ʹ�ܹرմ��ڣ���������
	}
	@Override
	public void actionPerformed(ActionEvent e){//�¼�����
        if(e.getSource().equals(choose)){//�жϴ��������İ�ť���ĸ�
            jfc.setFileSelectionMode(1);
            int state=jfc.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
            if(state==1){
                return;//�����򷵻�
            }
            else{
                File f=jfc.getSelectedFile();//fΪѡ�񵽵�Ŀ¼
                text.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource() == send) {
        	String filepath = text.getText();
        	System.out.println(filepath);
        	try {
				String size = client.FileGet(filepath);
				JOptionPane.showMessageDialog(frame, "������ϣ��ļ���СΪ"+size);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	frame.dispose();
        }
    }
}
