package be.kdg.groupcandroid;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createListeners();
	}

	public void createListeners() {
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				String ip = sp.getString("server_ip", "192.168.2.200");
				String port = sp.getString("server_port", "8080");

				TextView tvUser = (TextView) findViewById(R.id.txtUsername);
				TextView tvPass = (TextView) findViewById(R.id.txtPassword);
				String username = tvUser.getText().toString();
				String pass = tvPass.getText().toString();
				try {
					Integer response = new LoginTask().execute(
							new String[] { ip, port, username, pass }).get();
					if (response == 1) {
						Intent intent = new Intent(MainActivity.this,
								TripsOverview.class);
						startActivity(intent);
					} else {
						switch (response) {
						case 0:
							Toast.makeText(MainActivity.this,
									"Wrong Username/Password",
									Toast.LENGTH_SHORT).show();
							break;

						case -1:
							Toast.makeText(MainActivity.this,
									"Could not connect to the server",
									Toast.LENGTH_SHORT).show();
							break;

						case -2:
							Toast.makeText(MainActivity.this,
									"Could not connect. Is your Wifi/Data on?",
									Toast.LENGTH_SHORT).show();
							break;

						default:
							Toast.makeText(MainActivity.this,
									"Something went wrong, try again.",
									Toast.LENGTH_SHORT).show();						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
				}

			}
		});
		Button btnFacebook = (Button) findViewById(R.id.btnFacebookLogin);
		btnFacebook.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						TripsOverview.class);
				startActivity(intent);
			}
		});
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
		}
		return super.onOptionsItemSelected(item);
	}

}
