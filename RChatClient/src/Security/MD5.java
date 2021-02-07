package Security;

import java.security.MessageDigest;
import java.util.Base64;
/**
*MD5
*/
public class MD5 {
	//Éú³ÉMD5
	public String getMD5(String message) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(message.getBytes("utf-8"));
		message = Base64.getEncoder().encodeToString(digest);
		return message;
	}

}
