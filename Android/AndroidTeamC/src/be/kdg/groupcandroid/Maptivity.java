package be.kdg.groupcandroid;

import be.kdg.groupcandroid.model.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class Maptivity extends BaseActivity {

	private GoogleMap mMap;
	private Location loc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mapke);
		loc = (Location) getIntent().getSerializableExtra("location");
		addMarker();
		TextView tv = (TextView) findViewById(R.id.tvVraag);
		if (!loc.isAnswered() && ! loc.getQuestion().contentEquals("null") 
				&& !loc.getQuestion().isEmpty()) {
			tv.setText(loc.getQuestion());
			registerForContextMenu(tv);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openContextMenu(v);
				}
			});
		} else if (loc.getQuestion().contentEquals("null") ){
			tv.setText(getResources().getString(R.string.noquestion));
		} else {
			tv.setText(getResources().getString(R.string.alreadyanswered));
		}
	}

	@SuppressLint("NewApi")
	public void addMarker() {
		LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.addMarker(new MarkerOptions().position(ll).title(loc.getTitle())
				.snippet(loc.getDescription()));
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getResources().getString(R.string.possibleanswers));
		int order = 0;
		for (String s : loc.getAnswers()) {
			menu.add(0, v.getId(), order++, s);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Toast.makeText(this, loc.getAnswers().get(item.getOrder()),
				Toast.LENGTH_SHORT).show();
		TextView tv = (TextView) findViewById(R.id.tvVraag);
		// Sent answer to backend
		tv.setText(getResources().getString(R.string.answered));
		loc.setAnswered(true);
		unregisterForContextMenu(tv);
		Intent data = new Intent();
		// Set the data to pass back
		setResult(RESULT_OK, data);

		// Close the activity
		finish();
		return true;
	}

}