package http;

import java.util.HashMap;
import java.util.Map;

public class HttpParams {
	
	public String url;
	public Map<String, String> headers = new HashMap<String, String>();//包含content-type,cookie,因为这些都在http头中
	public Map<String, String> params = new HashMap<String, String>();
	 
}
