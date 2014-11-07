package com.bewareofraj.mytvtracker.api;

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

		Show show = new Show();
		JSONObject result = new JSONObject(new RetrieveTraktJSONTask().execute(
				query.toString()).get());

		show.setTitle(result.getString("title"));
		show.setYear(result.getInt("year"));
		show.setFirstAired(getDateFromUnixTimestamp(result
				.getInt("first_aired_utc")));
		show.setCountry(result.getString("country"));
		show.setOverview(result.getString("overview"));
		show.setStatus(result.getString("status"));
		show.setNetwork(result.getString("network"));
		show.setAirDay(result.getString("air_day"));	// EST
		show.setAirTime(result.getString("air_time"));	// EST
		show.setTvdbId(Integer.toString(result.getInt("tvdb_id")));
		show.setPosterUrl(result.getString("poster"));
		show.setSeasons(getNumberOfSeasons(id));

		return show;
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

}
