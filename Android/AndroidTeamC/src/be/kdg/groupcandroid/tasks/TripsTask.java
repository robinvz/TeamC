package be.kdg.groupcandroid.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kdg.groupcandroid.Item;
import be.kdg.groupcandroid.R;
import be.kdg.groupcandroid.R.drawable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class TripsTask extends AsyncTask<String, Void, ArrayList<Item>>{
	
	private ProgressDialog dialog;

	public TripsTask(Activity activity) {
		dialog = new ProgressDialog(activity);
	}
	


	@Override
	protected ArrayList<Item> doInBackground(String... params) {
		
		String ip = params[0];
		String port = params[1];
		String action = params[2];
		String username = params[3];
		String password = params[4];
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		ArrayList<Item> trips = new ArrayList<Item>();
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/"+ action + "trips"));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			if (params.length == 6){
				nameValuePairs.add(new BasicNameValuePair("keyword", params[5]));
			} 
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
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
	
	protected void onPreExecute() {
		this.dialog.setMessage("Getting Trips");
		this.dialog.show();
	}

	
	
	@Override
	protected void onPostExecute(ArrayList<Item> items) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
