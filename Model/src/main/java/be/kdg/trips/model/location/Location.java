package be.kdg.trips.model.location;


import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_LOCATION")
public class Location implements LocationInterface, Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    @NotNull
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questionId")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "tripId")
    @NotNull
    private Trip trip;

    public Location(Trip trip, double latitude, double longitude, Address address, String title, String description, Question question) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.address = address;
        this.question = question;
        this.trip = trip;
    }

    public Location(Trip trip, double latitude, double longitude, Address address, String title, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.address = address;
        this.trip = trip;
    }

    private Location() {
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }
}
