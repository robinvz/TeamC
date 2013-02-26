package be.kdg.trips.services.impl;


import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
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
    public User createUser(String email, String password) throws TripsException
    {
        return userController.createUser(email, password);
    }

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
    public boolean checkLogin(String email, String password) throws TripsException {
        return userController.checkLogin(email, password);
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country) throws TripsException
    {
        userController.updateUser(user, firstName, lastName, street, houseNr, city, postalCode, province, country);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException
    {
        userController.changePassword(user, oldPassword, newPassword);
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
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException
    {
        return tripController.findNonPrivateTripsByKeyword(keyword, user);
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException {
        return tripController.findAllNonPrivateTrips(user);
    }

    @Override
    public List<Trip> findPrivateTrips(User user) throws TripsException {
        return tripController.findPrivateTrips(user);
    }

    @Override
    public Trip findTripById(int id, User user) throws TripsException {
        return tripController.findTripById(id, user);
    }

    @Override
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException {
        return tripController.findTripsByOrganizer(organizer);
    }

    @Override
    public List<Trip> findAttendingTrips(User user) throws TripsException {
        return tripController.findAttendingTrips(user);
    }

    @Override
    public void publishTrip(Trip trip, User organizer) throws TripsException {
        tripController.publishTrip(trip, organizer);
    }

    @Override
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException
    {
        tripController.addLabelToTrip(trip, organizer, label);
    }

    @Override
    public void addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException {
        tripController.addLocationToTrip(organizer, trip, latitude, longitude, street, houseNr, city, postalCode, province, country, title, description);
    }

    @Override
    public void addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException {
        tripController.addLocationToTrip(organizer, trip, latitude, longitude, street, houseNr, city, postalCode, province, country, title, description, question, possibleAnswers, correctAnswerIndex);
    }

    @Override
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException {
        tripController.addDateToTimeBoundTrip(startDate, endDate, trip, organizer);
    }

    @Override
    public void deleteTrip(Trip trip, User organizer) throws TripsException, MessagingException {
        tripController.deleteTrip(trip, organizer);
    }

    //Enrollment Service
    @Override
    public Enrollment subscribe(Trip trip, User user) throws TripsException
    {
        return enrollmentController.subscribe(trip, user);
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
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException {
        return enrollmentController.invite(trip, organizer, user);
    }

    @Override
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException {
        return enrollmentController.acceptInvitation(trip, user);
    }
}
