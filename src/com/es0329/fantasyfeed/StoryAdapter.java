package com.es0329.fantasyfeed;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

/**
 * @author <a href="https://twitter.com/es0329">Eric</a>
 */
public class StoryAdapter extends ArrayAdapter<Story> {
	private ArrayList<Story> stories = new ArrayList<Story>();
	private Story currentStory;
	private Holder holder;
	private LayoutInflater inflater;

	public StoryAdapter(Context context, int resourceId,
			ArrayList<Story> stories) {
		super(context, resourceId, stories);
		this.stories = stories;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		holder = null;

		if (row == null) {
			row = inflater.inflate(R.layout.story, parent, false);
			holder = new Holder();
			holder.image = (SmartImageView) row.findViewById(R.id.image);
			holder.title = (TextView) row.findViewById(R.id.title);
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		currentStory = stories.get(position);
		holder.image.setImageUrl(currentStory.getImageUrl());
		holder.title.setText(currentStory.getTitle());

		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browser = new Intent(Intent.ACTION_VIEW, Uri
						.parse(stories.get(position).getLink()));
				getContext().startActivity(browser);
			}
		});
		return row;
	}

	static class Holder {
		SmartImageView image;
		TextView title;
		String link;
	}
}
