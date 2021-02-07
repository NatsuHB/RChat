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
	private JLabel chooseFile = new JLabel("选择文件");
	private JButton choose = new JButton("...");
	private JButton send = new JButton("确定");
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
		frame=new JFrame("接受文件");
		this.client = client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		jfc.setCurrentDirectory(new File("d:\\"));//文件选择器的初始目录定为d盘
        //下面两行是取得屏幕的高度和宽度
        double lx=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setBounds((int)(lx-300)/2, (int)(ly-130)/2, 300, 130);
        frame.setContentPane(tabPane);//设置布局
       //下面设定标签等的出现位置和高宽
        chooseFile.setBounds(10,10,70,25);
        text.setBounds(80,10,120,25);
        choose.setBounds(210,10,50,25);
        send.setBounds(100, 45, 80, 20);
        send.addActionListener(this);
        choose.addActionListener(this);//添加事件处理
        con.add(chooseFile);
        con.add(text);
        con.add(choose);
        con.add(send);
        con.add(jfc);
        tabPane.add("目录/文件选择",con);//添加布局
        frame.setVisible(true);//窗口可见
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//使能关闭窗口，结束程序
	}
	@Override
	public void actionPerformed(ActionEvent e){//事件处理
        if(e.getSource().equals(choose)){//判断触发方法的按钮是哪个
            jfc.setFileSelectionMode(1);
            int state=jfc.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
            if(state==1){
                return;//撤销则返回
            }
            else{
                File f=jfc.getSelectedFile();//f为选择到的目录
                text.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource() == send) {
        	String filepath = text.getText();
        	System.out.println(filepath);
        	try {
				String size = client.FileGet(filepath);
				JOptionPane.showMessageDialog(frame, "接收完毕，文件大小为"+size);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	frame.dispose();
        }
    }
}
