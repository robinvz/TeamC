package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import be.kdg.groupcandroid.model.Category;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.tasks.TripActionsTask;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TripFragment extends Fragment {

	private Trip trip;
	private List<Object> menuItems;
	SessionManager sm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		trip = (Trip) getArguments().getSerializable("trip");
		View view = inflater.inflate(R.layout.trip, container, false);
		addListeners(view);
		addOrganizerMenu();
		return view;

	}

	private void addListeners(View view) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final String ip = sp.getString("server_ip", "192.168.2.200");
		final String port = sp.getString("server_port", "8080");
		sm = new SessionManager(getActivity());
		final String email = sm.getEmail();
		final String pass = sm.getPassword();
		TextView tv = (TextView) view.findViewById(R.id.txtTripTitle);
		tv.setText(trip.getTitle());
		TextView txtEnrolle = (TextView) view.findViewById(R.id.txtEnrolledNr);
		txtEnrolle.setText(trip.getEnrollments() + "");
		TextView txtDes = (TextView) view.findViewById(R.id.txtDescription);
		txtDes.setText(trip.getDescription());
		TextView txtPrivacyNr = (TextView) view.findViewById(R.id.txtPrivacyNr);
		txtPrivacyNr.setText(trip.getPrivacy());
		final Button btnSubscribe = (Button) view
				.findViewById(R.id.btnSubscribe);
		final Button btnStart = (Button) view.findViewById(R.id.btnStart);
		btnStart.setVisibility(View.INVISIBLE);
		if (trip.isEnrolled() && (trip.isActive() || trip.isTimeless())) {
			btnSubscribe
					.setText(getResources().getString(R.string.unsubscribe));
			btnStart.setVisibility(View.VISIBLE);
			addItemsToMenu();
		} else if (trip.isEnrolled()) {
			addItemsToMenu();
			btnSubscribe
					.setText(getResources().getString(R.string.unsubscribe));
		}
		if (trip.isStarted()) {
			btnSubscribe.setVisibility(View.INVISIBLE);
			btnStart.setText(getResources().getString(R.string.stop));
		}
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TripActionsTask tat = new TripActionsTask(getActivity());
				try {
					if (btnStart
							.getText()
							.toString()
							.contentEquals(
									getResources().getString(R.string.start))) {

						if (tat.execute(
								new String[] { ip, port, "start", email, pass, // Start
																				// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							btnStart.setText(R.string.stop);
						}

					} else {
						if (tat.execute(
								new String[] { ip, port, "stop", email, pass, // Stop
																				// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							btnStart.setText(R.string.start);
							btnSubscribe.setVisibility(View.INVISIBLE);
						}
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(),
							"Could not connect to the server",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		btnSubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TripActionsTask tat = new TripActionsTask(getActivity());
				try {

					if (btnSubscribe
							.getText()
							.toString()
							.contentEquals(
									getResources()
											.getString(R.string.subscribe))) {
						if (tat.execute(
								new String[] { ip, port, "enroll", email, pass, // Start
																				// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							btnSubscribe.setText(R.string.unsubscribe);
							if (trip.isActive()) {
								btnStart.setVisibility(View.VISIBLE);
							}
						
							addItemsToMenu();
						}
					} else {
						if (tat.execute(
								new String[] { ip, port, "unsubscribe", email,
										pass, // Start
										// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							btnSubscribe.setText(R.string.subscribe);
							btnStart.setVisibility(View.INVISIBLE);
							removeItemsFromMenu();
						}
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(),
							"Could not connect to the server",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void addItemsToMenu(){
		if (!trip.getPrivacy().contentEquals("public")){
			menuItems = new ArrayList<Object>();
			TripActivity detail = (TripActivity) getActivity();
			menuItems.add(new Item("Chat",
					R.drawable.chat_icon, 2));
		
			detail.items.addAll(menuItems);
			detail.mAdapter.notifyDataSetChanged();
		}
	}
	
	private void addOrganizerMenu(){
		TripActivity detail = (TripActivity) getActivity();
		addCommunicationCategory();
		if (trip.getOrganizer().contentEquals(sm.getEmail())){
			detail.items.add(new Item("Broadcast", R.drawable.broadcast, 1)); 
		}
	}
	
	private void addCommunicationCategory(){
		TripActivity detail = (TripActivity) getActivity();
		if (!(detail.items.get(2) instanceof Category)){
			detail.items.add(new Category("Communicatie"));
		}
	}
	
	private void removeItemsFromMenu(){
		if (!trip.getPrivacy().contentEquals("public")){
			TripActivity detail = (TripActivity) getActivity();
			detail.items.removeAll(menuItems);
			detail.mAdapter.notifyDataSetChanged();
		}
	}

}
