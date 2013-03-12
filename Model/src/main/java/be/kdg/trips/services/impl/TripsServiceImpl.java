package be.kdg.trips.services.impl;


import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Repeatable;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import be.kdg.trips.utility.Fraction;
import be.kdg.trips.utility.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service(value = "tripsService")
public class TripsServiceImpl implements TripsService
{
    @Autowired
    private UserBL userBL;
    @Autowired
    private TripBL tripBL;
    @Autowired
    private EnrollmentBL enrollmentBL;

    //User Service
    @Override
    public User createUser(User user) throws TripsException {
        return userBL.createUser(user);
    }

    @Override
    public User findUser(String email) throws TripsException {
        return userBL.findUser(email);
    }

    @Override
    public List<User> findUsersByKeyword(String keyword, User user) throws TripsException {
        return userBL.findUsersByKeyword(keyword, user);
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String country, byte[] profilePicture) throws TripsException {
        userBL.updateUser(user, firstName, lastName, street, houseNr, city, postalCode, country, profilePicture);
    }

    @Override
    public void deleteUser(User user) throws TripsException {
        userBL.deleteUser(user);
    }

    @Override
    public boolean checkLogin(String email, String password) throws TripsException {
        return userBL.checkLogin(email, password);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException {
        userBL.changePassword(user, oldPassword, newPassword);
    }

    @Override
    public void setUsersCurrentPosition(User user, double latitude, double longitude) throws TripsException {
        userBL.setUserPosition(user, latitude, longitude);
    }

    //Trip Service
    @Override
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException {
        return tripBL.createTimelessTrip(title, description, privacy, organizer);
    }

    @Override
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate, Repeatable repeatable, Integer amount) throws TripsException {
        return tripBL.createTimeBoundTrip(title, description, privacy, organizer, startDate, endDate, repeatable, amount);
    }

    @Override
    public Trip findTripById(int id, User user) throws TripsException {
        return tripBL.findTripById(id, user);
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException {
        return tripBL.findAllNonPrivateTrips(user);
    }

    @Override
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException {
        return tripBL.findNonPrivateTripsByKeyword(keyword, user);
    }

    @Override
    public List<Trip> findPrivateTrips(User user) throws TripsException {
        return tripBL.findPrivateTrips(user);
    }

    @Override
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException {
        return tripBL.findTripsByOrganizer(organizer);
    }

    @Override
    public void editTripDetails(Trip trip, String title, String description, boolean chatAllowed, boolean positionVisible, User organizer) throws TripsException {
        tripBL.editTripDetails(trip, title, description, chatAllowed, positionVisible, organizer);
    }

    @Override
    public void publishTrip(Trip trip, User organizer) throws TripsException {
        tripBL.publishTrip(trip, organizer);
    }

    @Override
    public void deleteTrip(Trip trip, User organizer) throws TripsException, MessagingException {
        tripBL.deleteTrip(trip, organizer);
    }

    @Override
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException {
        tripBL.addDateToTimeBoundTrip(startDate, endDate, trip, organizer);
    }

    @Override
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException {
        tripBL.addLabelToTrip(trip, organizer, label);
    }

    @Override
    public void addRequisiteToTrip(String name, int amount, Trip trip, User organizer) throws TripsException {
        tripBL.addRequisiteToTrip(name, amount, trip, organizer);
    }

    @Override
    public void removeRequisiteFromTrip(String name, int amount, Trip trip, User organizer) throws TripsException {
        tripBL.removeRequisiteFromTrip(name, amount, trip, organizer);
    }

    @Override
    public void addImageToTrip(Trip trip, User organizer, byte[] image) throws TripsException {
        tripBL.addImageToTrip(trip, organizer, image);
    }

    @Override
    public void changeThemeOfTrip(Trip trip, String theme) throws TripsException {
        tripBL.changeThemeOfTrip(trip, theme);
    }

    @Override
    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException {
        return tripBL.addLocationToTrip(organizer, trip, latitude, longitude, street, houseNr, city, postalCode, country, title, description);
    }

    @Override
    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException {
        return tripBL.addLocationToTrip(organizer, trip, latitude, longitude, street, houseNr, city, postalCode, country, title, description, question, possibleAnswers, correctAnswerIndex, image);
    }

    @Override
    public Location findLocationById(int id) throws TripsException {
        return tripBL.findLocationById(id);
    }

    @Override
    public void editTripLocationDetails(User organizer, Trip trip, Location location, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException {
        tripBL.editTripLocationDetails(organizer, trip, location, street, houseNr, city, postalCode, country, title, description);
    }

    @Override
    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException {
        tripBL.switchLocationSequence(trip, user, location1, location2);
    }

    @Override
    public void deleteLocation(Trip trip, User organizer, Location location) throws TripsException {
        tripBL.deleteLocation(trip, organizer, location);
    }

    @Override
    public void addQuestionToLocation(User organizer, Location location, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException {
        tripBL.addQuestionToLocation(organizer, location, question, possibleAnswers, correctAnswerIndex, image);
    }

    @Override
    public Map<Question, Fraction> getQuestionsWithAnswerPercentage(Trip trip, User organizer) throws TripsException {
        return tripBL.getQuestionsWithAnswerPercentage(trip, organizer);
    }

    @Override
    public void editTripQuestionDetails(User organizer, Location location, Question question, String questionTitle, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException {
        tripBL.editTripQuestionDetails(organizer, location, question, questionTitle, possibleAnswers, correctAnswerIndex);
    }

    @Override
    public void removeQuestionFromLocation(User organizer, Location location) throws TripsException {
        tripBL.removeQuestionFromLocation(organizer, location);
    }

    //Enrollment Service
    @Override
    public Enrollment subscribe(Trip trip, User user) throws TripsException {
        return enrollmentBL.subscribe(trip, user);
    }

    @Override
    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException {
        return enrollmentBL.getEnrollmentsByUser(user);
    }

    @Override
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException {
        return enrollmentBL.getEnrollmentsByTrip(trip);
    }

    @Override
    public void disenroll(Trip trip, User user) throws TripsException {
        enrollmentBL.disenroll(trip, user);
    }

    @Override
    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException {
        enrollmentBL.addRequisiteToEnrollment(name, amount, trip, user, organizer);
    }

    @Override
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException {
        enrollmentBL.removeRequisiteFromEnrollment(name, amount, trip, user, organizer);
    }

    @Override
    public void addCostToEnrollment(String name, double amount, Trip trip, User user) throws TripsException {
        enrollmentBL.addCostToEnrollment(name, amount, trip, user);
    }

    @Override
    public void removeCostFromEnrollment(String name, double amount, Trip trip, User user) throws TripsException {
        enrollmentBL.removeCostFromEnrollment(name, amount, trip, user);
    }

    @Override
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException, MessagingException {
        return enrollmentBL.invite(trip, organizer, user);
    }

    @Override
    public List<Invitation> findInvitationsByUser(User user) throws TripsException {
        return enrollmentBL.getInvitationsByUser(user);
    }

    @Override
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException {
        return enrollmentBL.acceptInvitation(trip, user);
    }

    @Override
    public void declineInvitation(Trip trip, User user) throws TripsException {
        enrollmentBL.declineInvitation(trip, user);
    }

    @Override
    public void uninvite(Trip trip, User organizer, User user) throws TripsException {
        enrollmentBL.uninvite(trip, organizer, user);
    }

    @Override
    public void startTrip(Trip trip, User user) throws TripsException {
        enrollmentBL.startTrip(trip, user);
    }

    @Override
    public String stopTrip(Trip trip, User user) throws TripsException {
        return enrollmentBL.stopTrip(trip, user);
    }

    @Override
    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException {
        enrollmentBL.setLastLocationVisited(trip, user, location);
    }

    @Override
    public boolean checkAnswerFromQuestion(Question question, int answerIndex, User user) throws TripsException {
        return enrollmentBL.checkAnswerFromQuestion(question, answerIndex, user);
    }

    @Override
    public boolean isUserEnrolled(User user, Trip trip) {
        return enrollmentBL.isUserEnrolled(user, trip);
    }

    //Mail Service
    @Override
    public void sendContactMail(String subject, String text, String sender) throws MessagingException {
        MailSender.sendMail("'" + subject + "' from " +sender, text, "tripsnoreply@gmail.com");
    }

    @Override
    public void forgotPassword(String email) throws TripsException, MessagingException
    {
        userBL.forgotPassword(email);
    }
}
