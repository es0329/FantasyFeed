package com.es0329.fantasyfeed;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View masterLayout = inflater.inflate(R.layout.master, container, false);
		wireViews(masterLayout);
		checkNewsFeed();

		return masterLayout;
	}

	private void wireViews(View layout) {
		list = (ListView) layout.findViewById(R.id.list);
		progress = (ProgressBar) layout.findViewById(R.id.progress);
		dataCredit = (SmartImageView) layout.findViewById(R.id.dataCredit);
		dataCredit
				.setImageUrl("http://a.espncdn.com/i/apis/attribution/powered-by-espn-silver_200.png");
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

			try {
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
	};

	private void displayStories() {
		adapter = new StoryAdapter(getActivity(), R.layout.story, stories);
		list.setAdapter(adapter);
		progress.setVisibility(View.GONE);
	}
}
