package be.kdg.groupcandroid;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import be.kdg.groupcandroid.model.Item;
import be.kdg.groupcandroid.model.Trip;

public class TripListAdapter extends ArrayAdapter<Trip> {
	private int colorListItemA = 0xFFEBEBEB;
	private int colorListItemB = 0xFFF8F8F8;
	private ArrayList<Trip> tripList;

	public TripListAdapter(Context context, ArrayList<Trip> tripList, int resource) {
		super(context, resource, tripList) ;
		this.tripList = tripList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null){
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listitemrow, null);
		}
		Trip tripListItem = tripList.get(position);
		if (tripListItem != null){
			TextView title = (TextView) v.findViewById(R.id.txtTitle);
			ImageView icon = (ImageView) v.findViewById(R.id.imgIcon);
			if(icon != null){
				if (tripListItem.getPicture() != null){
				icon.setImageDrawable(tripListItem.getPicture());
				}else{
					icon.setImageResource(R.drawable.img_antwerpen);
				}
			}
			if (title != null){
				title.setText(tripListItem.getTitle());
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