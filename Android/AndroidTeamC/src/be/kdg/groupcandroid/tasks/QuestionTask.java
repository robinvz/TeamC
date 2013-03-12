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

public class QuestionTask extends AsyncTask<String, Void, Integer>{
	private ProgressDialog dialog;
	private final static int ALREADY_ANSWERED = -1;
	private final static int FAILED_LOGIN = -2;
	private final static int WRONG_ANSWER = 0;
	private final static int CORRECT_ANSWER = 1;
	
	public QuestionTask(Activity activity) {
		dialog = new ProgressDialog(activity);
	}

	@Override
	protected Integer doInBackground(String... params) {
		String ip = params[0];
		String port = params[1];
		String username = params[2];
		String password = params[3];
		String answerIndex = params[4];
		String locationId = params[5];

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/answerQuestion" ));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("answerIndex", answerIndex));
			nameValuePairs.add(new BasicNameValuePair("locationId", locationId));
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
					if (jsonObject.getBoolean("correct")){
						return CORRECT_ANSWER;
					}
					return WRONG_ANSWER;
				}				
				return ALREADY_ANSWERED;
			} else {
				return FAILED_LOGIN;
			}
		} catch (Exception e) {
			return FAILED_LOGIN;
		}
	}
	
	

	protected void onPreExecute() {
		this.dialog.setMessage("Sending Answer");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(final Integer success) {
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