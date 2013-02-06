package be.kdg.groupcandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createListeners();
	}
	
	public void createListeners(){
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, MenuContent.class);
            	startActivity(intent);
            }
        });
		Button btnFacebook = (Button) findViewById(R.id.btnFacebookLogin);
		btnFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, MenuContent.class);
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

}
