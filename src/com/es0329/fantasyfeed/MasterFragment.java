package com.es0329.fantasyfeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

/**
 * @author <a href="https://twitter.com/es0329">Eric</a>
 */
public class MasterFragment extends Fragment {
	private ListView list;
	private ProgressBar progress;
	private SmartImageView dataCredit;
	private StoryAdapter adapter;
	private Story tempStory;
	private ArrayList<Story> stories;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View masterLayout = inflater.inflate(R.layout.master, container, false);
		wireViews(masterLayout);
		// checkNewsFeed();
		CheckNews checkNews = new CheckNews();
		checkNews.execute();

		return masterLayout;
	}

	private void wireViews(View layout) {
		list = (ListView) layout.findViewById(R.id.list);
		progress = (ProgressBar) layout.findViewById(R.id.progress);
		dataCredit = (SmartImageView) layout.findViewById(R.id.dataCredit);
		dataCredit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browser = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://espn.go.com/"));
				startActivity(browser);
			}
		});
	}

	// private void checkNewsFeed() {
	// progress.setVisibility(View.VISIBLE);
	// RequestParams params = new RequestParams("apikey", getResources()
	// .getString(R.string.api_key));
	// Espn.get("", params, reponseHandler);
	// }

	// private JsonHttpResponseHandler reponseHandler = new
	// JsonHttpResponseHandler() {
	//
	// @Override
	// public void onSuccess(JSONObject response) {
	// stories = new ArrayList<Story>();
	//
	// // Log.i("RESPONSE", response);
	//
	// try {
	// JSONArray headlines = response.getJSONArray("headlines");
	// JSONObject result = new JSONObject();
	// JSONObject links = new JSONObject();
	// JSONObject mobile = new JSONObject();
	//
	// for (int i = 0; i < headlines.length(); i++) {
	// tempStory = new Story();
	// result = headlines.getJSONObject(i);
	//
	// tempStory.setId(result.optString("id"));
	// tempStory.setTitle(result.optString("title"));
	//
	// links = result.optJSONObject("links");
	// mobile = links.getJSONObject("mobile");
	// tempStory.setLink(mobile.optString("href"));
	//
	// JSONArray images = result.getJSONArray("images");
	//
	// if (!images.isNull(0)) {
	// JSONObject url = images.getJSONObject(0);
	// tempStory.setImageUrl(url.getString("url"));
	// } else {
	// tempStory.setImageUrl("");
	// }
	// stories.add(tempStory);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// displayStories();
	// }
	// };

	private class CheckNews extends AsyncTask<String, Void, String> {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams asyncParams;
		HttpGet getPost;
		String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			asyncParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(asyncParams, 30000);
			HttpConnectionParams.setSoTimeout(asyncParams, 30000);
			getPost = new HttpGet(Espn.BASE_URL + "apikey="
					+ getActivity().getResources().getString(R.string.api_key));
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				Log.i("GET", getPost.getURI().toString());
				HttpResponse response = client.execute(getPost);
				result = inputStreamToString(response.getEntity().getContent())
						.toString();
			} catch (ConnectTimeoutException e) {
				Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT)
						.show();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		private StringBuilder inputStreamToString(InputStream stream) {
			String line = "";
			StringBuilder result = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));

			try {
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				buildArticleList(result);
			} catch (Exception E) {
				Toast.makeText(getActivity(), "Error:" + E.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void buildArticleList(String rawJson) {
		stories = new ArrayList<Story>();

		Log.i("RESPONSE", rawJson);

		try {
			JSONObject response = new JSONObject(rawJson);
			JSONArray headlines = response.getJSONArray("headlines");
			JSONObject result = new JSONObject();
			JSONObject links = new JSONObject();
			JSONObject mobile = new JSONObject();

			for (int i = 0; i < headlines.length(); i++) {
				tempStory = new Story();
				result = headlines.getJSONObject(i);

				tempStory.setId(result.optString("id"));
				tempStory.setTitle(result.optString("title"));

				links = result.optJSONObject("links");
				mobile = links.getJSONObject("mobile");
				tempStory.setLink(mobile.optString("href"));

				JSONArray images = result.getJSONArray("images");

				if (!images.isNull(0)) {
					JSONObject url = images.getJSONObject(0);
					tempStory.setImageUrl(url.getString("url"));
				} else {
					tempStory.setImageUrl("");
				}
				stories.add(tempStory);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		displayStories();
	}

	private void displayStories() {
		adapter = new StoryAdapter(getActivity(), R.layout.story, stories);
		list.setAdapter(adapter);
		progress.setVisibility(View.GONE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			// checkNewsFeed();
			CheckNews checkNews = new CheckNews();
			checkNews.execute();
			return true;
		case R.id.about:
			showDialog(getActivity());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showDialog(Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(context.getResources().getString(R.string.about));

		WebView wv = new WebView(context);
		wv.loadUrl("file:///android_asset/credits.html");

		alert.setView(wv);
		alert.setNegativeButton(context.getResources()
				.getString(R.string.close), null);
		alert.show();
	}
}
