package be.kdg.trips.model.trip;

import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.user.User;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_TRIP")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public abstract class Trip implements Serializable, TripInterface {
    @Id
    private int id;
    @NotNull
    @Size(max = 50, message = "Title has a maximum amount of 50 characters")
    private String title;
    @NotNull
    @Size(max = 150, message = "Description has a maximum amount of 150 characters")
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_TRIP_LABEL")
    @Column(nullable = true)
    private Set<String> labels;
    @NotNull
    private TripPrivacy privacy;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User organizer;
    private boolean published;
    @OneToMany(mappedBy = "trip")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private List<Location> locations;
    private static int counter=0;
    @OneToMany(mappedBy = "trip")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private Set<Enrollment> enrollments;

    public Trip(String title, String description, TripPrivacy privacy, User organizer) {
        this.id = getNextId();
        this.title = title;
        this.description = description;
        this.privacy = privacy;
        this.organizer = organizer;
        this.labels = new HashSet<>();
        this.published = false;
        this.enrollments = new HashSet<>();
        this.locations = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    public Trip() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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
    public Set<String> getLabels() {
        return labels;
    }

    @Override
    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    @Override
    public TripPrivacy getPrivacy() {
        return privacy;
    }

    @Override
    public void setPrivacy(TripPrivacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public void addLabel(String newLabel){
        this.labels.add(newLabel);
    }

    @Override
    public User getOrganizer() {
        return organizer;
    }

    @Override
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean isPublished() {
        return published;
    }

    @Override
    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public boolean isActive(){
        return false;
    }

    @Override
    public void addLocation(Location location)
    {
        locations.add(location);
    }

    @Override
    public void removeLocation(Location location)
    {
        locations.remove(location);
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    @Override
    public void addEnrollment(Enrollment enrollment)
    {
        enrollments.add(enrollment);
    }

    @Override
    public void removeEnrollment(Enrollment enrollment)
    {
        enrollments.remove(enrollment);
    }

    private synchronized int getNextId() {
        return counter++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (id != trip.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
