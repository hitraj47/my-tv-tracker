package com.bewareofraj.mytvtracker.traktapi;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class TraktApiHelper {

    public static final String API_BASE_URL = "https://api-v2launch.trakt.tv/";
    public static final String API_METHOD_SHOW = "show/";
    public static final String API_FORMAT = ".json/";
    public static final String API_POSTER_SIZE_MEDIUM = "-300";
    public static final String API_POSTER_SIZE_SMALL = "-138";
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
    public static String getShowQuery(String id) {
        return API_BASE_URL + "/shows/" + id + "?extended=full,images";
    }

    public static Show getShowFromResult(String response) throws JSONException {
        Show show = new Show();
        JSONObject showObject = new JSONObject(response);
        show.setTitle(showObject.getString("title"));
        show.setYear(showObject.getInt("year"));
        show.setImdbId(showObject.getJSONObject("ids").getString("imdb"));
        show.setOverview(showObject.getString("overview"));
        show.setFirstAired(new DateTime(showObject.getString("first_aired")));

        Locale country = new Locale("", showObject.getString("country").toUpperCase());
        show.setCountry(country.getDisplayCountry());

        show.setRunTimeMinutes(showObject.getInt("runtime"));
        show.setNetwork(showObject.getString("network"));
        show.setStatus(showObject.getString("status"));
        show.setPosterUrl(showObject.getJSONObject("images").getJSONObject("poster").getString("full"));

        JSONObject airsObject = showObject.getJSONObject("airs");
        show.setAirDay(airsObject.getString("day"));
        show.setAirTime(airsObject.getString("time"));
        show.setAirTimeZone(airsObject.getString("timezone"));

        return show;
    }

    public static String getNumberOfSeasonsQuery(String id) {
        return API_BASE_URL + "shows/" + id + "/seasons";
    }
    
    public static int getNumberOfSeasonsFromResult(String resultString) throws JSONException {
        JSONArray resultsArray = new JSONArray(resultString);

        //  -1 because season 0 are specials
        return resultsArray.length()-1;
    }

    private static Date getDateFromUnixTimestamp(int timestamp) {
        return new Date((long) timestamp * 1000);
    }
    
    public static String getSearchQuery(String terms, String type, int limit) {
       return API_BASE_URL + "/search" + "?query=" + terms + "&type=" + type + "&limit=" + Integer.toString(limit);
    }

    public static ArrayList<Show> getShowSearchResults(String resultString) throws JSONException {
        ArrayList<Show> resultsAsShows = new ArrayList<>();
        JSONArray resultsArray = new JSONArray(resultString);

        if (resultsArray.length() > 0) {
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                JSONObject showObject = resultObject.getJSONObject("show");

                Show show = new Show();
                show.setTitle(showObject.getString("title"));
                show.setOverview(showObject.getString("overview"));

                if (showObject.isNull("year")) {
                    show.setYear(0);
                } else {
                    show.setYear(showObject.getInt("year"));
                }

                JSONObject posterObject = showObject.getJSONObject("images").getJSONObject("poster");
                show.setPosterUrl(posterObject.getString("thumb"));

                show.setImdbId(showObject.getJSONObject("ids").getString("imdb"));

                resultsAsShows.add(show);
            }
        }

        return resultsAsShows;
    }

    /**
     * Get the TV show calendar
     *
     * @return String
     */
    public static String getShowCalendar(int numDays) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String days = Integer.toString(numDays);
        return API_BASE_URL + "calendars/shows/" + date + "/" + days;
    }

    public static boolean isOnAir(String resultString, String imdbid) throws JSONException {
        ArrayList<String> ids = buildIdListFromCalendar(resultString);
        return ids.contains((String) imdbid);
    }

    private static ArrayList<String> buildIdListFromCalendar(String resultString) throws JSONException {
        ArrayList<String> ids = new ArrayList<>();
        JSONObject calendarObject = new JSONObject(resultString);
        Iterator<String> datesKeysIterator = calendarObject.keys();
        while (datesKeysIterator.hasNext()) {
            String dateKey = datesKeysIterator.next();
            JSONArray dateArray = calendarObject.getJSONArray(dateKey);
            for (int i = 0; i < dateArray.length(); i++) {
                JSONObject calendarItemObject = dateArray.getJSONObject(i);
                String imdbid = calendarItemObject.getJSONObject("show").getJSONObject("ids").getString("imdb");
                ids.add(imdbid);
            }
        }
        return ids;
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
