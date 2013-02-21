package be.kdg.groupcandroid;

import java.util.ArrayList;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ChatListFragment extends ListFragment {



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.chat, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ChatListAdapter cla = new ChatListAdapter(getActivity(), getChatMessages(), R.layout.chatmessage);
		setListAdapter(cla);
	}
	
	private ArrayList<ChatMessage> getChatMessages() {
		// TODO Ophalen van chatmessages uit backend
		ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
		chatMessages.add(new ChatMessage("Hi", R.drawable.img_antwerpen, "Peter", "12:00"));
		chatMessages.add(new ChatMessage("Hello", R.drawable.img_antwerpen, "Jessica", "12:01"));
		chatMessages.add(new ChatMessage("Are you on a trip right now?", R.drawable.img_antwerpen, "Peter", "12:01"));
		chatMessages.add(new ChatMessage("No, I'm not. I planned to go today but since I have an emergency at work I had to reschedule my day. I will go on a trip next week.", R.drawable.img_antwerpen, "Jessica", "12:03"));
		chatMessages.add(new ChatMessage("Are you on a trip now?", R.drawable.img_antwerpen, "Jessica", "12:04"));
		chatMessages.add(new ChatMessage("Yes.", R.drawable.img_antwerpen, "Peter", "12:05"));
		chatMessages.add(new ChatMessage("Have fun!", R.drawable.img_antwerpen, "Jessica", "12:06"));
		return chatMessages;
	}

	
}
