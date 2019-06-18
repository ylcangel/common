package http;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * 基于http connection实现的http utils
 * 
 * @author ylc
 * 
 */
public class HttpUtils {

	/**
	 * Http Get 请求
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            response回调函数
	 * 
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpGet(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			String requestUrl = null;
			if (Utils.isNotEmpty(httpParams.params)) {
				requestUrl = createRequestUrl(httpParams.params, 1, true);
				httpParams.url += requestUrl;
			}

			URL url = new URL(httpParams.url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (Utils.isNotEmpty(httpParams.headers)) {
				for (String header : httpParams.headers.keySet()) {
					connection.addRequestProperty(header, httpParams.headers.get(header));
				}
			}

			connection.connect();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			connection.disconnect();
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * https 不验证证书的get请求
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            对响应的回调函数
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpsGetNoCert(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			String uri = null;
			if (Utils.isNotEmpty(httpParams.params)) {
				uri = createRequestUrl(httpParams.params, 1, true);
				httpParams.url += uri;
			}

			URL url = new URL(httpParams.url);
			SSLContext sslContext = null;
			TrustManager[] trustManager = new TrustManager[] { new TrustAllCertsManager() };

			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManager, new java.security.SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setHostnameVerifier(new VerifyEverythingHostnameVerifier());

			for (String key : httpParams.headers.keySet()) {
				connection.addRequestProperty(key, httpParams.headers.get(key));
			}

			connection.connect();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			connection.disconnect();
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * https的get请求
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            对响应的回调函数
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpsGet(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			String uri = null;
			if (Utils.isNotEmpty(httpParams.params)) {
				uri = createRequestUrl(httpParams.params, 1, true);
				httpParams.url += uri;
			}

			URL url = new URL(httpParams.url);
			SSLContext sslContext = SSLContext.getDefault();

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

			for (String key : httpParams.headers.keySet()) {
				connection.addRequestProperty(key, httpParams.headers.get(key));
			}

			connection.connect();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			connection.disconnect();
			return obj;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * http post 请求
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            响应回调
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpPost(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			URL url = new URL(httpParams.url);
			URLConnection _connection = url.openConnection();
			HttpURLConnection connection = (HttpURLConnection) _connection;

			if (Utils.isNotEmpty(httpParams.headers)) {
				for (String header : httpParams.headers.keySet()) {
					connection.addRequestProperty(header, httpParams.headers.get(header));
				}
			}

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");

			String data = createRequestUrl(httpParams.params, 2, true);
			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes());
			os.flush();

			connection.connect();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			os.close();
			connection.disconnect();
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * https post 请求,不验证证书host
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            响应回调
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpsPostNoCert(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			URL url = new URL(httpParams.url);
			SSLContext sslContext = null;
			TrustManager[] trustManager = new TrustManager[] { new TrustAllCertsManager() };

			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManager, new java.security.SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setHostnameVerifier(new VerifyEverythingHostnameVerifier());

			if (Utils.isNotEmpty(httpParams.headers)) {
				for (String header : httpParams.headers.keySet()) {
					connection.addRequestProperty(header, httpParams.headers.get(header));
				}
			}

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.connect();

			String data = createRequestUrl(httpParams.params, 2, false);
			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes());
			os.flush();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			os.close();
			connection.disconnect();
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * https post请求
	 * 
	 * @param httpParams
	 *            参数
	 * @param response
	 *            响应回调
	 * @return 返回处理对象
	 * @throws Exception
	 *             异常
	 */
	public static Object doHttpsPost(HttpParams httpParams, HttpResponse response) throws Exception {
		try {
			URL url = new URL(httpParams.url);
			SSLContext sslContext = null;

			sslContext = SSLContext.getDefault();
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

			if (Utils.isNotEmpty(httpParams.headers)) {
				for (String header : httpParams.headers.keySet()) {
					connection.addRequestProperty(header, httpParams.headers.get(header));
				}
			}

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			connection.connect();

			String data = createRequestUrl(httpParams.params, 2, false);
			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes());
			os.flush();

			Object obj = null;
			if (response != null) {
				response.setCon(connection);
				obj = response.callback();
				response.getIn().close();
			}

			os.close();
			connection.disconnect();
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 用于生成get请求后面的request url部分
	 * 
	 * @param params
	 *            参数
	 * @param type
	 *            1 get 2 post
	 * @param isEncoding
	 *            是否编码
	 * @return request url
	 */
	@SuppressWarnings("deprecation")
	private static String createRequestUrl(Map<String, String> params, int type, boolean isEncoding) {
		StringBuffer _uri = new StringBuffer();
		if (type == 1)
			_uri.append("?");

		String uri = null;

		for (String param : params.keySet()) {
			_uri.append(param).append("=").append(params.get(param)).append("&");
		}

		String uri_ = _uri.toString();

		if (isEncoding)
			uri = URLEncoder.encode(uri_.substring(0, uri_.length() - 1));
		else
			uri = uri_.substring(0, uri_.length() - 1);

		System.out.println(uri);

		return uri;
	}

	/**
	 * 打印http头信息
	 * 
	 * @param headers
	 *            头列表
	 */
	public static void printHeaders(Map<String, List<String>> headers) {
		if (Utils.isNotEmpty(headers)) {
			System.out.println("Header info:");
			for (String key : headers.keySet()) {
				List<String> props = headers.get(key);
				for (String prop : props) {
					if (key == null)
						System.out.println(prop);
					else
						System.out.println(key + ":" + prop);
				}
			}
		}
	}

	/**
	 * 保存server发来的Set-Cookie为client端的Cookie
	 * 
	 * @param headers
	 * @param outcookies
	 */
	public static void getServerCookies(Map<String, List<String>> headers, Map<String, String> outcookies) throws Exception {
		if (Utils.isNotEmpty(headers)) {
			for (String key : headers.keySet()) {
				if (key != null && key.equalsIgnoreCase("Set-Cookie")) {
					List<String> props = headers.get(key);
					for (String prop : props) {
						String[] cooks = prop.split("=");
						if (cooks.length < 2)
							throw new Exception("Set-Cookie string was error!\n");
						System.out.println(cooks[0] + ">>" + prop);
						if (outcookies.containsKey(cooks[0]))
							outcookies.remove(cooks[0]);
						outcookies.put(cooks[0], prop);
					}
				}
			}
		}
	}

	/**
	 * 把map中的cookie连接成 cookie1; cookie2;cookie3这样的格式
	 * 
	 * @param cookies
	 *            传人cookies
	 * @return 待传人的cookie，注意不包含 Cookie：
	 */
	public static String createCookieString(Map<String, String> cookies) {
		StringBuffer cookie = new StringBuffer();
		if (Utils.isNotEmpty(cookies)) {
			for (String key : cookies.keySet()) {
				cookie.append(cookies.get(key)).append(";");
			}
			String ck = cookie.substring(0, cookie.toString().length() - 1);
			System.out.println(ck);
			return ck;
		}
		return null;
	}
}

/**
 * 信任所有证书，一般用于测试，或者本身证书就有问题的情况， 正常情况下不建议使用，会被中间人攻击
 * 
 */
class TrustAllCertsManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}

/**
 * 信任所有host，同上
 * 
 */
class VerifyEverythingHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}
