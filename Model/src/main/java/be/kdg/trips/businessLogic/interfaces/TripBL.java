package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.location.Location;
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

    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException;
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException;
    public List<Trip> findPrivateTrips(User user) throws TripsException;
    public Trip findTripById(int id, User user) throws TripsException;
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException;

    public void publishTrip(Trip trip, User user) throws TripsException;
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException;
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException;
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException;
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException;
    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException;

    public void deleteTrip(Trip trip, User user) throws TripsException, MessagingException;

    public boolean isExistingTrip(int id) throws TripsException;
    public boolean isOrganizer(Trip trip, User organizer) throws TripsException;
}
