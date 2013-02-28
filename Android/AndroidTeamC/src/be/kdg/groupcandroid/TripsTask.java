package be.kdg.groupcandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class TripsTask extends AsyncTask<String, Void, ArrayList<Item>>{

	@Override
	protected ArrayList<Item> doInBackground(String... params) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;
		ArrayList<Item> trips = new ArrayList<Item>();
		try {
			httpGet = new HttpGet(new URI("http://" + params[0] + ":" + params[1]
					+ "/service/"+ params[2]+ "trips?username=" + params[3] + "&password=" + params[4]));
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				JSONObject jsonObject = new JSONObject(builder.toString());
				if (jsonObject.getBoolean("valid")) {
					JSONArray array = jsonObject.getJSONArray("trips");
						for (int i = 0; i < array.length(); i++){
							trips.add(new Item(array.getJSONObject(i).getString("title"), R.drawable.img_antwerpen, array.getJSONObject(i).getInt("id")));
						}
					return trips;
				} else {
					return trips;
				}
			} else {
				return trips;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
			Log.d("error", message);
			return trips;
		}
	}

}
