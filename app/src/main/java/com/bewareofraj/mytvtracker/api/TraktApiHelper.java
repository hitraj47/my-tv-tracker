package com.bewareofraj.mytvtracker.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.bewareofraj.mytvtracker.SplashActivity;

public class TraktApiHelper {

	public static final String API_BASE_URL = "http://api.trakt.tv/";
	public static final String API_METHOD_SHOW = "show/";
	public static final String API_ARGUMENT_SHOW_SUMMARY = "summary";
	public static final String API_ARGUMENT_SHOW_SEASONS = "seasons";
	public static final String API_FORMAT = ".json/";
	public static final String API_POSTER_SIZE_MEDIUM = "-300";
	public static final String API_POSTER_SIZE_SMALL = "-138";
	public static final String API_METHOD_SEARCH = "search/";
	public static final String API_ARGUMENT_SHOWS = "shows";
	public static final String API_SEARCH_LIMIT = "10";
	public static final String API_METHOD_CALENDAR = "calendar/";
	public static final String API_ARGUMENT_SHOW_SEASON = "season";
	
	public static final String API_KEY_TITLE = "title";
	public static final String API_KEY_YEAR = "year";
	public static final String API_KEY_FIRST_AIRED_UTC = "first_aired_utc";
	public static final String API_KEY_COUNTRY = "country";
	public static final String API_KEY_OVERVIEW = "overview";
	public static final String API_KEY_STATUS = "status";
	public static final String API_KEY_NETWORK = "network";
	public static final String API_KEY_AIR_DAY = "air_day";
	public static final String API_KEY_AIR_TIME = "air_time";
	public static final String API_KEY_TVDBID = "tvdb_id";
	public static final String API_KEY_POSTER_URL = "poster";
	public static final String API_KEY_FIRST_AIRED = "first_aired";
	public static final String API_KEY_IMAGES_OBJECT = "images";
	public static final String API_KEY_EPISODES = "episodes";
	public static final String API_KEY_SHOW = "show";
	public static final String API_KEY_EPISODE = "episode";
	public static final String API_KEY_SCREEN = "screen";

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
		
		Show show = new Show();
		show.setTitle(result.getString(API_KEY_TITLE));
		show.setYear(result.getInt(API_KEY_YEAR));
		show.setFirstAired(getDateFromUnixTimestamp(result
				.getInt(API_KEY_FIRST_AIRED_UTC)));
		show.setFirstAiredTimeStamp(result.getInt(API_KEY_FIRST_AIRED));
		show.setCountry(result.getString(API_KEY_COUNTRY));
		show.setOverview(result.getString(API_KEY_OVERVIEW));
		show.setStatus(result.getString(API_KEY_STATUS));
		show.setNetwork(result.getString(API_KEY_NETWORK));
		show.setAirDay(result.getString(API_KEY_AIR_DAY));
		show.setAirTime(result.getString(API_KEY_AIR_TIME));
		show.setTvdbId(Integer.toString(result.getInt(API_KEY_TVDBID)));
		show.setPosterUrl(result.getString(API_KEY_POSTER_URL));
		show.setSeasons(getNumberOfSeasons(Integer.toString(result.getInt(API_KEY_TVDBID))));

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
	
	public ArrayList<Show> getShowSearchResults(String json) throws JSONException, InterruptedException, ExecutionException {
		JSONArray results = new JSONArray(json);
		ArrayList<Show> resultsAsShows = new ArrayList<Show>();
		if (results.length() > 0) {
			for (int i = 0; i < results.length(); i++) {
				JSONObject object = results.getJSONObject(i);
				Show show = new Show();
				show.setTitle(object.getString(API_KEY_TITLE));
				show.setYear(object.getInt(API_KEY_YEAR));
				show.setFirstAired(getDateFromUnixTimestamp(object.getInt(API_KEY_FIRST_AIRED)));
				show.setFirstAiredTimeStamp(object.getInt(API_KEY_FIRST_AIRED));
				show.setCountry(object.getString(API_KEY_COUNTRY));
				show.setOverview(object.getString(API_KEY_OVERVIEW));
				show.setNetwork(object.getString(API_KEY_NETWORK));
				show.setAirDay(object.getString(API_KEY_AIR_DAY));
				show.setAirTime(object.getString(API_KEY_AIR_TIME));
				show.setTvdbId(object.getString(API_KEY_TVDBID));
				JSONObject imagesObject = object.getJSONObject(API_KEY_IMAGES_OBJECT);
				show.setPosterUrl(imagesObject.getString(API_KEY_POSTER_URL));
				resultsAsShows.add(show);
			}
		}
		
		return resultsAsShows;
	}

	public String searchShows(String terms, String apiSearchLimit) throws JSONException, InterruptedException, ExecutionException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SEARCH);
		query.append(API_ARGUMENT_SHOWS);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append("?query=");
		query.append(terms);
		query.append("&limit=");
		query.append(API_SEARCH_LIMIT);

		return query.toString();
	}

	/**
	 * Determine if a show is currently on air
	 * @param id
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws JSONException 
	 */
	public boolean isCurrentlyOnAir(Context context, String id) throws JSONException, InterruptedException, ExecutionException {
		JSONArray resultsArray;
		if (!SplashActivity.isLocalJSONUpdated(context)) {
			String json = new RetrieveTraktJSONTask().execute(getCalendarJSONQuery()).get();
			SplashActivity.updatePreferences(json);
		}
		resultsArray = new JSONArray(getCalendarJSONLocal());
		
		for (int i = 0; i < resultsArray.length(); i++) {
			JSONObject calendarObject = resultsArray.getJSONObject(i);
			JSONArray episodes = calendarObject.getJSONArray(API_KEY_EPISODES);
			for (int j = 0; j < episodes.length(); j++) {
				JSONObject show = episodes.getJSONObject(j).getJSONObject(API_KEY_SHOW);
				String showId = show.getString(API_KEY_TVDBID);
				if (id.equals(showId)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String getCalendarJSONQuery() throws InterruptedException, ExecutionException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_CALENDAR);
		query.append(API_ARGUMENT_SHOWS);
		query.append(API_FORMAT);
		query.append(mApiKey);
		
		return query.toString();
	}
	
	public String getCalendarJSONLocal() {
		SharedPreferences preferences = SplashActivity.getSharedPreferences();
		return preferences.getString(SplashActivity.PREFS_KEY_CALENDAR_JSON, "");
	}
	
	public Episode[] getEpisodes(String tvdbid, String season) throws InterruptedException, ExecutionException, JSONException {
		StringBuilder query = new StringBuilder();
		query.append(API_BASE_URL);
		query.append(API_METHOD_SHOW);
		query.append(API_ARGUMENT_SHOW_SEASON);
		query.append(API_FORMAT);
		query.append(mApiKey);
		query.append(tvdbid + "/");
		query.append(season);
		
		String json = new RetrieveTraktJSONTask().execute(query.toString()).get();
		JSONArray resultsArray = new JSONArray(json);
		
		Episode[] episodes = new Episode[resultsArray.length()];
		for (int i = 0; i < resultsArray.length(); i++) {
			Episode episode = new Episode(Integer.parseInt(season));
			JSONObject object = resultsArray.getJSONObject(i);
			episode.setEpisodeNumber(object.getInt(API_KEY_EPISODE));
			episode.setFirstAired(object.getInt(API_KEY_FIRST_AIRED));
			episode.setImageUrl(object.getString(API_KEY_SCREEN));
			episode.setOverview(object.getString(API_KEY_OVERVIEW));
			episode.setTitle(object.getString(API_KEY_TITLE));
			episodes[i] = episode;
		}
		return episodes;
	}
	
}
