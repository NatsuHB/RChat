package Security;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
*RC4加密、解密
*/
public class RC4 {
	
	//加密
	public String encryptWithRC4(String plain, String key) throws Exception {
		SecretKeySpec secretkey = new SecretKeySpec(key.getBytes(),"RC4");
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, secretkey);
 
        byte[] b2 = cipher.doFinal(plain.getBytes());
        String cipherText = Base64.getEncoder().encodeToString(b2);
        return cipherText;
    }
	
	//解密
	public String decryptWithRC4(String cipherText, String key) throws Exception {
		byte[] b3 = Base64.getDecoder().decode(cipherText);       
        SecretKeySpec secretkey2 = new SecretKeySpec(key.getBytes(),"RC4");
        Cipher cipher2 = Cipher.getInstance("RC4");
        cipher2.init(Cipher.DECRYPT_MODE, secretkey2);
 
        byte[] b1 = cipher2.doFinal(b3);
        String plainText = new String(b1);
        return plainText;
    }


}
