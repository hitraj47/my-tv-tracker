package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		// Make website URL clickable
		TextView lblWebsite = (TextView) findViewById(R.id.lblWebsite);
		lblWebsite.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
