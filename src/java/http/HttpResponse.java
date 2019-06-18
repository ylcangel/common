package http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class HttpResponse {
	
	private HttpURLConnection con = null;
	
	public abstract Object callback() throws Exception;

	public InputStream getIn() throws Exception{
		try {
			return con.getInputStream();
		} catch (IOException e) {
			throw e;
		}
	}

	public HttpURLConnection getCon() {
		return con;
	}

	public void setCon(HttpURLConnection con) {
		this.con = con;
	}
	

}
