package com.bewareofraj.mytvtracker.util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bewareofraj.mytvtracker.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private ArrayList<String> mShowCalendarIds;
    private DateTime mShowCalendarLastUpdated;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public HashMap<String, String> getTraktHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("trakt-api-version", "2");
        headers.put("trakt-api-key", getString(R.string.trakt_client_id));
        return headers;
    }

    public ArrayList<String> getShowCalendarIds() {
        if (mShowCalendarIds == null) {
            mShowCalendarIds = new ArrayList<>();
        }
        return mShowCalendarIds;
    }

    public void setShowCalendarIds(ArrayList<String> ids) {
        mShowCalendarIds = ids;
    }

    public DateTime getShowCalendarLastUpdated() {
        return mShowCalendarLastUpdated;
    }

    public void setShowCalendarLastUpdated(DateTime lastUpdated) {
        mShowCalendarLastUpdated = lastUpdated;
    }

}
