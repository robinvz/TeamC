package be.kdg.groupcandroid;

import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

public class TripsOverview extends FragmentActivity {

	private static final String[] CONTENT = new String[] { "All",
			"Subscribed", "Created" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripslist);

		FragmentPagerAdapter adapter = new MyAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new PublicTripsFragment();
			case 1:
				return new EnrolledTripsFragment();

			case 2:
				return new CreatedTripsFragment();

			default:
				return null;
			}
		}
	}
}
