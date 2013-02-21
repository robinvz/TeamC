package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.model.trip.Trip;

import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripDao {
    public void saveOrUpdateTrip(Trip trip);
    public List getPublicTrips();
    public List getProtectedTrips();
    public List getProtectedTripsWithoutDetails();
    public List getNonPrivateTripsByKeyword(String keyword);
    public Trip getTrip(int id);
    public void deleteTrip(Trip trip);
}

