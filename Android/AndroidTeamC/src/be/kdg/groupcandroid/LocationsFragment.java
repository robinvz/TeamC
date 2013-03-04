package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.kdg.groupcandroid.tasks.LocationsTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LocationsFragment extends ListFragment {
	

	public LocationsFragment() {
		super();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.locationslist, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LocationListAdapter lla = new LocationListAdapter(getActivity(), getLocations(), R.layout.listitemrow);
		setListAdapter(lla);
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getActivity(), arg2 + "", Toast.LENGTH_LONG).show();
			}		
		});
	}
	
	private ArrayList<Location> getLocations() {
		LocationsTask lt = new LocationsTask(getActivity());
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(getActivity());
		String email = sm.getEmail();
		String pass = sm.getPassword();
		int tripId = getArguments().getInt("tripId");
		try {
			ArrayList<Location> locations = lt.execute(new String[]{ip, port, tripId + "", email, pass}).get(3, TimeUnit.SECONDS);
			return locations;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
