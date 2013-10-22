package com.es0329.fantasyfeed;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	private ArrayList<Story> stories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkNewsFeed();
	}

	private void checkNewsFeed() {
		RequestParams params = new RequestParams("apikey", getResources()
				.getString(R.string.api_key));
		Espn.get("", params, reponseHandler);
	}

	private JsonHttpResponseHandler reponseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			stories = new ArrayList<Story>();
			Story tempStory = new Story();

			try {
				JSONArray headlines = response.getJSONArray("headlines");
				JSONObject result = new JSONObject();
				JSONObject links = new JSONObject();
				JSONObject mobile = new JSONObject();

				for (int i = 0; i < headlines.length(); i++) {
					result = headlines.getJSONObject(i);

					tempStory.setId(result.optString("id"));
					tempStory.setTitle(result.optString("title"));

					links = result.optJSONObject("links");
					mobile = links.getJSONObject("mobile");
					tempStory.setLink(mobile.optString("href"));

					JSONArray images = result.getJSONArray("images");

					if (!images.isNull(0)) {
						JSONObject url = images.getJSONObject(0);

						if (url.getString("type").contains("header")) {
							tempStory.setImageUrl(url.getString("url"));
						}
					} else {
						tempStory.setImageUrl("");
					}
					stories.add(tempStory);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
}
