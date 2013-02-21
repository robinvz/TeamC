package be.kdg.groupcandroid;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatListAdapter extends ArrayAdapter<ChatMessage>{
	private int colorListItemA = 0xFFEBEBEB;
	private int colorListItemB = 0xFFF8F8F8;
	private String firstName;
	private ArrayList<ChatMessage> chatMessages;

	public ChatListAdapter(Context context, ArrayList<ChatMessage>  chatMessages, int resource) {
		super(context, resource, chatMessages);
		this.chatMessages = chatMessages;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null){
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.chatmessage, null);
		}
		ChatMessage chatMessage = chatMessages.get(position);
		if (chatMessage != null){
			if(position == 0){
				firstName = chatMessage.name;
			}
			TextView content = (TextView) v.findViewById(R.id.txtContent);
			TextView name = (TextView) v.findViewById(R.id.txtName);
			TextView time = (TextView) v.findViewById(R.id.txtTime);			
			ImageView icon = (ImageView) v.findViewById(R.id.imgChatPic);
			RelativeLayout chatRow = (RelativeLayout) v.findViewById(R.id.chatRow);
			if(icon != null){
				icon.setBackgroundResource(chatMessage.image);
			}
			if (name != null){
				name.setText(chatMessage.name);
				if (firstName.equals(chatMessage.name)){
					chatRow.setBackgroundColor(colorListItemB);
				}else{
					chatRow.setBackgroundColor(colorListItemA);
				}
			}
			if (content != null){
				content.setText(chatMessage.content);
			}
			if (time != null){
				time.setText(chatMessage.time);
			}
			
		}
		return v;
	}

}
