package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.bewareofraj.mytvtracker.api.Show;

public class SplashActivity extends Activity {

    private static final String TAG = "splash_activity";
    
    private Show mShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

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
        }, 1000);

	}

    private void displayTitle(String title) {
        TextView v = (TextView) findViewById(R.id.lblLoading);
        v.setText(title);
    }

}
