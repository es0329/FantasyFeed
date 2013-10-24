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
	private SmartImageView dataCredit;
	private ProgressBar progress;
	private ListView list;

	private ArrayList<Story> stories;
	private StoryAdapter adapter;
	private Story tempStory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.master, container, false);
		wireViews(layout);
		setListeners();

		new CheckNews().execute();
		return layout;
	}

	private void wireViews(View layout) {
		list = (ListView) layout.findViewById(R.id.list);
		progress = (ProgressBar) layout.findViewById(R.id.progress);
		dataCredit = (SmartImageView) layout.findViewById(R.id.dataCredit);
	}

	private void setListeners() {
		dataCredit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browser = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://espn.go.com/"));
				startActivity(browser);
			}
		});
	}

	private class CheckNews extends AsyncTask<String, Void, String> {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params;
		HttpGet getPost;
		String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 30000);
			HttpConnectionParams.setSoTimeout(params, 30000);
			getPost = new HttpGet(
					"http://api.espn.com/v1/fantasy/football/news?apikey="
							+ getActivity().getResources().getString(
									R.string.api_key));
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			stories = new ArrayList<Story>();

			try {
				HttpResponse response = client.execute(getPost);
				result = inputStreamToString(response.getEntity().getContent())
						.toString();
			} catch (ConnectTimeoutException e) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.no_network),
						Toast.LENGTH_SHORT).show();
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
				Toast.makeText(
						getActivity(),
						getResources().getString(R.string.error)
								+ E.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void buildArticleList(String rawJson) {

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
			new CheckNews().execute();
			return true;
		case R.id.about:
			displayCredits(getActivity());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void displayCredits(Context context) {
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
