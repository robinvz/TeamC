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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TripActionsTask extends AsyncTask<String, Void, Boolean> {

	private ProgressDialog dialog;

	public TripActionsTask(Activity activity) {
		dialog = new ProgressDialog(activity);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String ip = params[0];
		String port = params[1];
		String action = params[2];
		String username = params[3];
		String password = params[4];
		String tripId = params[5];

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/" + action));
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
					return true;
				}
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Performing action");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
