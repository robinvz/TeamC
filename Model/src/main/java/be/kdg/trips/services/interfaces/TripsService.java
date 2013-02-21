package be.kdg.trips.services.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
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
public interface TripsService
{
    //User Service
    public User createUser(String email, String password) throws TripsException;

    public User findUser(String email) throws TripsException;
    public boolean checkLogin(String email, String password);

    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country) throws TripsException;
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException;

    public void deleteUser(User user) throws TripsException;


    //Trip Service
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException;
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripsException;

    public List findNonPrivateTripsByKeyword(String keyword) throws TripsException;
    public List findAllNonPrivateTrips(User loggedInUser) throws TripsException;
    public List findPrivateTrips(User loggedInUser) throws TripsException;
    public Trip findTripById(int id) throws TripsException;

    public void publishTrip(Trip trip, User organizer) throws TripsException;
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException;
    public void addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException;
    public void addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException;

    public void deleteTrip(Trip trip, User organizer) throws TripsException, MessagingException;

    //Enrollment Service
    public Enrollment enroll(Trip trip, User user) throws TripsException;

    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException;
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException;

    public Invitation invite(Trip trip, User organizer, User user) throws TripsException;
}
