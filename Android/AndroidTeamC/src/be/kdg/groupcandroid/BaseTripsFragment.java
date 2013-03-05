package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.kdg.groupcandroid.tasks.TripByIdTask;
import be.kdg.groupcandroid.tasks.TripsTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
		final ArrayList<Item> tripsList = getTrips();
		SimpleListAdapter sla = new SimpleListAdapter(view.getContext(),
				tripsList, R.layout.listitemrow);
		listView.setAdapter(sla);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				String ip = sp.getString("server_ip", "192.168.2.200");
				String port = sp.getString("server_port", "8080");
				SessionManager sm = new SessionManager(getActivity());
				String email = sm.getEmail();
				String pass = sm.getPassword();
				TripByIdTask tt = new TripByIdTask();
				try {
					Trip trip = tt.execute(
							new String[] { ip, port,
									tripsList.get(arg2).id + "", email, pass })
							.get(3, TimeUnit.SECONDS);
					Intent intent = new Intent(view.getContext(),
							TripDetail.class);
					if (EnrolledTripsFragment.class.isInstance(this)) {
						trip.setEnrolled(true);
					}
					intent.putExtra("trip", trip);
					startActivity(intent);
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				} catch (TimeoutException e) {
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
				TextView keyword = (TextView) view.findViewById(
						R.id.tbxSearch);
				ArrayList<Item> trips = getTripsByKeyword(keyword.getText()
						.toString());
				SimpleListAdapter sla = new SimpleListAdapter(
						view.getContext(), trips, R.layout.listitemrow);
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

		return view;

	}

	private ArrayList<Item> getTrips() {
		// TODO Ophalen van trips uit backend
		TripsTask tt = new TripsTask(this.getActivity());
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(getActivity());

		try {
			return tt.execute(
					new String[] { ip, port, type, sm.getEmail(),
							sm.getPassword() })
					.get(3000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Could not connect to server",
					Toast.LENGTH_LONG).show();
		}
		return new ArrayList<Item>();
	}

	private ArrayList<Item> getTripsByKeyword(String keyword) {
		// TODO Ophalen van trips uit backend
		TripsTask tt = new TripsTask(this.getActivity());
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(getActivity());

		try {
			return tt.execute(
					new String[] { ip, port, "search", sm.getEmail(),
							sm.getPassword(), keyword }).get(3000,
					TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Could not connect to server",
					Toast.LENGTH_LONG).show();
		}
		return new ArrayList<Item>();
	}

}
