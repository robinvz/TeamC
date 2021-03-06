package be.kdg.trips.services.interfaces;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
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
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface TripsService
{
    //User Service
    public User createUser(User user) throws TripsException;
    public User findUser(String email) throws TripsException;
    public List<User> findUsersByKeyword(String keyword, User user) throws TripsException;
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String country, byte[] profilePicture) throws TripsException;
    public void deleteUser(User user) throws TripsException;
    public boolean checkLogin(String email, String password) throws TripsException;
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException;
    public void forgotPassword(String email) throws TripsException, MessagingException;
    public void setUsersCurrentPosition(User user, double latitude, double longitude) throws TripsException;

    //Trip Service
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException;
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate, Repeatable repeatable, Integer amount) throws TripsException;
    public Trip findTripById(int id, User user) throws TripsException;
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException;
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException;
    public List<Trip> findPrivateTrips(User user) throws TripsException;
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException;
    public void editTripDetails(Trip trip, String title, String description, boolean chatAllowed, boolean positionVisible, User organizer) throws TripsException;
    public void publishTrip(Trip trip, User organizer) throws TripsException;
    public void deleteTrip(Trip trip, User organizer) throws TripsException, MessagingException;

    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException;
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException;
    public void addRequisiteToTrip(String name, int amount, Trip trip, User organizer) throws TripsException;
    public void removeRequisiteFromTrip(String name, int amount, Trip trip, User organizer) throws TripsException;
    public void addImageToTrip(Trip trip, User organizer, byte[] image) throws TripsException;
    public void changeThemeOfTrip(Trip trip, String theme) throws TripsException;

    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException;
    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException;
    public Location findLocationById(int id) throws TripsException;
    public void editTripLocationDetails(User organizer, Trip trip, Location location, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException;
    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException;
    public void deleteLocation(Trip trip, User organizer, Location location) throws TripsException;

    public void addQuestionToLocation(User organizer, Location location, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException;
    public Map<Question, Fraction> getQuestionsWithAnswerPercentage(Trip trip, User organizer) throws TripsException;
    public void editTripQuestionDetails(User organizer, Location location, String questionTitle, List<String> possibleAnswers, Integer correctAnswerIndex, byte[] image) throws TripsException;
    public void removeQuestionFromLocation(User organizer, Location location) throws TripsException;
    public void removeImageFromQuestion(User organizer, Question question) throws TripsException;

    //Enrollment Service
    public Enrollment subscribe(Trip trip, User user) throws TripsException;
    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException;
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException;
    public void disenroll(Trip trip, User user) throws TripsException;

    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException;
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException;
    public void addCostToEnrollment(String name, double amount, Trip trip, User user) throws TripsException;
    public void removeCostFromEnrollment(String name, double amount, Trip trip, User user) throws TripsException;

    public Invitation invite(Trip trip, User organizer, User user) throws TripsException, MessagingException;
    public List<Invitation> findInvitationsByUser(User user) throws TripsException;
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException;
    public void declineInvitation(Trip trip, User user) throws TripsException;
    public void uninvite(Trip trip, User organizer, User user) throws TripsException;

    public void startTrip(Trip trip, User user) throws TripsException;
    public String stopTrip(Trip trip, User user) throws TripsException;

    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException;
    public boolean checkAnswerFromQuestion(Question question, int answerIndex, User user) throws TripsException;

    public boolean isUserEnrolled(User user, Trip trip);
    //MailContact service
    public void sendContactMail(String subject, String text, String sender) throws MessagingException;
}
