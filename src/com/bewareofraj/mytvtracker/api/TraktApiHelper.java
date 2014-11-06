package com.bewareofraj.mytvtracker.api;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

public class TraktApiHelper {
	
	public static final String API_BASE_URL = "http://api.trakt.tv/";
	public static final String API_METHOD_SHOW = "show/";
	public static final String API_ARGUMENT_SHOW_SUMMARY = "summary";
	public static final String API_FORMAT = ".json/";
	
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
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SHOW);
		query.append(API_ARGUMENT_SHOW_SUMMARY);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append(id + "/");
		try {
			JSONObject result = new RetrieveTraktJSONTask().execute(query.toString()).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public String getApiKey() {
		return mApiKey;
	}

	public void setApiKey(String mApiKey) {
		this.mApiKey = mApiKey + "/";
	}

}
