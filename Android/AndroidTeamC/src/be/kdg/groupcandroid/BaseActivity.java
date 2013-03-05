package be.kdg.groupcandroid;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {

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
		}
		else if(item.getItemId() == R.id.logout){
			SessionManager sm = new SessionManager(getBaseContext());
			sm.logoutUser();
			Session.openActiveSession(BaseActivity.this, true,
					new Session.StatusCallback() {

						// callback when session changes state
						@Override
						public void call(Session session,
								SessionState state, Exception exception) {
							if (session.isOpened()){
								session.closeAndClearTokenInformation();
							}

						}
					});
		}
		return super.onOptionsItemSelected(item);
	}
}
