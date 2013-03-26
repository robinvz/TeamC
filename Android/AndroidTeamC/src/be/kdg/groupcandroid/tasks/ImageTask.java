package be.kdg.groupcandroid.tasks;

import be.kdg.groupcandroid.model.Utilities;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class ImageTask extends AsyncTask<String, Void, Drawable>{

	@Override
	protected Drawable doInBackground(String... params) {
		return Utilities.getBitmapFromURL(params[0]);
	}

}
