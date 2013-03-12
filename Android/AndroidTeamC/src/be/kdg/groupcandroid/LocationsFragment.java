package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.kdg.groupcandroid.model.Location;
import be.kdg.groupcandroid.tasks.LocationsTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LocationsFragment extends ListFragment {
	LocationListAdapter lla ;

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
		lla = new LocationListAdapter(getActivity(), getLocations(), R.layout.listitemrow);
		setListAdapter(lla);
		final ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent inten = new Intent(getActivity(), Maptivity.class);
				inten.putExtra("location", ((Location) arg0.getItemAtPosition(position)));
				inten.putExtra("sequence", position);
				boolean previousAnswered = true;
				if (position != 0){
					previousAnswered = ((Location) arg0.getItemAtPosition(position - 1)).isAnswered();
				}
				inten.putExtra("previousanswered", previousAnswered);
				startActivityForResult(inten, 1);
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	if(requestCode == 1 && resultCode == Activity.RESULT_OK)
	    {
	        boolean answer_correct = data.getBooleanExtra("correct", false);
	        int position = data.getIntExtra("position", 0);
        	lla.getItem(position).setAnswered(true);
	        if (answer_correct){
	        	lla.getItem(position).setCorrect(true);
	        }
        	lla.notifyDataSetChanged();	        
	    }
	}
}
