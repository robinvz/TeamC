package be.kdg.groupcandroid;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import be.kdg.groupcandroid.tasks.LoginTask;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "wMntxG3ilX9JbzFjuxukt21MlRvajx5LDqY7OHtR", "6mJ5wpU6wLXCiushPW3m4jXug3mNQ9FMy9YtI9jN"); 
		PushService.setDefaultPushCallback(this, ChatActivity.class);
		session = new SessionManager(getApplicationContext());
		if (session.isLoggedIn()) {
			Intent intent = new Intent(LoginActivity.this, TripsOverview.class);
			startActivity(intent);
		} else {
			setContentView(R.layout.activity_main);
			createListeners();
		}
		
	}

	public void createListeners() {
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		final String ip = sp.getString("server_ip", "192.168.2.200");
		final String port = sp.getString("server_port", "8080");
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TextView tvUser = (TextView) findViewById(R.id.txtUsername);
				TextView tvPass = (TextView) findViewById(R.id.txtPassword);
				String username = tvUser.getText().toString();
				String pass = tvPass.getText().toString();
				if (username.isEmpty() || pass.isEmpty()) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							LoginActivity.this);

					alertDialogBuilder.setTitle("No email/password");
					alertDialogBuilder
							.setMessage(
									"Please fill in your email and password")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				} else {
					doLogin(ip, port, "service/login", username, pass);

				}
			}
		});
		Button btnFacebook = (Button) findViewById(R.id.btnFacebookLogin);
		btnFacebook.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Session.openActiveSession(LoginActivity.this, true,
						new Session.StatusCallback() {

							// callback when session changes state
							@Override
							public void call(Session session,
									SessionState state, Exception exception) {
								if (session.isOpened()) {
									Request.executeMeRequestAsync(session,
											new Request.GraphUserCallback() {

												// callback after Graph API
												// response with user object
												@Override
												public void onCompleted(
														GraphUser user,
														Response response) {
													if (user != null) {
														doLogin(ip,
																port,
																"facebooklogin",
																user.getProperty(
																		"email")
																		.toString() + "facebook",
																user.getId());
													}
													else{
														Toast.makeText(LoginActivity.this,
																"Could not connect to Facebook", Toast.LENGTH_SHORT)
																.show();
													}
												}
											});
								}

							}
						});
			}

		});
	}

	public void doLogin(String ip, String port, String url, String username,
			String pass) {
		try {
			Integer response = new LoginTask(this).execute(
					new String[] { ip, port, url, username, pass }).get(3,
					TimeUnit.SECONDS);
			if (response == 1) {
				session.createLoginSession(username, pass);
				PushService.subscribe(this, "user" +  username.hashCode(), LoginActivity.class);	//Subscribing to channel for chat
				Intent intent = new Intent(LoginActivity.this,
						TripsOverview.class);
				startActivity(intent);
			} else {
				switch (response) {
				case 0:
					Toast.makeText(LoginActivity.this,
							"Wrong Username/Password", Toast.LENGTH_SHORT)
							.show();
					break;

				case -1:
					Toast.makeText(LoginActivity.this,
							"Could not connect to the server",
							Toast.LENGTH_SHORT).show();
					break;

				case -2:
					Toast.makeText(LoginActivity.this,
							"Could not connect. Is your Wifi/Data on?",
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Toast.makeText(LoginActivity.this,
							"Something went wrong, try again.",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
		} catch (TimeoutException e) {
			Toast.makeText(LoginActivity.this, "Connection Timed Out.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.getItem(0).setVisible(false);
		return true;
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

	@Override
	protected void onResume() {
		super.onResume();
		session = new SessionManager(getApplicationContext());
		if (session.isLoggedIn()) {
			Intent intent = new Intent(LoginActivity.this, TripsOverview.class);
			startActivity(intent);
		} else {
			setContentView(R.layout.activity_main);
			createListeners();
		}
	}
	
	

}
