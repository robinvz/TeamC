package be.kdg.trips.services.impl;


import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

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
    private UserBL userController;
    @Autowired
    private TripBL tripController;
    @Autowired
    private EnrollmentBL enrollmentController;

    //User Service
    @Override
    public User createUser(User user) throws TripsException {
        return userController.createUser(user);
    }

    @Override
    public User findUser(String email) throws TripsException
    {
        return userController.findUser(email);
    }

    @Override
    public List<User> findUsersByKeyword(String keyword, User user) throws TripsException {
        return userController.findUsersByKeyword(keyword, user);
    }

    @Override
    public boolean checkLogin(String email, String password) throws TripsException {
        return userController.checkLogin(email, password);
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country, byte[] profilePicture) throws TripsException
    {
        userController.updateUser(user, firstName, lastName, street, houseNr, city, postalCode, province, country, profilePicture);
    }

    @Override
    public void changePassword(User loggedInUser, String oldPassword, String newPassword) throws TripsException
    {
        userController.changePassword(loggedInUser, oldPassword, newPassword);
    }

    @Override
    public void deleteUser(User user) throws TripsException
    {
        userController.deleteUser(user);
    }

    //Trip Service
    @Override
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException {
        return tripController.createTimelessTrip(title, description, privacy, organizer);
    }

    @Override
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripsException
    {
        return tripController.createTimeBoundTrip(title, description, privacy, organizer, startDate, endDate);
    }

    @Override
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User loggedInUser) throws TripsException
    {
        return tripController.findNonPrivateTripsByKeyword(keyword, loggedInUser);
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User loggedInUser) throws TripsException {
        return tripController.findAllNonPrivateTrips(loggedInUser);
    }

    @Override
    public List<Trip> findPrivateTrips(User loggedInUser) throws TripsException {
        return tripController.findPrivateTrips(loggedInUser);
    }

    @Override
    public Trip findTripById(int id, User loggedInUser) throws TripsException {
        return tripController.findTripById(id, loggedInUser);
    }

    @Override
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException {
        return tripController.findTripsByOrganizer(organizer);
    }

    @Override
    public void publishTrip(Trip trip, User loggedInUser) throws TripsException {
        tripController.publishTrip(trip, loggedInUser);
    }

    @Override
    public void addLabelToTrip(Trip trip, User loggedInUser, String label) throws TripsException
    {
        tripController.addLabelToTrip(trip, loggedInUser, label);
    }

    @Override
    public Location addLocationToTrip(User loggedInUser, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException {
        return tripController.addLocationToTrip(loggedInUser, trip, latitude, longitude, street, houseNr, city, postalCode, province, country, title, description);
    }

    @Override
    public Location addLocationToTrip(User loggedInUser, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException {
        return tripController.addLocationToTrip(loggedInUser, trip, latitude, longitude, street, houseNr, city, postalCode, province, country, title, description, question, possibleAnswers, correctAnswerIndex);
    }

    @Override
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User loggedInUser) throws TripsException {
        tripController.addDateToTimeBoundTrip(startDate, endDate, trip, loggedInUser);
    }

    @Override
    public void addRequisiteToTrip(String name, int amount, Trip trip, User organizer) throws TripsException
    {
        tripController.addRequisiteToTrip(name, amount, trip, organizer);
    }

    @Override
    public void removeRequisiteFromTrip(String name, int amount, Trip trip, User organizer) throws TripsException
    {
        tripController.removeRequisiteFromTrip(name, amount, trip, organizer);
    }

    @Override
    public void switchLocationSequence(Trip trip, User loggedInUser, int location1, int location2) throws TripsException {
        tripController.switchLocationSequence(trip, loggedInUser, location1, location2);
    }

    @Override
    public void deleteTrip(Trip trip, User loggedInUser) throws TripsException, MessagingException {
        tripController.deleteTrip(trip, loggedInUser);
    }

    //Enrollment Service
    @Override
    public Enrollment subscribe(Trip trip, User loggedInUser) throws TripsException
    {
        return enrollmentController.subscribe(trip, loggedInUser);
    }

    @Override
    public Enrollment acceptInvitation(Trip trip, User loggedInUser) throws TripsException {
        return enrollmentController.acceptInvitation(trip, loggedInUser);
    }

    @Override
    public void declineInvitation(Trip trip, User loggedInUser) throws TripsException {
        enrollmentController.declineInvitation(trip, loggedInUser);
    }

    @Override
    public void disenroll(Trip trip, User user) throws TripsException {
        enrollmentController.disenroll(trip, user);
    }

    @Override
    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException {
        enrollmentController.setLastLocationVisited(trip, user, location);
    }

    @Override
    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException {
        return enrollmentController.getEnrollmentsByUser(user);
    }

    @Override
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException {
        return enrollmentController.getEnrollmentsByTrip(trip);
    }

    @Override
    public List<Invitation> findInvitationsByUser(User user) throws TripsException {
        return enrollmentController.getInvitationsByUser(user);
    }

    @Override
    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        enrollmentController.addRequisiteToEnrollment(name, amount, trip, user, organizer);
    }

    @Override
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        enrollmentController.removeRequisiteFromEnrollment(name, amount, trip, user, organizer);
    }

    @Override
    public Invitation invite(Trip trip, User loggedInUser, User invitee) throws TripsException, MessagingException {
        return enrollmentController.invite(trip, loggedInUser, invitee);
    }

    @Override
    public void uninvite(Trip trip, User organizer, User user) throws TripsException {
        enrollmentController.uninvite(trip, organizer, user);
    }
}
