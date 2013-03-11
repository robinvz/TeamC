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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kdg.groupcandroid.R;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class LocationsTask extends AsyncTask<String, Void, ArrayList<Location>> {
	private ProgressDialog dialog;

	public LocationsTask(Activity activity) {
		dialog = new ProgressDialog(activity);
	}

	@Override
	protected ArrayList<Location> doInBackground(String... params) {

		String ip = params[0];
		String port = params[1];
		String tripId = params[2];
		String username = params[3];
		String password = params[4];

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		ArrayList<Location> locations = new ArrayList<Location>();
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/locations"));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("id", tripId));
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
					JSONArray array = jsonObject.getJSONArray("locations");
					for (int i = 0; i < array.length(); i++) {
						int id = array.getJSONObject(i).getInt("id");
						String title = array.getJSONObject(i)
								.getString("title");
						String description = array.getJSONObject(i).getString(
								"description");
						double latitude = array.getJSONObject(i).getDouble(
								"latitude");
						double longitude = array.getJSONObject(i).getDouble(
								"longitude");
						String question = array.getJSONObject(i).getString(
								"question");
						Location location = new Location(id, title,
								description, latitude, longitude);
						location.addQuestion(question);
						try{
							JSONArray answers = array.getJSONObject(i)
									.getJSONArray("possibleAnswers");
							for (int j = 0; j < answers.length(); j++) {
								location.addAnswer(answers.getString(j));
							}
						}catch (Exception e){
							//NO question so no answer
						}
						locations.add(location);
					}
					return locations;
				} else {
					return locations;
				}
			} else {
				return locations;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
			Log.d("error", message);
			return locations;
		}
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Getting Locations");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(ArrayList<Location> result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	@Override
	protected void onCancelled() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
