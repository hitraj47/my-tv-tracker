package com.bewareofraj.mytvtracker.util;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

/**
 * Created by Rajesh on 3/9/2015.
 * This class extends volley's request class to include the ability to send customer headers
 */
public class CustomRequest extends StringRequest {

    private HashMap<String, String> mParams;

    public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, HashMap<String, String> parameters) {
        super(method, url, listener, errorListener);
        mParams = parameters;
    }

    @Override
    public HashMap<String, String> getParams() {
        return mParams;
    }
}
