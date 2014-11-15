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
		
		// Make web site URL clickable
		TextView lblWebsite = (TextView) findViewById(R.id.lblWebsite);
		lblWebsite.setMovementMethod(LinkMovementMethod.getInstance());
		TextView lblTrakt = (TextView) findViewById(R.id.lblTraktCredits);
		lblTrakt.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
