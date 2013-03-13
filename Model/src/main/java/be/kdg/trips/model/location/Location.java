package be.kdg.trips.model.location;


import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_LOCATION")
public class Location implements Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @DecimalMin(value = "-90.00", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.00", message = "Latitude must be between -90 and 90")
    private double latitude;
    @NotNull
    @DecimalMin(value = "-180.00", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.00", message = "Longitude must be between -180 and 180")
    private double longitude;
    @NotNull
    @Size(max = 50, message = "Title has a maximum amount of 50 characters")
    private String title;
    @Size(max = 150, message = "Description has a maximum amount of 150 characters")
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    @NotNull
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    private Question question;
    @ManyToOne
    @JoinColumn(name = "tripId")
    @NotNull
    private Trip trip;
    @NotNull
    private int sequence;

    public Location(Trip trip, double latitude, double longitude, Address address, String title, String description, int sequence, Question question) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.address = address;
        question.setLocation(this);
        this.question = question;
        this.trip = trip;
        this.sequence = sequence;
    }

    public Location(Trip trip, double latitude, double longitude, Address address, String title, String description, int sequence) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.address = address;
        this.trip = trip;
        this.sequence = sequence;
    }

    public Location() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        question.setLocation(this);
        this.question = question;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Integer getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (Double.compare(location.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
