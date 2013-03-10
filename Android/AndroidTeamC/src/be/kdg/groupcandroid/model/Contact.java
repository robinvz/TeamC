package be.kdg.groupcandroid.model;

import java.io.Serializable;

public class Contact implements Serializable {	
	String firstName;
	String lastName;	
	String email;
//	byte[] profilePicture;
//	String city;
	
	
	public Contact(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}


	@Override
	public String toString() {
		return firstName + " " + lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
		

}
