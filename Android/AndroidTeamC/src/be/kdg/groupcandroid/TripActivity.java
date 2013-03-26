package be.kdg.groupcandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import be.kdg.groupcandroid.model.Category;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.tasks.CurrentLocationTask;
import be.kdg.groupcandroid.tasks.TripActionsTask;
import net.simonvt.menudrawer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParsePush;

@SuppressLint("NewApi")
public class TripActivity extends FragmentActivity implements LocationListener {

	private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.ContentSample.activePosition";
	private static final String STATE_CONTENT_TEXT = "net.simonvt.menudrawer.samples.ContentSample.contentText";

	private MenuDrawer mMenuDrawer;

	public MenuAdapter mAdapter;
	private ListView mList;
	private int mActivePosition = -1;
	private String mContentText;
	private Trip trip;
	final List<Object> items = new ArrayList<Object>();

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		Intent myIntent = getIntent();
		getCurrentLocation();
		trip = (Trip) myIntent.getSerializableExtra("trip");
		if (inState != null) {
			mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
			mContentText = inState.getString(STATE_CONTENT_TEXT);
		}
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setContentView(R.layout.activity_contentsample);
		items.add(new Item(trip.getTitle(), R.drawable.ic_launcher, 0));
		items.add(new Category(getResources().getString(R.string.navigation)));
		items.add(new Item(getResources().getString(R.string.locations),
				R.drawable.positie, 3));
		mList = new ListView(this);
		mAdapter = new MenuAdapter(items);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(mItemClickListener);
		mList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mMenuDrawer.invalidate();
			}
		});

		mMenuDrawer.setMenuView(mList);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		TripFragment tp = new TripFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("trip", trip);
		tp.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment1, tp);
		transaction.commit();

	}

	private void getCurrentLocation() {
		// Getting LocationManager object from System Service
		// LOCATION_SERVICE
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_LOW);

		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location
		android.location.Location location = locationManager
				.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		} else {
			Intent settingsIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(settingsIntent);
		}
		locationManager.requestLocationUpdates(provider, 20000, 0, this);
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mMenuDrawer.setActiveView(view, position);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			Object clickedObject = parent.getAdapter().getItem(position);
			if (clickedObject instanceof Item) {
				Item clickItem = (Item) clickedObject;
				if (clickItem.title.contentEquals(getResources().getString(
						R.string.chat))) {
					Intent intent = new Intent(TripActivity.this,
							ChatActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("tripid", trip.getId());
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (clickItem.title.contentEquals(getResources()
						.getString(R.string.locations))) {
					LocationsFragment locFr = new LocationsFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("trip", trip);
					bundle.putBoolean("started", trip.isStarted());
					locFr.setArguments(bundle);
					transaction.replace(R.id.fragment1, locFr);
				} else if (clickItem.title.contentEquals(getResources()
						.getString(R.string.broadcast))) {
					makeBroadcast();
				} else if (clickItem.title.contentEquals(getResources()
						.getString(R.string.userlocations))) {
					Intent intent = new Intent(TripActivity.this,
							UserMapActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("tripid", trip.getId());
					intent.putExtras(bundle);
					startActivity(intent);
				} else { // Weird view selected
					TripFragment tp = new TripFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("trip", trip);
					tp.setArguments(bundle);
					transaction.replace(R.id.fragment1, tp);
				}
				transaction.commit();

			}
			mMenuDrawer.closeMenu();
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
		outState.putString(STATE_CONTENT_TEXT, mContentText);
	}

	protected void makeBroadcast() {
		AlertDialog.Builder alert = new AlertDialog.Builder(TripActivity.this);
		alert.setMessage(getResources().getString(R.string.typebroadcast));
		alert.setTitle("Broadcast");
		final EditText input = new EditText(TripActivity.this);
		alert.setView(input);
		alert.setPositiveButton(getResources().getString(R.string.send),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						ParsePush push = new ParsePush();
						push.setChannel("trip" + trip.getId() + "-"
								+ trip.getTitle().hashCode());
						push.setMessage(trip.getTitle() + ": " + value);
						push.sendInBackground();
						Toast.makeText(
								TripActivity.this,
								getResources()
										.getString(R.string.broadcastsent),
								Toast.LENGTH_LONG).show();
					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

	@Override
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			return;
		}

		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int drawerState = mMenuDrawer.getDrawerState();
		switch (item.getItemId()) {
		case android.R.id.home:
			if (drawerState == MenuDrawer.STATE_OPEN
					|| drawerState == MenuDrawer.STATE_OPENING) {
				mMenuDrawer.closeMenu();
			} else {
				mMenuDrawer.openMenu(true);
			}
			return true;
		case R.id.menu_settings:
			Intent preferences = new Intent(this, Preferences.class);
			startActivity(preferences);
			return true;
		case R.id.logout:
			SessionManager sm = new SessionManager(getBaseContext());
			sm.logoutUser();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class MenuAdapter extends BaseAdapter {

		private List<Object> mItems;

		MenuAdapter(List<Object> items) {
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position) instanceof Item ? 0 : 1;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItem(position) instanceof Item;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Object item = getItem(position);

			if (item instanceof Category) {
				if (v == null) {
					v = getLayoutInflater().inflate(R.layout.menu_row_category,
							parent, false);
				}

				((TextView) v).setText(((Category) item).mTitle);

			} else {
				if (v == null) {
					v = getLayoutInflater().inflate(R.layout.menu_row_item,
							parent, false);
				}

				TextView tv = (TextView) v;
				tv.setText(((Item) item).title);
				tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).image,
						0, 0, 0);
			}

			v.setTag(R.id.mdActiveViewPosition, position);

			if (position == mActivePosition) {
				mMenuDrawer.setActiveView(v, position);
			}

			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
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
