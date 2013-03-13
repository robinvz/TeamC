package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface EnrollmentBL
{
    public Enrollment subscribe(Trip trip, User user) throws TripsException;
    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException;
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException;
    public void disenroll(Trip trip, User user) throws TripsException;

    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException;
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException;
    public void addCostToEnrollment(String name, double amount, Trip trip, User user) throws TripsException;
    public void removeCostFromEnrollment(String name, double amount, Trip trip, User user) throws TripsException;

    public Invitation invite(Trip trip, User organizer, User user) throws TripsException, MessagingException;
    public Invitation selfInvite(Trip trip, User organizer);
    public List<Invitation> findInvitationsByUser(User user) throws TripsException;
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException;
    public void declineInvitation(Trip trip, User user) throws TripsException;
    public void uninvite(Trip trip, User organizer, User user) throws TripsException;

    public void startTrip(Trip trip, User user) throws TripsException;
    public String stopTrip(Trip trip, User user) throws TripsException;

    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException;
    public boolean checkAnswerFromQuestion(Question question, int answerIndex, User user) throws TripsException;

    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isUserEnrolled(User user, Trip trip);
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException;
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException;
}
