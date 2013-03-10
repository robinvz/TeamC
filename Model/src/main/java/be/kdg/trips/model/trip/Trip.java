package be.kdg.trips.model.trip;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.user.User;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

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
    @Size(min = 4, max = 50, message = "Title has a maximum amount of 50 characters and a minimum of 4 characters")
    private String title;
    @NotNull
    @Size(min = 4, max = 150, message = "Description has a maximum amount of 50 characters and a minimum of 4 characters")
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_TRIP_LABEL", joinColumns = @JoinColumn(name = "tripId"))
    @Column(name = "label")
    private Set<String> labels;
    @NotNull
    private TripPrivacy privacy;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User organizer;
    private boolean published;
    @OneToMany(mappedBy = "trip")
    @Cascade(CascadeType.REMOVE)
    private List<Location> locations;
    @OneToMany(mappedBy = "trip")
    @Cascade(CascadeType.REMOVE)
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "trip")
    @Cascade(CascadeType.REMOVE)
    private Set<Invitation> invitations;
    @ElementCollection
    @CollectionTable(name = "T_TRIP_REQUISITE", joinColumns = @JoinColumn(name = "tripId"))
    @Column(name = "requisite")
    private Map<String, Integer> requisites;
    private static int counter=0;
    @Lob
    private byte[] image;
    @NotNull
    private String theme;

    public Trip(String title, String description, TripPrivacy privacy, User organizer) {
        this.id = getNextId();
        this.title = title;
        this.description = description;
        this.privacy = privacy;
        if(privacy == TripPrivacy.PRIVATE)
        {
            this.published = true;
            this.invitations = new HashSet<>();
        }
        else
        {
            this.published = false;
        }
        this.organizer = organizer;
        this.labels = new HashSet<>();
        this.enrollments = new HashSet<>();
        this.locations = new ArrayList<>();
        this.requisites = new HashMap<>();
        this.theme = "default";
    }

    public Trip(String title, String description, TripPrivacy privacy, User organizer, byte[] image) {
        this(title, description, privacy, organizer);
        this.image = image;
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

    public Map<String, Integer> getRequisites()
    {
        return requisites;
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
    public boolean isTimeBoundTrip()
    {
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
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return o1.getSequence() - o2.getSequence();
            }
        });
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

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    @Override
    public void addRequisite(String name, int amount)
    {
        if(amount == 0)
        {
            amount = 1;
        }
        if(this.requisites.containsKey(name))
        {
            this.requisites.put(name, this.requisites.get(name) + Math.abs(amount));
        }
        else
        {
            this.requisites.put(name, Math.abs(amount));
        }
    }

    @Override
    public void removeRequisite(String name, int amount)
    {
        if(this.requisites.containsKey(name))
        {
            int value = this.requisites.get(name);
            if(amount < value)
            {
                this.requisites.put(name, value - Math.abs(amount));
            }
            else
            {
                this.requisites.remove(name);
            }
        }
    }

    public Set<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }

    public void addInvitation(Invitation invitation)
    {
        this.invitations.add(invitation);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
