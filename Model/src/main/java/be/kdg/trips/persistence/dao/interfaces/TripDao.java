package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;

import java.util.Collection;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripDao {
    public List<Trip> getPublicTrips();
    public List<Trip> getProtectedTrips();
    public List<Trip> getProtectedTripsWithoutDetails();
    public List<Trip> getPublicTripsByKeyword(String keyword);
    public List<Trip> getProtectedTripsByKeyword(String keyword);
    public List<Trip> getProtectedTripsWithoutDetailsByKeyword(String keyword);
    public List<Trip> getTripsByOrganizer(User organizer);
    public Trip getTrip(int id) throws TripsException;
    public Trip getTripByQuestion(Question question) throws TripsException;
    public Location getLocationById(int id) throws TripsException;

    public void createTrip(Trip trip);
    public void updateTrip(Trip trip);
    public void saveOrUpdateLocation(Location location);


    public void deleteTrip(int id);
    public void deleteLocation(int id);

    public boolean isExistingTrip(int id) throws TripsException;
}

