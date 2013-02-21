package be.kdg.trips.model.enrollment;

import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;

import java.util.Date;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface EnrollmentInterface {
    public Trip getTrip();
    public User getUser();
    public Date getDate();
    public void setTrip(Trip trip);
    public void setUser(User user);
    public void setDate(Date date);
}

