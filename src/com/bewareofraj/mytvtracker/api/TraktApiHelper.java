package com.bewareofraj.mytvtracker.api;

public class TraktApiHelper {
	
	public static final String API_BASE_URL = "http://api.trakt.tv/";
	public static final String API_METHOD_SHOW = "show/";
	
	private String mApiKey = "";
	
	public TraktApiHelper(String apiKey) {
		this.setApiKey(apiKey);
	}
	
	/**
	 * Get a TV show based on TVDB ID
	 * @param id The TVDB ID
	 * @return A Show object
	 */
	public Show getShow(String id) {
		return null;
	}

	public String getApiKey() {
		return mApiKey;
	}

	public void setApiKey(String mApiKey) {
		this.mApiKey = mApiKey;
	}

}
