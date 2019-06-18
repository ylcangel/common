/**
 * Encrypt基类，实现装饰器模式,如果传人encrypt为空只执行实例化实例的加解密，否则一层层执行<br/>
 * 如：<br/>
 * Encrypt encryptor = new AESEncrypt(new RC4Encrypt(null, key1), key2) <br/>
 * encryptor.decrypt <br/>
 * 上面的代码执行了两次解密，首先是rc4解密，再次是Aes解密
 * 
 * 
 * 
 */
public abstract class BaseEncrypt {

	protected volatile BaseEncrypt encrypt;
	protected String key;

	public BaseEncrypt(BaseEncrypt encrypt, String key) {
		this.encrypt = encrypt;
		this.key = key;
	}


	/**
	 * 加密
	 * @param inData 输入加密数据
	 * @return 解密后数据
	 * @throws Exception
	 */
	public abstract byte[] encrypt(byte[] inData) throws Exception;


	/**
	 * 解密
	 * @param inData 输入加密数据
	 * @return 解密后数据
	 * @throws Exception
	 */
	public abstract byte[] decrypt(byte[] inData) throws Exception;


	public BaseEncrypt getEncrypt() {
		return encrypt;
	}


	public void setEncrypt(BaseEncrypt encrypt) {
		this.encrypt = encrypt;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}
	
}
