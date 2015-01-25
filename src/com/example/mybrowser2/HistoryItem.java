package com.example.mybrowser2;

public class HistoryItem {
	private String title;
	private String url;
	private int timestamp;
	
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public HistoryItem(String title, String url, int timestamp) {
		super();
		this.title = title;
		this.url = url;
		this.timestamp = timestamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}