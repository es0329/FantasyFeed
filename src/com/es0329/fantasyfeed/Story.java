package com.es0329.fantasyfeed;

/**
 * @author <a href="https://twitter.com/es0329">Eric</a>
 */
public class Story {
	private String id;
	private String title;
	private String link;
	private String imageUrl;

	public Story(String id, String title, String link, String imageUrl) {
		setId(id);
		setTitle(title);
		setLink(link);
		setImageUrl(imageUrl);
	}

	public Story() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ID# = " + getId() + "\tTITLE = " + getTitle() + "\nLINK = "
				+ getLink()
				+ "\nIMAGE_URL = " + getImageUrl();
	}
}
