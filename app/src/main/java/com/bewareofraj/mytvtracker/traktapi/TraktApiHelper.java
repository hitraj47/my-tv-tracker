package com.bewareofraj.mytvtracker.traktapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TraktApiHelper {

    public static final String API_BASE_URL = "https://api-v2launch.trakt.tv/";
    public static final String API_METHOD_SHOW = "show/";
    public static final String API_ARGUMENT_SHOW_SUMMARY = "summary";
    public static final String API_ARGUMENT_SHOW_SEASONS = "seasons";
    public static final String API_FORMAT = ".json/";
    public static final String API_POSTER_SIZE_MEDIUM = "-300";
    public static final String API_POSTER_SIZE_SMALL = "-138";
    public static final String API_METHOD_SEARCH = "search/";
    public static final String API_ARGUMENT_SHOWS = "shows";
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

    /**
     * Private constructor, this is a utility class
     * * *
     */
    private TraktApiHelper() {}
    
    /**
     * Get a TV show based on TVDB ID
     *
     * @param id The TVDB ID
     * @return A Show object
     */
    public static String getShowQuery(String id, String apiKey) {
        return API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SUMMARY + API_FORMAT + apiKey + "/" + id + "/";
    }
    
    public static Show getShowObjectFromResult(JSONObject result) throws JSONException {
        Show show = new Show();
        show.setTitle(result.getString(API_KEY_TITLE));
        show.setYear(result.getInt(API_KEY_YEAR));
        show.setFirstAired(getDateFromUnixTimestamp(result.getInt(API_KEY_FIRST_AIRED_UTC)));
        show.setFirstAiredTimeStamp(result.getInt(API_KEY_FIRST_AIRED));
        show.setCountry(result.getString(API_KEY_COUNTRY));
        show.setOverview(result.getString(API_KEY_OVERVIEW));
        show.setStatus(result.getString(API_KEY_STATUS));
        show.setNetwork(result.getString(API_KEY_NETWORK));
        show.setAirDay(result.getString(API_KEY_AIR_DAY));
        show.setAirTime(result.getString(API_KEY_AIR_TIME));
        show.setTvdbId(Integer.toString(result.getInt(API_KEY_TVDBID)));
        show.setPosterUrl(result.getString(API_KEY_POSTER_URL));
        //TODO: show.setSeasons(getNumberOfSeasons(show.getTvdbId(), "num_seasons"));

        return show;
    }

    public static String getNumberOfSeasonsQuery(String id, String apiKey, String requestTag) {
        return API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SEASONS + API_FORMAT + apiKey + "/" + id;
    }
    
    public static int getNumberOfSeasonsFromResult(JSONArray result) throws JSONException {
        return result.length()-1;
    }

    private static Date getDateFromUnixTimestamp(int timestamp) {
        return new Date((long) timestamp * 1000);
    }
    
    public static String getSearchQuery(String terms, String type, int limit) {
       return API_BASE_URL + API_METHOD_SEARCH + "?query=" + terms + "&type=" + type + "&limit=" + Integer.toString(limit);
    }

    public static ArrayList<Show> getShowSearchResults(String stringResults) throws JSONException {
        ArrayList<Show> resultsAsShows = new ArrayList<>();
        JSONArray resultsArray = new JSONArray(stringResults);

        if (resultsArray.length() > 0) {
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                JSONObject showObject = resultObject.getJSONObject("show");

                Show show = new Show();
                show.setTitle(showObject.getString("title"));

                if (showObject.isNull("year")) {
                    show.setYear(0);
                } else {
                    show.setYear(showObject.getInt("year"));
                }

                JSONObject posterObject = showObject.getJSONObject("images").getJSONObject("poster");
                show.setPosterUrl(posterObject.getString("thumb"));

                show.setTvdbId(showObject.getJSONObject("ids").getString("tvdb"));

                resultsAsShows.add(show);
            }
        }

        return resultsAsShows;
    }

    /**
     * Determine if a show is currently on air
     *
     * @return String
     */
    public static String getCurrentlyOnAirQuery(String apiKey) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String days = "7";
        return API_BASE_URL + API_METHOD_CALENDAR + API_ARGUMENT_SHOWS + API_FORMAT + apiKey + "/" + date + "/" + days;
    }
    
    public static boolean getCurrentlyOnAirResult(JSONArray result, String id) throws JSONException {
        boolean currentlyOnAir = false;
        for (int i = 0; i < result.length(); i++) {
            JSONObject calendarObject = result.getJSONObject(i);
            JSONArray episodes = calendarObject.getJSONArray(API_KEY_EPISODES);
            for (int j = 0; j < episodes.length(); j++) {
                JSONObject show = episodes.getJSONObject(j).getJSONObject(API_KEY_SHOW);
                String showId = show.getString(API_KEY_TVDBID);
                if (id.equals(showId)) {
                    currentlyOnAir = true;
                }
            }
        }
        return currentlyOnAir;
    }

    public static String getEpisodesQuery(String tvdbid, String season, String apiKey, String requestTag) {
        return API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SEASON + API_FORMAT + apiKey + "/" + tvdbid + "/" + season;
    }
    
    public static ArrayList<Episode> getEpisodesResult(JSONArray resultsArray, String season) throws JSONException {
        ArrayList<Episode> episodes = new ArrayList<>();
        for (int i = 0; i < resultsArray.length(); i++) {
            Episode episode = new Episode(Integer.parseInt(season));
            JSONObject object = resultsArray.getJSONObject(i);
            episode.setEpisodeNumber(object.getInt(API_KEY_EPISODE));
            episode.setFirstAired(object.getInt(API_KEY_FIRST_AIRED));
            episode.setImageUrl(object.getString(API_KEY_SCREEN));
            episode.setOverview(object.getString(API_KEY_OVERVIEW));
            episode.setTitle(object.getString(API_KEY_TITLE));
            episodes.add(i, episode);
        }
        return episodes;
    }
}
