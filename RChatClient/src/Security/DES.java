package Security;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
*DES
*1.�������DES��Կ
*2.DES���ܡ�����
*/

public class DES {
    // ��ʼ����/ƫ����
    private static String ivStr = "166290ac";
    private static byte[] iv = ivStr.getBytes();
    
    //���������Կ
    public String getkey() throws Exception {
    	SecureRandom sr = new SecureRandom();
    	KeyGenerator kg = KeyGenerator.getInstance("DES");
    	kg.init(sr);
    	Key kkey = kg.generateKey();
    	byte[] bkey = kkey.getEncoded();
    	String key = new String(bkey);
    	return key;
    }

    //����
    public String encryptDES(String encryptString, String key) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec secertkey = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secertkey, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

        return Base64.getEncoder().encodeToString(encryptedData);
    }


    //����
    public String decryptDES(String decryptString, String key) throws Exception {
        byte[] byteMi = Base64.getDecoder().decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec secertkey = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secertkey, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }
}
