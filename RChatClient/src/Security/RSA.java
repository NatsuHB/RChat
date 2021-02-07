package Security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
/**
*RSA
*1.随机生成RSA密钥对
*2.RSA加密、解密
*/
public class RSA {
	private RSAPublicKey rsaPubKey;
	private RSAPrivateKey rsaPriKey;
	private KeyPairGenerator keyGen;
	public RSA() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
		try {
			keyGen = KeyPairGenerator.getInstance("RSA", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		keyGen.initialize(512,new SecureRandom());
	}
	
	//随机生成RSA密钥对
	public void GenerateKey() throws Exception {
	    KeyPair keyPair = keyGen.generateKeyPair();
	    this.rsaPubKey = (RSAPublicKey)keyPair.getPublic();
	    this.rsaPriKey = (RSAPrivateKey)keyPair.getPrivate();
	}
	
	public String getPubKey() {
		return Base64.getEncoder().encodeToString(rsaPubKey.getEncoded());
	}
	
	public String getPriKey() {
		return Base64.getEncoder().encodeToString(rsaPriKey.getEncoded());
	}
	
	//加密
	public String encode(String data,String key) throws Exception {
		byte[] FriKeyByte = Base64.getDecoder().decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(FriKeyByte);
		rsaPubKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, rsaPubKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
	}
	
	//解密
	public String decode(String data,String key) throws Exception {
		byte[] MyKeyByte = Base64.getDecoder().decode(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(MyKeyByte);
		rsaPriKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.DECRYPT_MODE, rsaPriKey);
	    return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
	}
}
