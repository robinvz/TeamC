package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class PublicTripsFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.publictrips, container,
				false);
		ListView listView = (ListView) view.findViewById(R.id.list);

		final ArrayList<Item> tripsList = getTrips();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(view.getContext(), MenuContent.class);
				intent.putExtra("tripName", tripsList.get(arg2).title);
				intent.putExtra("tripBanner", tripsList.get(arg2).image);
				startActivity(intent);
			}
		});
		SimpleListAdapter sla = new SimpleListAdapter(view.getContext(),
				tripsList, R.layout.listitemrow);
		listView.setAdapter(sla);

		// Search options
		Button btnShowSearch = (Button) view.findViewById(R.id.btnShowSearch);
		btnShowSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelativeLayout searchLayout = (RelativeLayout) view
						.findViewById(R.id.searchLayout);
				searchLayout.setVisibility(View.VISIBLE);
				v.setVisibility(View.INVISIBLE);
				EditText tbxRequest = (EditText) view
						.findViewById(R.id.tbxSearch);
			}
		});

		EditText tbxRequest = (EditText) view.findViewById(R.id.tbxSearch);
		tbxRequest.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
		tbxRequest.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
				btnSearch.performClick();

				return false;
			}
		});

		Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Send search request to backend and wait for new list
				EditText tbxRequest = (EditText) view
						.findViewById(R.id.tbxSearch);
				/*
				 * InputMethodManager imm =
				 * (InputMethodManager)view.getBaseContext
				 * ().getSystemService(Context.INPUT_METHOD_SERVICE);
				 * imm.hideSoftInputFromWindow(tbxRequest.getWindowToken(), 0);
				 * 
				 * Toast.makeText(getApplicationContext(), "searching for " +
				 * tbxRequest.getText(), Toast.LENGTH_LONG).show();
				 */
			}
		});
		return view;

	}

	private ArrayList<Item> getTrips() {
	/*	// TODO Ophalen van trips uit backend
		TripsTask tt = new TripsTask();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		String type = "all";
		try {
			return tt.execute(new String[] { ip, port, type,"bob", "bob" }).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Item>();
		
		/**/
		ArrayList<Item> trips = new ArrayList<Item>();
		trips.add(new Item("Tocht door Antwerpen", R.drawable.img_antwerpen));
		trips.add(new Item("Boswandeling Lokeren", R.drawable.img_antwerpen));
		trips.add(new Item("Trip door Brussel", R.drawable.img_antwerpen));
		trips.add(new Item("De oude binnenstad", R.drawable.img_antwerpen));
		trips.add(new Item("Kunst in Antwerpen", R.drawable.img_antwerpen));
		trips.add(new Item("Uitstap naar zee", R.drawable.img_antwerpen));
		trips.add(new Item("Rock Werchter", R.drawable.img_antwerpen));
		return trips;
	}

}
