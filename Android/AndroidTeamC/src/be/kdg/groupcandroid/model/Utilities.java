package be.kdg.groupcandroid.model;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class Utilities {

	public static Drawable getBitmapFromURL(String src) {
	    try {
	        InputStream is = (InputStream) new URL(src).getContent();
	        Drawable d = Drawable.createFromStream(is, "src");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}

}
