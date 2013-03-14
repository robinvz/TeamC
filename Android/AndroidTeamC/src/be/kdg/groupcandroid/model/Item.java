package be.kdg.groupcandroid.model;

import android.graphics.drawable.Drawable;

public class Item {
	
	public String title;
	public Integer image;
	public Drawable imageDrawable;
	public int id;
	
	public Item(String title, Integer image, int id) {
	
		this.title = title;
		this.image = image;
		this.id = id;
	}
	
	public Item(String title, Drawable image, int id) {
		
		this.title = title;
		this.imageDrawable = image;
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
}