package be.kdg.groupcandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
	
	public static TestFragment newInstance(int index) {
		 TestFragment f = new TestFragment();

	        // Supply index input as an argument.
	        Bundle args = new Bundle();
	        args.putInt("index", index);
	        f.setArguments(args);

	        return f;
	    }
	

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		int positie = 0;
		try {
			positie = getShownIndex();
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(positie);
		switch (positie) {	//categorieen hebben ook posities
		case 0:
			return inflater.inflate(R.layout.trips, container, false);
		case 2:
			return inflater.inflate(R.layout.chat, container, false);
		case 3:
			return inflater.inflate(R.layout.broadcast, container, false);
		default:
			return inflater.inflate(R.layout.positie, container, false);
		}
	}
	
}