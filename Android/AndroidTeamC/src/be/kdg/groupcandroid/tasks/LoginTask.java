package be.kdg.groupcandroid.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoginTask extends AsyncTask<String, Void, Integer> {

	private ProgressDialog dialog;

	public LoginTask(Activity activity) {
		dialog = new ProgressDialog(activity);
	}

	@Override
	protected Integer doInBackground(String... params) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI("http://" + params[0] + ":"
					+ params[1] + "/" + params[2]));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("password", params[4]));
			nameValuePairs.add(new BasicNameValuePair("username", params[3]));
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
					return 1;
				} else {
					return 0;
				}
			} else {
				return -1;
			}
		} catch (IOException e) {
			return -2;
		} catch (JSONException e) {
			return -3;
		} catch (Exception e) {
			e.printStackTrace();
			return -4;
		}
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Logging In");
		this.dialog.show();
	}

	
	
	@Override
	protected void onPostExecute(Integer result) {
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
