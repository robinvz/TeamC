package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import be.kdg.groupcandroid.model.Category;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.model.Utilities;
import be.kdg.groupcandroid.tasks.ImageTask;
import be.kdg.groupcandroid.tasks.TripActionsTask;
import be.kdg.groupcandroid.tasks.TripsTask;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				getPicture();
			}
		}).run();
		addListeners(view);
		setButtonVisibility(view);
		return view;

	}

	private void getPicture() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final String ip = sp.getString("server_ip", "192.168.2.200");
		final String port = sp.getString("server_port", "8080");
		final String url = "http://" + ip + ":" + port + "/tripPic/"
				+ trip.getId();
		ImageTask tt = new ImageTask();
		Drawable bm;
		try {
			bm = tt.execute(new String[] { url }).get();
		} catch (Exception e ){
			bm = null;
		}
		trip.setPicture(bm); 

	}

	private void addListeners(final View view) {
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
		ImageView img = (ImageView) view.findViewById(R.id.tripBanner);
		img.setImageDrawable(trip.getPicture());
		TextView txtDes = (TextView) view.findViewById(R.id.txtDescription);
		txtDes.setText(trip.getDescription());
		TextView txtPrivacyNr = (TextView) view.findViewById(R.id.txtPrivacyNr);
		txtPrivacyNr.setText(trip.getPrivacy());
		final Button btnSubscribe = (Button) view
				.findViewById(R.id.btnSubscribe);
		final Button btnStart = (Button) view.findViewById(R.id.btnStart);
		btnStart.setVisibility(View.INVISIBLE);
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
							trip.setEnrolled(true);
						} else {
							Toast.makeText(
									getActivity(),
									getResources().getString(
											R.string.couldnotenroll),
									Toast.LENGTH_LONG).show();
						}
					} else {
						if (tat.execute(
								new String[] { ip, port, "unsubscribe", email,
										pass, // Start
										// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							trip.setEnrolled(false);
						} else {
							Toast.makeText(getActivity(),
									getResources().getString(R.string.unroll),
									Toast.LENGTH_LONG).show();
						}
					}
					setButtonVisibility(view);

				} catch (Exception e) {
					Toast.makeText(getActivity(),
							"Could not connect to the server",
							Toast.LENGTH_LONG).show();
				}
			}
		});

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
							trip.setStarted(true);
						} else {
							Toast.makeText(
									getActivity(),
									getResources().getString(
											R.string.couldnotstart),
									Toast.LENGTH_LONG).show();
						}

					} else {
						if (tat.execute(
								new String[] { ip, port, "stop", email, pass, // Stop
																				// Trip
										trip.getId() })
								.get(3, TimeUnit.SECONDS)) {
							trip.setStarted(false);
						} else {
							Toast.makeText(
									getActivity(),
									getResources().getString(
											R.string.couldnotstop),
									Toast.LENGTH_LONG).show();
						}
					}
					setButtonVisibility(view);
				} catch (Exception e) {
					Toast.makeText(getActivity(),
							"Could not connect to the server",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void setButtonVisibility(View view) {
		Button btnSubscribe = (Button) view.findViewById(R.id.btnSubscribe);
		Button btnStart = (Button) view.findViewById(R.id.btnStart);
		if (trip.isEnrolled() && trip.isStarted()
				&& (trip.isActive() || trip.isTimeless())) {
			btnSubscribe.setVisibility(View.INVISIBLE);
			btnStart.setVisibility(View.VISIBLE);
			btnStart.setText(getResources().getString(R.string.stop));
		} else if (trip.isEnrolled() && !trip.isStarted()
				&& (trip.isActive() || trip.isTimeless())) {
			btnSubscribe.setVisibility(View.VISIBLE);
			btnStart.setVisibility(View.VISIBLE);
			btnStart.setText(getResources().getString(R.string.start));
			btnSubscribe
					.setText(getResources().getString(R.string.unsubscribe));
		} else if (trip.isEnrolled()) {
			btnSubscribe.setVisibility(View.VISIBLE);
			btnStart.setVisibility(View.INVISIBLE);
			btnSubscribe
					.setText(getResources().getString(R.string.unsubscribe));
		} else {
			btnStart.setVisibility(View.INVISIBLE);
			btnSubscribe.setVisibility(View.VISIBLE);
			btnSubscribe.setText(getResources().getString(R.string.subscribe));
		}
		addItemsToMenu();
	}

	private void addItemsToMenu() {

		TripActivity detail = (TripActivity) getActivity();
		if (detail.items.size() > 3) {
			while (detail.items.size() > 3) {
				detail.items.remove(3);
			}
		}
		if (trip.isStarted() && (trip.isActive() || trip.isTimeless())
				&& !trip.getPrivacy().contentEquals("public")) {
			detail.items.add(new Item(getResources().getString(
					R.string.userlocations), R.drawable.chat_icon, 2));
		}
		addCommunicationCategory();
		addOrganizerMenu();
		if (trip.isStarted() && (trip.isActive() || trip.isTimeless())
				&& !trip.getPrivacy().contentEquals("public")) {
			detail.items.add(new Item(getResources().getString(R.string.chat),
					R.drawable.chat_icon, 2));
		}
		detail.mAdapter.notifyDataSetChanged();
	}

	private void addOrganizerMenu() {
		TripActivity detail = (TripActivity) getActivity();
		if (trip.getOrganizer().contentEquals(sm.getEmail())) {
			detail.items.add(new Item(getResources().getString(
					R.string.broadcast), R.drawable.broadcast, 1));
		}
		detail.mAdapter.notifyDataSetChanged();
	}

	private void addCommunicationCategory() {
		TripActivity detail = (TripActivity) getActivity();
		if (!(detail.items.get(2) instanceof Category)) {
			detail.items.add(new Category(getResources().getString(
					R.string.communication)));
		}
	}

}
