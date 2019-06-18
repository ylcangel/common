import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES加解密
 * 
 * 
 */
public class AESEncrypt extends BaseEncrypt {

	public AESEncrypt(BaseEncrypt encrypt, String key) {
		super(encrypt, key);
	}

	@Override
	public byte[] encrypt(byte[] inData) throws Exception {// TODO,是否padding
		try {
			if (encrypt != null)
				inData = encrypt.encrypt(inData);

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256, new SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();// TODO
			SecretKeySpec _key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, _key);
			return cipher.doFinal(inData);
		} catch (Exception e) {
			throw new Exception("encrypt failed", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] inData) throws Exception {
		try {
			if (encrypt != null)
				inData = encrypt.encrypt(inData);

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256, new SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();// TODO
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(inData);
		} catch (Exception e) {
			throw new Exception("decrypt failed", e);
		}
	}

}
