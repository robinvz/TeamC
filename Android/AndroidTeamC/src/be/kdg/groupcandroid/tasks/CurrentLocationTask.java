package be.kdg.groupcandroid.tasks;

import java.io.BufferedReader;
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
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import be.kdg.groupcandroid.model.Contact;

public class CurrentLocationTask extends
		AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {

		String ip = params[0];
		String port = params[1];
		String latitude= params[2];
		String longitude = params[3];
		String username = params[4];
		String password = params[5];

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/updateLocation"));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
			nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
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
					
					return true;
				} else {
					return false ;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
			Log.d("error", message);
			return false;
		}
	}
}
