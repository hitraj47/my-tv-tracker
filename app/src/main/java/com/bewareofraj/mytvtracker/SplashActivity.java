package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends Activity {

    private static final String TAG = "splash_activity";
    
    private Show mShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        final String requestTag = "splash_activity";
        String query = TraktApiHelper.getShowQuery("258823", getString(R.string.trakt_api_key));

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
            }
        };

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    mShow = TraktApiHelper.getShowObjectFromResult(jsonObject);
                    displayTitle(mShow.getTitle());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        VolleyController.getInstance().getRequestQueue().getCache().clear();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, query, null, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonObjReq, requestTag);


        /*
        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);*/

	}

    private void displayTitle(String title) {
        TextView v = (TextView) findViewById(R.id.lblLoading);
        v.setText(title);
    }

}
