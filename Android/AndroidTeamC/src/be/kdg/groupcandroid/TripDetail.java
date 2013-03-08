package be.kdg.groupcandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import be.kdg.groupcandroid.model.Category;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.tasks.TripActionsTask;
import net.simonvt.menudrawer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressLint("NewApi")
public class TripDetail extends FragmentActivity {

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
		trip = (Trip) myIntent.getSerializableExtra("trip");
		if (inState != null) {
			mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
			mContentText = inState.getString(STATE_CONTENT_TEXT);
		}
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setContentView(R.layout.activity_contentsample);
		items.add(new Item(trip.getTitle(), R.drawable.ic_launcher, 0));
		items.add(new Category("Navigatie"));
		items.add(new Item("Locaties", R.drawable.positie, 3));
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
		TripFragment tp  = new TripFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("trip", trip);
		tp.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment1, tp);
		transaction.commit();

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
				if (clickItem.title.toLowerCase().contains("chat")) {
					/*
					ChatListFragment listfr = new ChatListFragment();
					transaction.replace(R.id.fragment1, listfr);*/
					Intent intent = new Intent(TripDetail.this, ChatActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("tripid", trip.getId());
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (clickItem.id == 0) { // TripTitle is selected so
					TripFragment tp  = new TripFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("trip", trip);
					tp.setArguments(bundle);
					transaction.replace(R.id.fragment1, tp);
				}
				else if (clickItem.title.toLowerCase().contains("loc")) { // TripTitle is selected so
					LocationsFragment locFr = new LocationsFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("tripId", Integer.parseInt(trip.getId()));
					locFr.setArguments(bundle);
					transaction.replace(R.id.fragment1, locFr);
				}else { // TripTitle is selected so title view
					TemplateFragment tempFragment = TemplateFragment.newInstance(
							position, trip);
					transaction.replace(R.id.fragment1, tempFragment);
				}
				transaction.commit();

			}
			// transaction.addToBackStack(null);

			mMenuDrawer.closeMenu();
		}
	};


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
		outState.putString(STATE_CONTENT_TEXT, mContentText);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_settings) {
			Intent intent = new Intent(this, Preferences.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.logout) {
			SessionManager sm = new SessionManager(getBaseContext());
			sm.logoutUser();
		}
		return super.onOptionsItemSelected(item);
	}
}
