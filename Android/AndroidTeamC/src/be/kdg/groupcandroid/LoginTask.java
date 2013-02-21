package be.kdg.groupcandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class LoginTask extends AsyncTask<String, Void, Integer> {

	@Override
	protected Integer doInBackground(String... params) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(new URI("http://" + params[0] + ":" + params[1]
					+ "/service/login?username=" + params[2] + "&password=" + params[3]));
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
			return -4;
		}
	}

}
