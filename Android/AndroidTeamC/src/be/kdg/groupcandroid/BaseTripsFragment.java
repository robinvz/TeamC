package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.parse.PushService;

import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.tasks.ImageTask;
import be.kdg.groupcandroid.tasks.TripByIdTask;
import be.kdg.groupcandroid.tasks.TripsTask;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BaseTripsFragment extends Fragment {

	private String type;
	private Boolean searchVisible;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSearchVisible(Boolean isVisible) {
		this.searchVisible = isVisible;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater
				.inflate(R.layout.tripsview, container, false);
		final ListView listView = (ListView) view.findViewById(R.id.list);
		try {
			if (!searchVisible) {
				Button search = (Button) view.findViewById(R.id.btnShowSearch);
				search.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		final ArrayList<Trip> tripsList = getTrips();
		final TripListAdapter sla = new TripListAdapter(view.getContext(), tripsList,
				R.layout.listitemrow);
		listView.setAdapter(sla);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final String ip = sp.getString("server_ip", "192.168.2.200");
		final String port = sp.getString("server_port", "8080");
		for (final Trip t : tripsList){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ImageTask im = new ImageTask();
				final String url = "http://" + ip + ":" + port + "/tripPic/"
						+ t.getId();
				try {
					t.setPicture(im.execute(new String[]{url}).get());
					sla.notifyDataSetChanged();
				} catch (Exception e){
					Log.d("Picture", "Could not set a picture");
				}
			}
		}).run();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				SessionManager sm = new SessionManager(getActivity());
				String email = sm.getEmail();
				String pass = sm.getPassword();
				TripByIdTask tt = new TripByIdTask();
				try {
					tt.execute(new String[] { ip, port,
							tripsList.get(arg2).getId(), email, pass });
					Trip trip = tt.get();
					Intent intent = new Intent(view.getContext(),
							TripActivity.class);
					if (EnrolledTripsFragment.class.isInstance(this)) {
						trip.setEnrolled(true);
					}
					intent.putExtra("trip", trip);
					startActivity(intent);
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				} catch (Exception e) {
					Toast.makeText(getActivity(), "Connection timed out",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		final Button showSearch = (Button) view
				.findViewById(R.id.btnShowSearch);
		showSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RelativeLayout rl = (RelativeLayout) view
						.findViewById(R.id.searchLayout);
				if (rl.getVisibility() == View.GONE) {
					rl.setVisibility(View.VISIBLE);
					showSearch.setVisibility(View.INVISIBLE);
				}
			}
		});
		Button search = (Button) view.findViewById(R.id.btnSearch);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView keyword = (TextView) view.findViewById(R.id.tbxSearch);
				ArrayList<Trip> trips = getTripsByKeyword(keyword.getText()
						.toString());
				TripListAdapter sla = new TripListAdapter(view.getContext(),
						trips, R.layout.listitemrow);
				listView.setAdapter(sla);
				sla.notifyDataSetChanged();
				RelativeLayout rl = (RelativeLayout) view
						.findViewById(R.id.searchLayout);
				if (rl.getVisibility() == View.VISIBLE) {
					rl.setVisibility(View.GONE);
					showSearch.setVisibility(View.VISIBLE);
				}
			}
		});
		if (type.contentEquals("enrolled"));
		{
			subscribeToChannel(tripsList);
		}
		return view;
	}

	private void subscribeToChannel(ArrayList<Trip> tripsList) {
		for (Trip trip : tripsList) {
			PushService.subscribe(getActivity(), "trip" + trip.getId() + "-"
					+ trip.getTitle().hashCode(), TripsOverview.class);
		}
	}

	private ArrayList<Trip> getTrips() {
		// TODO Ophalen van trips uit backend
		TripsTask tt = new TripsTask(this.getActivity());
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final String ip = sp.getString("server_ip", "192.168.2.200");
		final String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(getActivity());
		try {
			return tt.execute(
					new String[] { ip, port, type, sm.getEmail(),
							sm.getPassword() }).get();
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Could not connect to server",
					Toast.LENGTH_LONG).show();
			tt.dialog.dismiss();
		}
		return new ArrayList<Trip>();
	}

	private ArrayList<Trip> getTripsByKeyword(String keyword) {
		// TODO Ophalen van trips uit backend
		TripsTask tt = new TripsTask(this.getActivity());
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(getActivity());

		try {
			tt.execute(new String[] { ip, port, "search", sm.getEmail(),
					sm.getPassword(), keyword });
			ArrayList<Trip> mySearchedTrips = tt.get(3, TimeUnit.SECONDS);
			return mySearchedTrips;
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Could not connect to server",
					Toast.LENGTH_LONG).show();
		}
		return new ArrayList<Trip>();
	}

}
