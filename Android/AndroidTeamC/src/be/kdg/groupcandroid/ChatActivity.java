package be.kdg.groupcandroid;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import be.kdg.groupcandroid.model.Contact;
import be.kdg.groupcandroid.model.Trip;
import be.kdg.groupcandroid.tasks.ContactsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.android.gms.internal.ch;

public class ChatActivity extends FragmentActivity {

	private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.RightMenuSample.activePosition";
	private static final String STATE_CONTENT_TEXT = "net.simonvt.menudrawer.samples.RightMenuSample.contentText";

	private static final int MENU_OVERFLOW = 1;

	private MenuDrawer mMenuDrawer;
	String tripid;
	private MenuAdapter mAdapter;
	private ListView mList;

	private int mActivePosition = -1;
	private String mContentText;
	private TextView mContentTextView;
	List<Contact> contacts;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);

		if (inState != null) {
			mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
			mContentText = inState.getString(STATE_CONTENT_TEXT);
		}

		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT,
				Position.RIGHT);
		mMenuDrawer.setContentView(R.layout.chatfragment);
		Bundle bundle = getIntent().getExtras();
		tripid = bundle.getString("tripid");
		if (bundle.containsKey("message")){
			String message = (String) bundle.get("message");
			String email = (String) bundle.get("email");
			switchFragment(email, message);	
		}
		makeContactsList();
	}

	private void makeContactsList() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String ip = sp.getString("server_ip", "192.168.2.200");
		String port = sp.getString("server_port", "8080");
		SessionManager sm = new SessionManager(this);
		String email = sm.getEmail();
		String pass = sm.getPassword();
		ContactsTask ct = new ContactsTask(this);
		contacts = new ArrayList<Contact>();
		try {
			contacts = ct.execute(
					new String[] { ip, port, tripid, email, pass }).get(3,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			Toast.makeText(this, "Could not connect", Toast.LENGTH_SHORT)
					.show();
		}
		List<Object> items = new ArrayList<Object>();
		for (Contact c : contacts) {
			items.add(new Item(c.toString(), R.drawable.contacticon));
		}

		// A custom ListView is needed so the drawer can be notified when it's
		// scrolled. This is to update the position
		// of the arrow indicator.
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
	}

	private void switchFragment(String email, String message) {
		Contact tmp = new Contact("", "", email);
		ChatListFragment clf = new ChatListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tripid", tripid);
		bundle.putSerializable("contact", tmp);
		if (message != null) {
			bundle.putString("message", message);
		}
		clf.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.chatFragment, clf);
		transaction.commit();
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mActivePosition = position;
			mMenuDrawer.setActiveView(view, position);
			mMenuDrawer.closeMenu();
			ChatListFragment clf = new ChatListFragment();
			Bundle bundle = new Bundle();
			bundle.putString("tripid", tripid);
			bundle.putSerializable("contact", contacts.get(position));
			clf.setArguments(bundle);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.chatFragment, clf);
			transaction.commit();
		}
	};


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
		outState.putString(STATE_CONTENT_TEXT, mContentText);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem overflowItem = menu.add(0, MENU_OVERFLOW, 0, null);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			overflowItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		overflowItem.setIcon(R.drawable.contactsicon);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_OVERFLOW:
			mMenuDrawer.toggleMenu();
			makeContactsList();
			return true;
		}

		return super.onOptionsItemSelected(item);
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

	private static class Item {

		String mTitle;
		int mIconRes;

		Item(String title, int iconRes) {
			mTitle = title;
			mIconRes = iconRes;
		}
	}

	private static class Category {

		String mTitle;

		Category(String title) {
			mTitle = title;
		}
	}

	private class MenuAdapter extends BaseAdapter {

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
				tv.setText(((Item) item).mTitle);
				tv.setCompoundDrawablesWithIntrinsicBounds(
						((Item) item).mIconRes, 0, 0, 0);
			}

			v.setTag(R.id.mdActiveViewPosition, position);

			if (position == mActivePosition) {
				mMenuDrawer.setActiveView(v, position);
			}

			return v;
		}
	}
}
