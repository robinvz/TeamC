package be.kdg.groupcandroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParsePush;

import be.kdg.groupcandroid.model.ChatMessage;
import be.kdg.groupcandroid.model.Contact;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatListFragment extends ListFragment {
	
	

	Contact contact;
	String tripId;
	static ChatListAdapter cla ;
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try{
			contact = (Contact) getArguments().getSerializable("contact");
		}catch (NullPointerException p){
			return inflater.inflate(R.layout.selectcontact, container, false);
		}
		View view = inflater.inflate(R.layout.chat, container, false);

		cla = new ChatListAdapter(getActivity(),
				getChatMessages(), R.layout.chatmessage);
		setListAdapter(cla);
		final EditText editText = (EditText) view
				.findViewById(R.id.txtChatInput);
		final DateFormat dateFormat = new SimpleDateFormat(
				"EEE d MMM HH:mm:ss");
		final Calendar cal = Calendar.getInstance();
		if (getArguments().containsKey("message")){
			ChatMessage cm = new ChatMessage(getArguments().getString("message"), R.drawable.img_antwerpen, contact.getEmail(), dateFormat.format(cal.getTime()));
			cla.add(cm);
			cla.notifyDataSetChanged();
		}
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				ParsePush push = new ParsePush();
				SessionManager sm = new SessionManager(getActivity());

				push.setChannel("user" + contact.getEmail().hashCode());
				//push.setChannel("user" + sm.getEmail().hashCode());

				JSONObject jso = new JSONObject();
				String email = sm.getEmail();
				try {
					jso.put("email", email);
					jso.put("message", editText.getText().toString());
					jso.put("tripid", tripId);
					jso.put("action", "be.kdg.groupcandroid.CHAT");
					push.setData(jso);
					push.sendInBackground();
					cla.add(new ChatMessage(editText.getText().toString(),
							R.drawable.img_antwerpen, email,
							dateFormat.format(cal.getTime())));
					cla.notifyDataSetChanged();
					editText.setText("");
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "Error sending text", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ChatReceiver cr = new ChatReceiver();
		getActivity().registerReceiver(cr, new IntentFilter("be.kdg.groupcandroid.CHAT"));
		super.onActivityCreated(savedInstanceState);
		try {
			tripId = getArguments().getString("tripid");
		} catch (NullPointerException np) {

		}

	}

	private ArrayList<ChatMessage> getChatMessages() {
		// TODO Ophalen van chatmessages uit backend
		ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
		return chatMessages;
	}
	
	
	static class ChatReceiver extends BroadcastReceiver {
		
		

		public ChatReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();
				String channel = intent.getExtras().getString("com.parse.Channel");
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));
				String sender = json.getString("email");
				String tripid = json.getString("tripid");
				String message = json.getString("message");

				Log.d("ChatReceiver", sender);
				Log.d("ChatReceiver", message);
				Log.d("ChatReceiver", tripid);
				notify(sender, message, tripid, context);
				DateFormat dateFormat = new SimpleDateFormat(
						"EEE d MMM HH:mm:ss");
				final Calendar cal = Calendar.getInstance();
				cla.add(new ChatMessage(message, R.drawable.img_antwerpen, sender, dateFormat.format(cal.getTime())));
				cla.notifyDataSetChanged();
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
			resultIntent.putExtra("message", message);
			resultIntent.putExtra("tripid", tripid);


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
			Notification nf = mBuilder.build();
			nf.defaults = Notification.DEFAULT_ALL;
			nf.flags = Notification.FLAG_AUTO_CANCEL;

			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, nf);
		}
	}

}
