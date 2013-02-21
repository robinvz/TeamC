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

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class TripsOverview extends Activity {
	/*
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.tripslist);
		ListView listView = (ListView) findViewById(R.id.list);
		
		final ArrayList<Item> tripsList = getTrips();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TripsOverview.this,
						MenuContent.class);
				intent.putExtra("tripName", tripsList.get(arg2).title); 
				intent.putExtra("tripBanner", tripsList.get(arg2).image);
				startActivity(intent);
			}
		});
		SimpleListAdapter sla = new SimpleListAdapter(this, tripsList,
				R.layout.listitemrow);
		listView.setAdapter(sla);

		//Search options
        Button btnShowSearch = (Button) findViewById(R.id.btnShowSearch);		
        btnShowSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelativeLayout searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
				searchLayout.setVisibility(View.VISIBLE);
				v.setVisibility(View.INVISIBLE);
				EditText tbxRequest = (EditText) findViewById(R.id.tbxSearch);
			}
		});
        
        EditText tbxRequest = (EditText) findViewById(R.id.tbxSearch);
        tbxRequest.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        tbxRequest.setOnEditorActionListener(new OnEditorActionListener() {		
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Button btnSearch = (Button) findViewById(R.id.btnSearch);
				btnSearch.performClick();
				
				return false;
			}
		});
        
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Send search request to backend and wait for new list
				EditText tbxRequest = (EditText) findViewById(R.id.tbxSearch);	
				InputMethodManager imm = 
			              (InputMethodManager)getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			           imm.hideSoftInputFromWindow(tbxRequest.getWindowToken(), 0);

				Toast.makeText(getApplicationContext(), "searching for " + tbxRequest.getText(), Toast.LENGTH_LONG).show();
			}
		});

	}
	
*/
	


	/*private ArrayList<Item> getTrips() {
		// TODO Ophalen van trips uit backend
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
				JSONArray jsonObject = new JSONArray(builder.toString());
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
		ArrayList<Item> trips = new ArrayList<Item>();
		trips.add(new Item("Tocht door Antwerpen", R.drawable.img_antwerpen));
		trips.add(new Item("Boswandeling Lokeren", R.drawable.img_antwerpen));
		trips.add(new Item("Trip door Brussel", R.drawable.img_antwerpen));
		trips.add(new Item("De oude binnenstad", R.drawable.img_antwerpen));
		trips.add(new Item("Kunst in Antwerpen", R.drawable.img_antwerpen));
		trips.add(new Item("Uitstap naar zee", R.drawable.img_antwerpen));
		trips.add(new Item("Rock Werchter", R.drawable.img_antwerpen));
		return trips;
	}*/

}
