package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;

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
    public Location getLocationById(int id) throws TripsException;

    public void saveTrip(Trip trip);
    public void updateTrip(Trip trip);
    public void saveOrUpdateLocation(Location location);
    public void updateQuestion(Question question);

    public void deleteTrip(int id);
    public void deleteLocation(int id);
    public void deleteQuestion(int id);

    public boolean isExistingTrip(int id) throws TripsException;
    public boolean isExistingLocation(int id)throws TripsException;
}

