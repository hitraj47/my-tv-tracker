package com.bewareofraj.mytvtracker.traktapi;

import com.bewareofraj.mytvtracker.util.MyApplication;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class TraktApiHelper {

    public static final String API_BASE_URL = "https://api-v2launch.trakt.tv/";

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

        show.setOnAir(MyApplication.getInstance().getShowCalendarIds().contains(show.getImdbId()));

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

    public static ArrayList<String> buildIdListFromCalendar(String resultString) throws JSONException {
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

    public static String getEpisodesForSeason(String id, int seasonNumber) {
        return API_BASE_URL + "shows/" + id + "/" + "seasons/" + Integer.toString(seasonNumber) + "?extended=full,images";
    }

    public static ArrayList<Episode> getEpisodesFromResult(String stringResults) throws JSONException {
        ArrayList<Episode> episodes = new ArrayList<>();
        JSONArray episodesArray = new JSONArray(stringResults);

        for (int i = 0; i < episodesArray.length(); i++) {
            JSONObject episodeObject = episodesArray.getJSONObject(i);

            Episode episode = new Episode(episodeObject.getInt("season"));
            episode.setEpisodeNumber(episodeObject.getInt("number"));
            episode.setTitle(episodeObject.getString("title"));
            episode.setOverview(episodeObject.getString("overview"));
            episode.setFirstAired(new DateTime(episodeObject.getString("first_aired")));
            episode.setImageUrl(episodeObject.getJSONObject("images").getJSONObject("screenshot").getString("full"));

            episodes.add(episode);
        }

        return episodes;
    }
}
