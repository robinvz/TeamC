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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import be.kdg.groupcandroid.model.Contact;

public class ContactsTask extends AsyncTask<String, Void, ArrayList<Contact>>{
	private ProgressDialog dialog;
		public ContactsTask(Activity activity) {
			dialog = new ProgressDialog(activity);
		}


	@Override
	protected ArrayList<Contact> doInBackground(String... params) {

		String ip = params[0];
		String port = params[1];
		String tripId = params[2];
		String username = params[3];
		String password = params[4];

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost;
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		try {
			httpPost = new HttpPost(new URI("http://" + ip + ":" + port
					+ "/service/contacts"));
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
					JSONArray array = jsonObject.getJSONArray("contacts");
					for (int i = 0; i < array.length(); i++) {
						String email = array.getJSONObject(i)
								.getString("email");
						String firstName = array.getJSONObject(i).getString(
								"firstName");
						String lastName = array.getJSONObject(i).getString(
								"lastName");
						Contact c = new Contact(firstName, lastName, email);
						contacts.add(c);
					}
					return contacts;
				} else {
					return contacts;
				}
			} else {
				return contacts;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
			Log.d("error", message);
			return contacts;
		}
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Getting Contacts");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(ArrayList<Contact> result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}