package be.kdg.trips.model.trip;

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
public abstract class Trip implements Serializable {
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
    private boolean chatAllowed;
    private boolean positionVisible;

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
        this.chatAllowed = true;
        this.positionVisible = true;
    }

    public Trip(String title, String description, TripPrivacy privacy, User organizer, byte[] image) {
        this(title, description, privacy, organizer);
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public Trip() {
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

    public Set<String> getLabels() {
        return labels;
    }

    public TripPrivacy getPrivacy() {
        return privacy;
    }

    public void addLabel(String newLabel){
        this.labels.add(newLabel);
    }

    public User getOrganizer() {
        return organizer;
    }

    public Map<String, Integer> getRequisites()
    {
        return requisites;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isActive(){
        return false;
    }

    public boolean isTimeBoundTrip()
    {
        return false;
    }

    public void addLocation(Location location)
    {
        locations.add(location);
    }

    public void removeLocation(Location location)
    {
        locations.remove(location);
    }

    public List<Location> getLocations() {
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return o1.getSequence() - o2.getSequence();
            }
        });
        return locations;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void addEnrollment(Enrollment enrollment)
    {
        enrollments.add(enrollment);
    }

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

    public void addRequisite(String name, int amount)
    {
        if(this.requisites.containsKey(name))
        {
            this.requisites.put(name, this.requisites.get(name) + Math.abs(amount));
        }
        else
        {
            this.requisites.put(name, Math.abs(amount));
        }
    }

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
    /*
    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }
    */
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

    public boolean isPositionVisible() {
        return positionVisible;
    }

    public void setPositionVisible(boolean positionVisible) {
        this.positionVisible = positionVisible;
    }

    public boolean isChatAllowed() {
        return chatAllowed;
    }

    public void setChatAllowed(boolean chatAllowed) {
        this.chatAllowed = chatAllowed;
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
