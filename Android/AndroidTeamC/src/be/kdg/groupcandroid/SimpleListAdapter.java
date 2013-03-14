package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.groupcandroid.model.Item;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleListAdapter extends ArrayAdapter<Item> {
	private int colorListItemA = 0xFFEBEBEB;
	private int colorListItemB = 0xFFF8F8F8;
	private int colorOnPress = 0xFF00B9E6;
	private ArrayList<Item> tripList;

	public SimpleListAdapter(Context context, ArrayList<Item>  tripList, int resource) {
		super(context, resource, tripList);
		this.tripList = tripList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null){
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listitemrow, null);
		}
		Item tripListItem = tripList.get(position);
		if (tripListItem != null){
			TextView title = (TextView) v.findViewById(R.id.txtTitle);
			ImageView icon = (ImageView) v.findViewById(R.id.imgIcon);
			if(icon != null){
				icon.setImageDrawable(tripListItem.imageDrawable);
			}
			if (title != null){
				title.setText(tripListItem.title);
				if (position % 2 == 0){
					title.setBackgroundColor(colorListItemB);
				}else{
					title.setBackgroundColor(colorListItemA);
				}
			}
		}
		return v;
	}
}
