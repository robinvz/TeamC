package be.kdg.groupcandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.groupcandroid.model.Location;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationListAdapter extends ArrayAdapter<Location> {
	private int colorListItemA = 0xFFEBEBEB;
	private int colorListItemB = 0xFFF8F8F8;
	private int colorOnPress = 0xFF00B9E6;
	private ArrayList<Location> locationList;

	public LocationListAdapter(Context context, ArrayList<Location>  locationList, int resource) {
		super(context, resource, locationList);
		this.locationList = locationList;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null){
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.locationitem, null);
		}
		Location location = locationList.get(position);
		if (location != null){
			TextView title = (TextView) v.findViewById(R.id.txtLocationTitle);
			TextView seqnr = (TextView) v.findViewById(R.id.txtSequence);
			ImageView img = (ImageView) v.findViewById(R.id.imageView1);
			if (title != null){
				title.setText(location.getTitle());
				seqnr.setText(position + "");
				if (position % 2 == 0){
					title.setBackgroundColor(colorListItemB);
				}else{
					title.setBackgroundColor(colorListItemA);
				}
				if (location.isAnswered() && location.isCorrect() && location.getQuestion() != null){
					img.setImageResource(R.drawable.correct);
				} else if (location.isAnswered() && !location.isCorrect() && location.getQuestion() != null){
					img.setImageResource(R.drawable.wrong);
				} else if (location.getQuestion().contentEquals("null")){
					img.setVisibility(View.INVISIBLE);
				}
			}
		}
		return v;
	}
	
	
}