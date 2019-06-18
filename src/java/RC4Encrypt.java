/**
 * rc4解密
 * 
 * 
 */
public class RC4Encrypt extends BaseEncrypt {

	public RC4Encrypt(BaseEncrypt encrypt, String key) {
		super(encrypt, key);
	}

	@Override
	public byte[] encrypt(byte[] inData) throws Exception {
		if (encrypt != null)
			inData = encrypt.encrypt(inData);

		return rc4(inData);

	}

	@Override
	public byte[] decrypt(byte[] inData) throws Exception {
		if (encrypt != null)
			inData = encrypt.encrypt(inData);

		return rc4(inData);

	}

	private byte[] rc4(byte[] inData) {
		int x = 0;
		int y = 0;
		byte _key[] = initKey(key);
		int xorIndex;
		byte[] result = new byte[inData.length];

		for (int i = 0; i < inData.length; i++) {
			x = (x + 1) & 0xff;
			y = ((_key[x] & 0xff) + y) & 0xff;
			byte tmp = _key[x];
			_key[x] = _key[y];
			_key[y] = tmp;
			xorIndex = ((_key[x] & 0xff) + (_key[y] & 0xff)) & 0xff;
			result[i] = (byte) (inData[i] ^ _key[xorIndex]);
		}
		return result;
	}

	private byte[] initKey(String aKey) {
		byte[] b_key = aKey.getBytes();
		byte state[] = new byte[256];

		for (int i = 0; i < 256; i++) {
			state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		if (b_key == null || b_key.length == 0) {
			return null;
		}
		for (int i = 0; i < 256; i++) {
			index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % b_key.length;
		}
		return state;
	}

}
