package UserSocket;

/**
 * ��������ڴ����ļ�
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

public class fileClient {
	private int port = 2224;
	private Socket socket;
	
	//�����ļ����öԷ�IP��������
	public fileClient(String Friend_address) {
		try {
			socket = new Socket(Friend_address, 2224);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������ʧ��");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "��������ʧ��");
		}
		
	}
	
	//��������socket�����ļ�������
	public fileClient(Socket Friend_socket) {
		socket = Friend_socket;
		
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	} 
	
	//�����ļ�
	public void FileSend(String FileName) throws Exception {
		File file = new File(FileName);
		FileInputStream fis = null;
		DataOutputStream dos = null;
		try {
			if(file.exists()) {
				fis = new FileInputStream(file);
				dos = new DataOutputStream(socket.getOutputStream());
				
				dos.writeUTF(file.getName());
				dos.flush();
				dos.writeLong(file.length());
				dos.flush();
				
				byte[] bytes = new byte[1024];
				int length = 0;
				long progress = 0;
				while((length = fis.read(bytes, 0, bytes.length)) != -1) {
					dos.write(bytes, 0, length);
					dos.flush();
				}
			}
			else {
				System.out.println("�Ҳ����ļ�");
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if(fis != null)
				fis.close();
			if(dos != null)
				dos.close();
		}
		
	}
	
	//�����ļ�
	public String FileGet(String FilePath) {
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try {
			dis = new DataInputStream(socket.getInputStream());
			
			String fileName = dis.readUTF();
			long file_length = dis.readLong();
			File directory = new File(FilePath);
			if(!directory.exists()) {
				directory.mkdir();
			}
			File file = new File(directory.getAbsolutePath() + File.separatorChar+fileName);
			fos = new FileOutputStream(file);
			
			byte[] bytes = new byte[1024];
			int length = 0;
			while((length = dis.read(bytes, 0, bytes.length)) !=-1) {
				fos.write(bytes, 0, length);
				fos.flush();
			}
			
			String f_length = getFileSize(file_length);
			return f_length;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(fos != null) {
					fos.close();
				}
				if(dis != null) {
					dis.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "�������";
		
	}
	
	//����ļ���С
	private String getFileSize(long length) {
		DecimalFormat df = new DecimalFormat("#0.0");
		double size = ((double) length) / (1 << 30);  
        if(size >= 1) {  
            return df.format(size) + "GB";  
        }  
        size = ((double) length) / (1 << 20);  
        if(size >= 1) {  
            return df.format(size) + "MB";  
        }  
        size = ((double) length) / (1 << 10);  
        if(size >= 1) {  
            return df.format(size) + "KB";  
        }  
        return length + "B";
	}

}