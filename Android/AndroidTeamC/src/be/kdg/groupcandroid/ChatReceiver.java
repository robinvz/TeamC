package be.kdg.groupcandroid;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));
			String sender = json.getString("sender");
			String tripid = json.getString("tripid");
			String message = json.getString("message");

			Log.d("ChatReceiver", sender);
			Log.d("ChatReceiver", message);
			Log.d("ChatReceiver", tripid);
			notify(sender, message, tripid, context);
		} catch (JSONException e) {
		}
	}

	public void notify(String sender, String message, String tripid, Context context) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(sender)
				.setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra("email", sender);
		

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ChatActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		int mId = 0;
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mId, mBuilder.build());
	}
}