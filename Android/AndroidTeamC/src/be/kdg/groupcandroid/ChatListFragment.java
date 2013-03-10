package be.kdg.groupcandroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParsePush;

import be.kdg.groupcandroid.model.Contact;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

		final ChatListAdapter cla = new ChatListAdapter(getActivity(),
				getChatMessages(), R.layout.chatmessage);
		setListAdapter(cla);
		final EditText editText = (EditText) view
				.findViewById(R.id.txtChatInput);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				DateFormat dateFormat = new SimpleDateFormat(
						"EEE d MMM HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				ParsePush push = new ParsePush();
				SessionManager sm = new SessionManager(getActivity());

				//push.setChannel("user" + contact.getEmail().hashCode());
				push.setChannel("user" + sm.getEmail().hashCode());

				JSONObject jso = new JSONObject();
				String email = sm.getEmail();
				try {
					jso.put("sender", email);
					jso.put("message", editText.getText().toString());
					jso.put("tripid", tripId);
					jso.put("action", "be.kdg.groupcandroid.CHAT");
					push.setData(jso);
					push.sendInBackground();
					cla.notifyDataSetChanged();
					cla.add(new ChatMessage(editText.getText().toString(),
							R.drawable.img_antwerpen, contact.toString(),
							dateFormat.format(cal.getTime())));
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

}
