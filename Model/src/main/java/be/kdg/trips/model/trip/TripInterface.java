package be.kdg.trips.model.trip;

import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.user.User;

import java.util.List;
import java.util.Set;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripInterface {
    public int getId();
    public String getTitle();
    public void setTitle(String title);
    public String getDescription();
    public void setDescription(String description);
    public Set<String> getLabels();
    //public void setLabels(Set<String> labels);
    public TripPrivacy getPrivacy();
    //public void setPrivacy(TripPrivacy privacy);
    public void addLabel(String newLabel);
    public User getOrganizer();
    //public void setOrganizer(User organizer);
    public boolean isPublished();
    public void setPublished(boolean published);
    public boolean isActive();
    public boolean isTimeBoundTrip();
    public void addLocation(Location location);
    public void removeLocation(Location location);
    public List<Location> getLocations();
    public Set<Enrollment> getEnrollments();
    public void addEnrollment(Enrollment enrollment);
    public void removeEnrollment(Enrollment enrollment);
    public void addRequisite(String name, int amount);
    public void removeRequisite(String name, int amount);
}
