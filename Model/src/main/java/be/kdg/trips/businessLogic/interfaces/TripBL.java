package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Repeatable;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.utility.Fraction;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripBL
{
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException;
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate, Repeatable repeatable, Integer amount) throws TripsException;

    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException;
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException;
    public List<Trip> findPrivateTrips(User user) throws TripsException;
    public Trip findTripById(int id, User user) throws TripsException;
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException;
    public Trip findTripByQuestion(Question question) throws TripsException;
    public Location findLocationById(int id) throws TripsException;

    public void editTripDetails(Trip trip, String title, String description, boolean chatAllowed, boolean positionVisible, User organizer) throws TripsException;
    public void editTripLocationDetails(User organizer, Trip trip, Location location, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException;
    public void editTripQuestionDetails(User organizer, Location location, Question question, String questionTitle, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException;
    public void publishTrip(Trip trip, User user) throws TripsException;
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException;
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException;
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException;
    public void deleteLocation(Trip trip, User user, Location location) throws TripsException;
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException;
    public void addRequisiteToTrip(String name, int amount, Trip trip, User organizer) throws TripsException;
    public void removeRequisiteFromTrip(String name, int amount, Trip trip, User organizer) throws TripsException;
    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException;
    public void addImageToTrip(Trip trip, User organizer, byte[] image) throws TripsException;
    public void changeThemeOfTrip(Trip trip, String theme) throws TripsException;

    public void deleteTrip(Trip trip, User user) throws TripsException, MessagingException;

    public boolean isExistingTrip(int id) throws TripsException;
    public boolean isExistingLocation(int id) throws TripsException;
    public boolean isOrganizer(Trip trip, User organizer) throws TripsException;
    public boolean isTripNotActive(Trip trip) throws TripsException;

    public void addQuestionToLocation(User organizer, Location location, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException;
    public void removeQuestionFromLocation(User organizer, Location location) throws TripsException;

    public Map<Question, Fraction> getQuestionsWithAnswerPercentage(Trip trip, User organizer) throws TripsException;
}
