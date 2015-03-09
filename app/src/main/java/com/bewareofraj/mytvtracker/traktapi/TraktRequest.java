package com.bewareofraj.mytvtracker.traktapi;

import com.android.volley.Response;
import com.bewareofraj.mytvtracker.util.CustomRequest;

import java.util.HashMap;

/**
 * Created by Rajesh on 3/9/2015.
 */
public class TraktRequest extends CustomRequest {

    public TraktRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public TraktRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, HashMap<String, String> parameters) {
        super(method, url, listener, errorListener, parameters);
    }
}
