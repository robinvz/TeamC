package be.kdg.groupcandroid;

import java.io.Serializable;

public class Trip implements Serializable{
	private String title;
	private String description;
	private boolean enrolled; 
	private int enrollments;
	private String Privacy;

	public Trip(String title, String description, int enrollments,
			String privacy) {
		super();
		this.title = title;
		this.description = description;
		this.enrollments = enrollments;
		Privacy = privacy;
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

	public int getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(int enrollments) {
		this.enrollments = enrollments;
	}

	public String getPrivacy() {
		return Privacy;
	}

	public void setPrivacy(String privacy) {
		Privacy = privacy;
	}

	
	
	public boolean isEnrolled() {
		return enrolled;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

	public Trip() {
		super();
	}
	
	

}
