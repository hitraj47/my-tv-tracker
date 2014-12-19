package com.bewareofraj.mytvtracker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class RetrieveTraktJSONTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... queries) {
		String result = null;
		try {
			URL url = new URL(queries[0]);
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder jsonStringBuilder = new StringBuilder();
    		String line;
    		while ((line = br.readLine()) != null) {
    			jsonStringBuilder.append(line);
    		}

    		br.close();
    		is.close();
    		
    		result = jsonStringBuilder.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
