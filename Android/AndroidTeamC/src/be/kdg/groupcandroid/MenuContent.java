package be.kdg.groupcandroid;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import net.simonvt.menudrawer.*;

import java.util.ArrayList;
import java.util.List;


public class MenuContent extends FragmentActivity {

    private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.ContentSample.activePosition";
    private static final String STATE_CONTENT_TEXT = "net.simonvt.menudrawer.samples.ContentSample.contentText";
   
    private MenuDrawer mMenuDrawer;

    private MenuAdapter mAdapter;
    private ListView mList;

    private int mActivePosition = -1;
    private String mContentText;
    private TextView mContentTextView;
    


    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        Intent myIntent= getIntent();
        String tripName = myIntent.getStringExtra("tripName");
        // TODO custom banner -> crashes
        //Integer tripBanner = Integer.parseInt(myIntent.getStringExtra("tripBanner"));
        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
            mContentText = inState.getString(STATE_CONTENT_TEXT);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mMenuDrawer.setContentView(R.layout.activity_contentsample);
        
        List<Object> items = new ArrayList<Object>();
        items.add(new Item(tripName, R.drawable.ic_launcher));
        items.add(new Category("Communicatie"));
        items.add(new Item("Chat",  R.drawable.chat_icon));
        items.add(new Item("Broadcast", R.drawable.broadcast));
        items.add(new Category("Navigeren"));
        items.add(new Item("Posities", R.drawable.positie));

        mList = new ListView(this);
        mAdapter = new MenuAdapter(items);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(mItemClickListener);
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mMenuDrawer.invalidate();
            }
        });

        mMenuDrawer.setMenuView(mList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
       //Set tripTitle, tripBanner,...
        TextView txtTripTitle = (TextView) findViewById(R.id.txtTripTitle);
        txtTripTitle.setText(tripName);
             
        
    }
    
    
    
    
  

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            // Create an instance of ExampleFragment
            TestFragment firstFragment = TestFragment.newInstance(position);
            
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
      
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (position != 2){
                transaction.replace(R.id.fragment1, firstFragment);

            }
            else{
                ChatListFragment listfr = new ChatListFragment();

                transaction.replace(R.id.fragment1, listfr);
	
            }
            transaction.addToBackStack(null);
            transaction.commit();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
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
                    v = getLayoutInflater().inflate(R.layout.menu_row_category, parent, false);
                }

                ((TextView) v).setText(((Category) item).mTitle);

            } else {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_item, parent, false);
                }

                TextView tv = (TextView) v;
                tv.setText(((Item) item).title);
                tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).image, 0, 0, 0);
            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
        }
    }
}
