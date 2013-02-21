package be.kdg.trips.model.location;


import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
    @Column(nullable = false)
    @DecimalMin("-90.00")
    @DecimalMax("90.00")
    private double latitude;
    @Column(nullable = false)
    @DecimalMin("-180.00")
    @DecimalMax("180.00")
    private double longitude;
    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String title;
    @Size(max = 150)
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questionId")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "tripId")
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
