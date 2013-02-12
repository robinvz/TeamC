package be.kdg.trips.services;

import be.kdg.trips.exceptions.TripException;
import be.kdg.trips.exceptions.UserException;
import be.kdg.trips.model.Trip;
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

    public void createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripException;

    public void addLabelToTrip(Trip trip, User organizer, String label) throws UserException;

    public List findNonPrivateTripsByKeyword(String keyword) throws TripException;

    public List findAllTimelessNonPrivateTrips() throws TripException;

    public List findAllTimeBoundPublishedNonPrivateTrips() throws TripException;

    public void publishTrip(Trip trip);
}
