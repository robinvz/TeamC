package be.kdg.groupcandroid.model;

import java.io.Serializable;

public class Trip implements Serializable {
	private String title;
	private String description;
	private boolean enrolled;
	private String organizer;
	private int enrollments;
	private String Privacy;
	private boolean active = false;
	private boolean started = false;

	private String id;
	private boolean timeless = false;

	public Trip(String id, String title, String description, int enrollments,
			String privacy) {
		super();
		this.id = id;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getId() {
		return this.id;
	}

	public void setStarted(boolean boolean1) {
		started = boolean1;

	}

	public boolean isStarted() {
		return started;
	}

	public void setTimeless(boolean boolean1) {
		timeless = boolean1;
	}

	public boolean isTimeless() {
		return timeless;
	}

	public void setOrganizer(String string) {
		this.organizer = string;
	}

	public String getOrganizer() {
		return organizer;
	}

}
