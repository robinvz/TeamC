package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripBL
{
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException;
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripsException;

    public List<Trip> findNonPrivateTripsByKeyword(String keyword);
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException;
    public List<Trip> findPrivateTrips(User user) throws TripsException;
    public Trip findTripById(int id) throws TripsException;

    public void publishTrip(Trip trip, User user) throws TripsException;
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException;
    public void addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException;
    public void addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException;

    public void deleteTrip(Trip trip, User user) throws TripsException, MessagingException;

    public boolean isExistingTrip(int id) throws TripsException;
    public boolean isOrganizer(Trip trip, User organizer) throws TripsException;
}
