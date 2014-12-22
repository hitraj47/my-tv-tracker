package com.bewareofraj.mytvtracker.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bewareofraj.mytvtracker.util.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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
    
    public static final String DEFAULT_LOADING_MESSAGE = "Loading...";
    
    private String mLoadingMessage = DEFAULT_LOADING_MESSAGE;

    private static final String TAG = "trakt_api_helper";

    private String mApiKey = "";
    private Context mContext;
    private boolean mShowProgressDialog = false;

    public TraktApiHelper(Context context, String apiKey) {
        this.mContext = context;
        this.setApiKey(apiKey);
    }

    public void showProgressDialog(boolean showProgressDialog) {
        this.mShowProgressDialog = showProgressDialog;
    }

    /**
     * Get a TV show based on TVDB ID
     *
     * @param id The TVDB ID
     * @return A Show object
     */
    public Show getShow(String id, String requestTag) {

        String url = API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SUMMARY + API_FORMAT + mApiKey + id + "/";

        final ProgressDialog pDialog = new ProgressDialog(mContext);

        if (mShowProgressDialog) {
            pDialog.setMessage(DEFAULT_LOADING_MESSAGE);
            pDialog.show();
        }

        final Show show = new Show();
        
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (pDialog.isShowing()) {
                    // hide the progress dialog
                    pDialog.dismiss();
                }
            }
        };
        
        Response.Listener<JSONObject> responseListener;
        responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                try {
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
                    show.setSeasons(getNumberOfSeasons(show.getTvdbId(), "num_seasons"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        };

        JsonObjectRequest jsonObjReq;
        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(jsonObjReq, requestTag);

        return show;
    }

    public int getNumberOfSeasons(String id, String requestTag) throws JSONException {
        String query = API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SEASONS + API_FORMAT + mApiKey + id;

        final ProgressDialog pDialog = new ProgressDialog(mContext);

        if (mShowProgressDialog) {
            pDialog.setMessage(DEFAULT_LOADING_MESSAGE);
            pDialog.show();
        }
        
        // for getting the season number from the inner response listener
        class DataReceiver {
            public int season = -1;            
        }
        
        final DataReceiver r = new DataReceiver();

        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            
            @Override
            public void onResponse(JSONArray array) {
                try {
                    JSONObject object = array.getJSONObject(0);
                    r.season = object.getInt("season");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        };

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(query, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonArrayReq, requestTag);

        return r.season;
    }

    private Date getDateFromUnixTimestamp(int timestamp) {
        return new Date((long) timestamp * 1000);
    }

    public void setApiKey(String mApiKey) {
        this.mApiKey = mApiKey + "/";
    }
    
    public ArrayList<Show> searchShows(String terms, int apiSearchLimit, String requestTag) {
        final ArrayList<Show> resultsAsShows = new ArrayList<>();

        final ProgressDialog pDialog = new ProgressDialog(mContext);

        if (mShowProgressDialog) {
            pDialog.setMessage(DEFAULT_LOADING_MESSAGE);
            pDialog.show();
        }
        
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                }
            }
        };
        
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        };
        
        String query = API_BASE_URL + API_METHOD_SEARCH + API_ARGUMENT_SHOWS + API_FORMAT + mApiKey + "?query=" + terms + "&limit=" + apiSearchLimit;
        
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);
        
        return resultsAsShows;
    }

    /**
     * Determine if a show is currently on air
     *
     * @param id The TVDB ID
     * @return boolean
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JSONException
     */
    public boolean isCurrentlyOnAir(final String id, String requestTag) {
        String query = API_BASE_URL + API_METHOD_CALENDAR + API_ARGUMENT_SHOWS + API_FORMAT + mApiKey;

        final ProgressDialog pDialog = new ProgressDialog(mContext);

        if (mShowProgressDialog) {
            pDialog.setMessage(DEFAULT_LOADING_MESSAGE);
            pDialog.show();
        }
        
        class DataReceiver {
            public boolean isOnAir = false;            
        }
        final DataReceiver dr = new DataReceiver();
        
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray resultsArray) {
                try {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject calendarObject = resultsArray.getJSONObject(i);
                        JSONArray episodes = calendarObject.getJSONArray(API_KEY_EPISODES);
                        for (int j = 0; j < episodes.length(); j++) {
                            JSONObject show = episodes.getJSONObject(j).getJSONObject(API_KEY_SHOW);
                            String showId = show.getString(API_KEY_TVDBID);
                            if (id.equals(showId)) {
                                dr.isOnAir = true;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        };
        
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);

        return dr.isOnAir;
    }

    public ArrayList<Episode> getEpisodes(String tvdbid, final String season, String requestTag) {
        
        String query = API_BASE_URL + API_METHOD_SHOW + API_ARGUMENT_SHOW_SEASON + API_FORMAT + mApiKey + tvdbid + "/" + season;
        final ArrayList<Episode> episodes = new ArrayList<>();

        final ProgressDialog pDialog = new ProgressDialog(mContext);

        if (mShowProgressDialog) {
            pDialog.setMessage(DEFAULT_LOADING_MESSAGE);
            pDialog.show();
        }
        
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray resultsArray) {
                try {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        };
        
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);

        return episodes;
    }

    public String getLoadingMessage() {
        return mLoadingMessage;
    }

    public void setLoadingMessage(String mLoadingMessage) {
        this.mLoadingMessage = mLoadingMessage;
    }
}
