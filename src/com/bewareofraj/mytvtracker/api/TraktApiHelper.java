package com.bewareofraj.mytvtracker.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TraktApiHelper {

	public static final String API_BASE_URL = "http://api.trakt.tv/";
	public static final String API_METHOD_SHOW = "show/";
	public static final String API_ARGUMENT_SHOW_SUMMARY = "summary";
	public static final String API_ARGUMENT_SHOW_SEASONS = "seasons";
	public static final String API_FORMAT = ".json/";
	public static final String API_POSTER_SIZE_MEDIUM = "-300";
	public static final String API_POSTER_SIZE_SMALL = "-138";
	public static final String API_METHOD_SEARCH = "search/";
	public static final String API_ARGUMENT_SEARCH_SHOWS = "shows";
	public static final String API_SEARCH_LIMIT = "10";

	private String mApiKey = "";

	public TraktApiHelper(String apiKey) {
		this.setApiKey(apiKey);
	}

	/**
	 * Get a TV show based on TVDB ID
	 * 
	 * @param id
	 *            The TVDB ID
	 * @return A Show object
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	public Show getShow(String id) throws InterruptedException,
			ExecutionException, JSONException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SHOW);
		query.append(API_ARGUMENT_SHOW_SUMMARY);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append(id + "/");

		JSONObject result = new JSONObject(new RetrieveTraktJSONTask().execute(
				query.toString()).get());

		return createShowFromJSONObject(result);
	}

	public int getNumberOfSeasons(String id) throws InterruptedException, ExecutionException, JSONException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SHOW);
		query.append(API_ARGUMENT_SHOW_SEASONS);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append(id);

		JSONArray result = new JSONArray(new RetrieveTraktJSONTask().execute(
				query.toString()).get());
		JSONObject latestSeason = result.getJSONObject(0);

		return latestSeason.getInt("season");
	}

	private Date getDateFromUnixTimestamp(int timestamp) {
		Date date = new Date((long) timestamp * 1000);
		return date;
	}

	public String getApiKey() {
		return mApiKey;
	}

	public void setApiKey(String mApiKey) {
		this.mApiKey = mApiKey + "/";
	}
	
	public ArrayList<Show> getSearchResults(String terms) throws JSONException, InterruptedException, ExecutionException {
		JSONArray results = searchShows(terms, API_SEARCH_LIMIT);
		ArrayList<Show> resultsAsShows = new ArrayList<Show>();
		if (results.length() > 0) {
			for (int i = 0; i < results.length(); i++) {
				JSONObject object = results.getJSONObject(i);
				Show show = getShow( Integer.toString( object.getInt("tvdb_id")));
				resultsAsShows.add(show);
			}
		}
		
		return resultsAsShows;
	}

	private JSONArray searchShows(String terms, String apiSearchLimit) throws JSONException, InterruptedException, ExecutionException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SEARCH);
		query.append(API_ARGUMENT_SEARCH_SHOWS);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append("?query=");
		query.append(terms);
		query.append("&limit=");
		query.append(API_SEARCH_LIMIT);

		return new JSONArray(new RetrieveTraktJSONTask().execute(
				query.toString()).get());
	}
	
	private Show createShowFromJSONObject(JSONObject obj) throws JSONException, InterruptedException, ExecutionException {
		Show show = new Show();
		show.setTitle(obj.getString("title"));
		show.setYear(obj.getInt("year"));
		show.setFirstAired(getDateFromUnixTimestamp(obj
				.getInt("first_aired_utc")));
		show.setCountry(obj.getString("country"));
		show.setOverview(obj.getString("overview"));
		show.setStatus(obj.getString("status"));
		show.setNetwork(obj.getString("network"));
		show.setAirDay(obj.getString("air_day"));	// EST
		show.setAirTime(obj.getString("air_time"));	// EST
		show.setTvdbId(Integer.toString(obj.getInt("tvdb_id")));
		show.setPosterUrl(obj.getString("poster"));
		show.setSeasons(getNumberOfSeasons(Integer.toString(obj.getInt("tvdb_id"))));
		
		return show;
	}

}
