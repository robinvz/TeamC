package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import be.kdg.groupcandroid.model.Contact;
import be.kdg.groupcandroid.tasks.ContactsTask;
import be.kdg.groupcandroid.tasks.CurrentLocationTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserMapActivity extends FragmentActivity implements
		LocationListener {

	private GoogleMap mMap;
	private int tripId;
	private List<Marker> locationMarkers;
	private Marker current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tripId = getIntent().getIntExtra("tripid", 0);
		if (isGoogleMapsInstalled()) {
			setContentView(R.layout.usermap);
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
				Button btn = (Button) findViewById(R.id.buttonRefresh);
				locationMarkers = new ArrayList<Marker>();
				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						updateUserLocations();
					}
				});
				mMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.usermap)).getMap();
				// Enabling MyLocation Layer of Google Map
				mMap.setMyLocationEnabled(true);

				// Getting LocationManager object from System Service
				// LOCATION_SERVICE
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				// Creating a criteria object to retrieve provider
				Criteria criteria = new Criteria();

				// Getting the name of the best provider
				String provider = locationManager.getBestProvider(criteria,
						true);

				// Getting Current Location
				android.location.Location location = locationManager
						.getLastKnownLocation(provider);

				if (location != null) {
					updateUserLocations();
					onLocationChanged(location);
				} else {
					Intent settingsIntent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(settingsIntent);
				}
				locationManager
						.requestLocationUpdates(provider, 20000, 0, this);
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

	public Marker addMarker(double latitude, double longitude, String title) {
		LatLng ll = new LatLng(latitude, longitude);

		Marker mark = mMap.addMarker(new MarkerOptions().position(ll).title(
				title));
		return mark;
	}

	public void updateUserLocations() {
		ContactsTask ct = new ContactsTask(this);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(this);
		String email = sm.getEmail();
		String pass = sm.getPassword();

		ct.execute(new String[] { ip, port, tripId + "", email, pass });
		List<Contact> contacts;
		try {
			contacts = ct.get();
			for (Marker marker : locationMarkers) {
				marker.remove();
			}
			locationMarkers.clear();
			for (Contact contact : contacts) {
				Marker mark = addMarker(contact.getLatitude(),
						contact.getLongitude(), contact.toString());
				locationMarkers.add(mark);
			}
		} catch (Exception e) {
			Toast.makeText(this, getResources().getString(R.string.noconnect),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		if (current != null) {
			current.remove();
		}
		current = addMarker(location.getLatitude(), location.getLongitude(),
				getResources().getString(R.string.currentlocation));
		CurrentLocationTask clt = new CurrentLocationTask();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(this);
		String email = sm.getEmail();
		String pass = sm.getPassword();
		clt.execute(new String[] { ip, port, location.getLatitude() + "",
				location.getLongitude() + "", email, pass });
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				location.getLatitude(), location.getLongitude()), 11));
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