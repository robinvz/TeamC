package be.kdg.trips.services;

import be.kdg.trips.exceptions.TripException;
import be.kdg.trips.model.TripPrivacy;
import be.kdg.trips.model.user.User;

import java.util.Date;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripService {
    public void createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer);
    public void createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate);
    public List findTripsByTitle(String stadswandeling) throws TripException;
    public List findTripsByKeyword(String keyword) throws TripException;
}
