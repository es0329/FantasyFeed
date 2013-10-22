package com.es0329.fantasyfeed;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * HTTP client class with static accessors to communicate with ESPN's API.
 * 
 * @author <a href="https://twitter.com/es0329">Eric</a>
 */
public class Espn {
	protected static final String BASE_URL = "http://api.espn.com/v1/fantasy/football/news?";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
