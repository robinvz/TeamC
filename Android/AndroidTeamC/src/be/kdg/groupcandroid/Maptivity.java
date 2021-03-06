package be.kdg.groupcandroid;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import be.kdg.groupcandroid.model.Location;
import be.kdg.groupcandroid.tasks.MoveTask;
import be.kdg.groupcandroid.tasks.QuestionTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.qualcomm.QCARSamples.CloudRecognition.CloudReco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class Maptivity extends FragmentActivity implements LocationListener {

	private static final double RADIUS = 100;
	private GoogleMap mMap;
	private Location loc;
	private Marker current;
	private Marker endlocation;
	private int sequence;
	private Polyline line;
	private Location previousLocation;;
	boolean started = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		loc = (Location) getIntent().getSerializableExtra("location");
		previousLocation = (Location) getIntent().getSerializableExtra(
				"previousLocation");
		started = getIntent().getBooleanExtra("started", false);
		super.onCreate(savedInstanceState);
		sequence = getIntent().getIntExtra("sequence", 0);
		if (isGoogleMapsInstalled()) {
			setContentView(R.layout.mapke);
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getBaseContext());

			// Showing status
			if (status != ConnectionResult.SUCCESS) { // Google Play Services
														// are not available

				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						this, requestCode);
				dialog.show();
			} else {

				mMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
				// Enabling MyLocation Layer of Google Map
				mMap.setMyLocationEnabled(true);

				// Getting LocationManager object from System Service
				// LOCATION_SERVICE
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

				// Creating a criteria object to retrieve provider
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.NO_REQUIREMENT);

				// Getting the name of the best provider
				String provider = locationManager.getBestProvider(criteria,
						true);

				// Getting Current Location
				android.location.Location location = locationManager
						.getLastKnownLocation(provider);
				locationManager
						.requestLocationUpdates(provider, 20000, 0, this);

				if (location != null) {
					onLocationChanged(location);
				}
			}

		} else {
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.installGoogleMaps));
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.install),
					getGoogleMapsListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}

	}

	public OnClickListener getGoogleMapsListener() {
		return new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id=com.google.android.apps.maps"));
				startActivity(intent);

				// Finish the activity so they can't circumvent the check
				finish();
			}
		};
	}

	private boolean isGoogleMapsInstalled() {
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					"com.google.android.apps.maps", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public Marker addMarker(double latitude, double longitude, String title,
			String description) {
		LatLng ll = new LatLng(latitude, longitude);

		Marker mark = mMap.addMarker(new MarkerOptions().position(ll)
				.title(title).snippet(description));
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		return mark;
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
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(this);
		String email = sm.getEmail();
		String pass = sm.getPassword();
		QuestionTask qt = new QuestionTask(this);
		qt.execute(new String[] { ip, port, email, pass, item.getOrder() + "",
				loc.getId() + "" });
		try {
			switch (qt.get(3, TimeUnit.SECONDS)) {
			case 0: // incorrect
				finishQuestion(false);
				break;
			case 1: // correct
				finishQuestion(true);
				break;
			case -1:
				Toast.makeText(this,
						getResources().getString(R.string.alreadyanswered),
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(this,
						getResources().getString(R.string.noconnect),
						Toast.LENGTH_SHORT).show();
				break;
			}
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.noconnect),
					Toast.LENGTH_SHORT).show();
		}
		return true;

	}

	private void finishQuestion(boolean correct) {
		TextView tv = (TextView) findViewById(R.id.tvVraag);
		tv.setText(getResources().getString(R.string.answered));
		loc.setAnswered(true);
		unregisterForContextMenu(tv);
		Intent data = new Intent();
		data.putExtra("position", sequence);
		data.putExtra("correct", correct);
		// Set the data to pass back
		setResult(RESULT_OK, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.augmentedreality, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.artif) {
			Intent i = new Intent(this, CloudReco.class);
			startActivity(i);

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		addMarker(loc.getLatitude(), loc.getLongitude(), loc.getTitle(),
				loc.getDescription());
		addCircle(new LatLng(loc.getLatitude(), loc.getLongitude()), RADIUS); // RADIUS
																				// OF
																				// CIRCLE
		if (line != null) {
			line.remove();
		}
		if (current != null) {
			current.remove();
		}
		line = drawLine(new LatLng(loc.getLatitude(), loc.getLongitude()),
				new LatLng(location.getLatitude(), location.getLongitude()));
		current = addMarker(location.getLatitude(), location.getLongitude(),
				getResources().getString(R.string.currentlocation),
				getResources().getString(R.string.currentlocationsnippet));
		android.location.Location loco = new android.location.Location("");
		loco.setLatitude(loc.getLatitude());
		loco.setLongitude(loc.getLongitude());
		TextView tv = (TextView) findViewById(R.id.tvVraag);
		if (!started) {
			tv.setText(getResources().getString(R.string.notstarted));
		} else if (loc.getQuestion().contentEquals("null")) {
			tv.setText(getResources().getString(R.string.noquestion));
			if (location.distanceTo(loco) <= RADIUS && !loc.isVisited()) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(this);
				String ip = sp.getString("server_ip", "192.168.2.200");
				String port = sp.getString("server_port", "8080");
				SessionManager sm = new SessionManager(this);
				String email = sm.getEmail();
				String pass = sm.getPassword();
				Intent data = new Intent();
				data.putExtra("position", sequence);
				// Set the data to pass back
				MoveTask mv = new MoveTask(this);
				mv.execute(new String[] { ip, port, loc.getId() + "", email,
						pass });
				setResult(RESULT_OK, data);
			}
		} else if (previousLocation != null && !previousLocation.isAnswered()) {
			tv.setText(getResources().getString(R.string.answerpreviousfirst));
		} else if (location.distanceTo(loco) > RADIUS) {
			tv.setText(getResources().getString(R.string.notcloseenough));
			tv.setOnClickListener(null);
		} else if (!loc.isAnswered()
				&& !loc.getQuestion().contentEquals("null")
				&& !loc.getQuestion().isEmpty()) {
			tv.setText(loc.getQuestion());
			registerForContextMenu(tv);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openContextMenu(v);
				}
			});
		} else {
			tv.setText(getResources().getString(R.string.alreadyanswered));
		}
	}

	public Polyline drawLine(LatLng from, LatLng to) {
		PolylineOptions rectOptions = new PolylineOptions().add(from).add(to);
		return mMap.addPolyline(rectOptions);
	}

	private Circle addCircle(LatLng latLng, double radius) {
		CircleOptions circleOptions = new CircleOptions().center(latLng)
				.radius(radius).strokeColor(Color.BLUE); // In meters
		return mMap.addCircle(circleOptions);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}