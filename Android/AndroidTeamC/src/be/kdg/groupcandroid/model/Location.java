package be.kdg.groupcandroid.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable {
	
	private int id;
	private String title;
	private String description;
	private double latitude;
	private double longitude;
	private String question;
	private boolean answered;
	private ArrayList<String> answers = new ArrayList<String>();
	
	public Location(int id, String title, String description, double latitude,
			double longitude) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(String answer){
		this.answers.add(answer);
	}

	public void addQuestion(String question) {
		// TODO Auto-generated method stub
		this.question = question;	
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	
	
	
	
	
	

}
