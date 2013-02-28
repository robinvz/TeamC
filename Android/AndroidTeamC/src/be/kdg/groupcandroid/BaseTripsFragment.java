package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BaseTripsFragment extends Fragment {
	
	private String type;
	

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View view = inflater.inflate(R.layout.tripsview, container,
					false);
			ListView listView = (ListView) view.findViewById(R.id.list);

			final ArrayList<Item> tripsList = getTrips();
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
					String pass =sm.getPassword();
					TripTask tt = new TripTask();
					try {
						Trip trip = tt.execute(new String[]{ip, port, tripsList.get(arg2).id +"", email, pass}).get();
						Intent intent = new Intent(view.getContext(), MenuContent.class);
						intent.putExtra("trip", trip);
						startActivity(intent);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			SimpleListAdapter sla = new SimpleListAdapter(view.getContext(),
					tripsList, R.layout.listitemrow);
			listView.setAdapter(sla);
			return view;

		}

		private ArrayList<Item> getTrips() {
			// TODO Ophalen van trips uit backend
			TripsTask tt = new TripsTask();
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			String ip = sp.getString("server_ip", "192.168.2.200");
			String port = sp.getString("server_port", "8080");
			SessionManager sm = new SessionManager(getActivity());
			try {
				return tt.execute(new String[] { ip, port, type, sm.getEmail(), sm.getPassword() }).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ArrayList<Item>();
			
			/*
			ArrayList<Item> trips = new ArrayList<Item>();
			trips.add(new Item("Tocht door Antwerpen", R.drawable.img_antwerpen));
			trips.add(new Item("Boswandeling Lokeren", R.drawable.img_antwerpen));
			trips.add(new Item("Trip door Brussel", R.drawable.img_antwerpen));
			trips.add(new Item("De oude binnenstad", R.drawable.img_antwerpen));
			trips.add(new Item("Kunst in Antwerpen", R.drawable.img_antwerpen));
			trips.add(new Item("Uitstap naar zee", R.drawable.img_antwerpen));
			trips.add(new Item("Rock Werchter", R.drawable.img_antwerpen));
			return trips;*/
		}

}
