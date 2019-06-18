package http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



public class Utils {

	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 验证传人对象是否为空
	 * 
	 * @param obj
	 *            传人对象
	 * @return true 非空， false 空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Object obj) {
		if (obj instanceof Map) {
			return (obj != null && ((Map) obj).size() > 0);
		} else if (obj instanceof List) {
			return (obj != null && ((List) obj).size() > 0);
		} else if (obj instanceof String) {
			return (obj != null && ((String) obj).length() > 0 && ((String) obj) != "");
		}
		return false;
	}

	/**
	 * 把input流中转换为字符
	 * 
	 * @param in
	 *            input流
	 * @param charset
	 *            字符集
	 * @return 转换后的字符串
	 */
	public static String writeToString(InputStream in, String charset) {
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));

			char[] c = new char[1024];
			int off;
			while ((off = br.read(c, 0, 1024)) != -1) {
				sb.append(c, 0, off);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 写文件
	 * 
	 * @param file
	 *            文件
	 * @param data
	 *            数据
	 * @throws IOException
	 */
	public static void writeToFile(File file, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		bos.write(data);
		bos.flush();
		bos.close();
		fos.close();
	}

	/**
	 * 从文件中读取字符串
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static String readFromFile(String path) throws Exception {
		File file = new File(path);

		if (file == null || !file.exists())
			throw new IOException("file : " + path + " not found!");

		try {
			StringBuffer content = new StringBuffer();
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tmp;

			while ((tmp = br.readLine()) != null)
				content.append(tmp);

			br.close();
			in.close();

			return content.toString();
		} catch (Exception e) {
			throw new Exception("read from file : " + path + " failed!", e);
		}
	}

	/**
	 * 向文件中写内容，并且该文件具备全局可读写
	 * 
	 * @param path
	 *            文件路径
	 * @param content
	 *            内容
	 * @throws Exception
	 */
	public static void saveToFile(String path, String content) throws Exception {
		File file = new File(path);

		File folder = file.getParentFile();
		if (folder == null || !folder.exists())
			folder.mkdir();

		if (!file.exists())
			file.createNewFile();

		file.setReadable(true, false);
		file.setWritable(true, false);

		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		bos.write(content.getBytes("UTF-8"));
		bos.flush();
		bos.close();
		fos.close();

	}

	/**
	 * 字符串转换成十六进制字符串
	 * 
	 * @param String
	 *            str 待转换的ASCII字符串
	 * @return
	 */
	public static String str2HexStr(String str) {

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	/**
	 * 十六进制转换字符串
	 * 
	 * @param String
	 *            str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param byte[] b byte数组
	 * @return
	 */
	public static String byte2HexStr(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().trim();
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *            src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
		}
		return ret;
	}

	/**
	 * String的字符串转换成unicode的String
	 * 
	 * @param String
	 *            strText 全角字符串
	 * @return String 每个unicode之间无分隔符
	 * @throws Exception
	 */
	public static String strToUnicode(String strText) throws Exception {
		char c;
		StringBuilder str = new StringBuilder();
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = (int) c;
			strHex = Integer.toHexString(intAsc);
			if (intAsc > 128)
				str.append("\\u" + strHex);
			else
				// 低位在前面补00
				str.append("\\u00" + strHex);
		}
		return str.toString();
	}

	/**
	 * unicode的String转换成String的字符串
	 * 
	 * @param String
	 *            hex 16进制值字符串 （一个unicode为2byte）
	 * @return String 全角字符串
	 */
	public static String unicodeToString(String hex) {
		int t = hex.length() / 6;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < t; i++) {
			String s = hex.substring(i * 6, (i + 1) * 6);
			// 高位需要补上00再转
			String s1 = s.substring(2, 4) + "00";
			// 低位直接转
			String s2 = s.substring(4);
			// 将16进制的string转为int
			int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
			// 将int转换为字符
			char[] chars = Character.toChars(n);
			str.append(new String(chars));
		}
		return str.toString();
	}

	/**
	 * 计算字符串的SHA-1值
	 * 
	 * @param input
	 *            字符串
	 * @return String
	 */
	public static String sha1(String input) {

		byte[] data = input.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(data);
			return byte2HexStr(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 计算字符串的SHA-1值
	 * 
	 * @param input
	 *            字符串
	 * @return String
	 */
	public static String sha1(byte[] data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(data);
			return byte2HexStr(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 计算文件的SHA-1值
	 * 
	 * @param file
	 *            文件
	 * @return String
	 */
	public static String sha1(File file) {
		String sha1 = "";
		if (file.exists()) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				byte[] cache = new byte[2048];
				int len;
				while ((len = in.read(cache)) != -1) {
					messageDigest.update(cache, 0, len);
				}
				in.close();
				byte[] data = messageDigest.digest();
				sha1 = byte2HexStr(data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sha1;
	}

	/**
	 * 计算字符串的MD5值
	 * 
	 * @param input
	 *            字符串
	 * @return String
	 */
	public static String md5(String input) {

		byte[] data = input.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data);
			return byte2HexStr(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 计算字符串的MD5值
	 * 
	 * @param input
	 *            字符串
	 * @return String
	 */
	public static String md5(byte[] data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data);
			return byte2HexStr(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 计算文件的MD5值
	 * 
	 * @param file
	 *            文件
	 * @return String
	 */
	public static String md5FileDigest(File file) {

		String md5 = "";
		if (file.exists()) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				byte[] cache = new byte[2048];
				int len;
				while ((len = in.read(cache)) != -1) {
					messageDigest.update(cache, 0, len);
				}
				in.close();
				byte[] data = messageDigest.digest();
				md5 = byte2HexStr(data);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return md5;
	}

	/*
	 * @param srcFile 需解压的文件
	 * 
	 * @param dstPath 解压路径
	 * 
	 * @param entryname 解压指定entry
	 */
	public static void unzipEntry(File srcFile, String entryname, String dstPath) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
			ZipInputStream zis = new ZipInputStream(bis);

			BufferedOutputStream bos = null;

			byte[] b = new byte[1024];
			ZipEntry entry = null;

			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();
				if (entryName.startsWith(entryname)) {
					String dir = entryName.substring(0, entryName.lastIndexOf("/"));
					File dstFile = new File(dstPath + File.separator + dir);
					if (!dstFile.exists())
						dstFile.mkdirs();

					String fname = entryName.substring(entryName.lastIndexOf("/") + 1);
					File f = new File(dstFile, fname);
					if (!f.exists())
						f.createNewFile();

					bos = new BufferedOutputStream(new FileOutputStream(f));
					int len;
					while ((len = zis.read(b)) != -1) {
						bos.write(b, 0, len);
					}

					bos.flush();
					bos.close();
				}
			}

			
			zis.close();
			bis.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * 判断zip中是否包含某项
	 * @param srcFile
	 * @param entryname
	 * @return
	 */
	public static boolean isZipHasEntry(File srcFile, String entryname) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
			ZipInputStream zis = new ZipInputStream(bis);

			ZipEntry entry = null;

			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();
				if (entryName.startsWith(entryname)) {
					
					zis.close();
					bis.close();
					return true;
				}
			}
			
			zis.close();
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * 递归删除目录
	 * 
	 * @param path
	 */
	public static boolean delete(String path) {
		try {
			File f = new File(path);
			if (!f.exists())
				return true;

			if (f.isDirectory()) {// 如果是目录，先递归删除
				String[] list = f.list();
				for (int i = 0; i < list.length; i++) {
					delete(path + "/" + list[i]);// 先删除目录下的文件
				}
			}
			f.delete();
			return true;
		} catch (Exception e) {
			// donothing
			return false;
		}
	}
}
