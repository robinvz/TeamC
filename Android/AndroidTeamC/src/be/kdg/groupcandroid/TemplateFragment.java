package be.kdg.groupcandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TemplateFragment extends Fragment {

	public static TemplateFragment newInstance(int index, Trip myTrip) {
		TemplateFragment f = new TemplateFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putSerializable("trip", myTrip);
		f.setArguments(args);
		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	public Trip getTrip() {
		return (Trip) getArguments().getSerializable("trip");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		int positie = 0;
		try {
			positie = getShownIndex();
		} catch (Exception e) {
			// TODO: handle exception
		}
		switch (positie) { // categorieen hebben ook posities
		case 0:
			View view = inflater.inflate(R.layout.trip, container, false);
			return view;
		case 2:
			return inflater.inflate(R.layout.chat, container, false);
		case 3:
			return inflater.inflate(R.layout.broadcast, container, false);
		default:
			return inflater.inflate(R.layout.positie, container, false);
		}
	}

	public void setTexts(Trip trip) {
		TextView txtTripTitle = (TextView) getView().findViewById(
				R.id.txtTripTitle);
		txtTripTitle.setText(trip.getTitle());
		TextView txtDescription = (TextView) getView().findViewById(
				R.id.txtDescription);
		txtDescription.setText(trip.getDescription());
		TextView txtEnrolledNr = (TextView) getView().findViewById(
				R.id.txtEnrolledNr);
		txtEnrolledNr.setText(trip.getEnrollments() + "");
		TextView txtPrivacy = (TextView) getView().findViewById(
				R.id.txtPrivacyNr);
		txtPrivacy.setText(trip.getPrivacy());
		final Button btnSubscribe = (Button) getView().findViewById(
				R.id.btnSubscribe);
		final Button btnStart = (Button) getView().findViewById(R.id.btnStart);
		btnStart.setVisibility(View.INVISIBLE);
		if (trip.getPrivacy().toLowerCase().contains("public")) {
			btnSubscribe.setVisibility(View.INVISIBLE);
			btnStart.setVisibility(View.INVISIBLE);
		}
		if (trip.isEnrolled()) {
			btnSubscribe.setText(R.string.unsubscribe);
			btnStart.setVisibility(View.VISIBLE);
		}
	}

}