package com.bewareofraj.mytvtracker.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bewareofraj.mytvtracker.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajesh on 3/5/2015.
 */
public class TraktStringRequest extends StringRequest {

    private Map<String, String> mHeaders;

    public TraktStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, listener, errorListener);
        mHeaders = new HashMap<>();
        mHeaders.put("trakt-api-version", "2");
        mHeaders.put("trakt-api-key", context.getString(R.string.trakt_client_id));
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        return mHeaders;
    }
}
