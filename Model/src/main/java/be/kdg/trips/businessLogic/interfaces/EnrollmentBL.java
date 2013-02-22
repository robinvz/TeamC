package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;

import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface EnrollmentBL
{
    public Enrollment enroll(Trip trip, User user) throws TripsException;
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException;

    public List<Enrollment> getEnrollmentsByUser(User user) throws TripsException;
    public List<Enrollment> getEnrollmentsByTrip(Trip trip) throws TripsException;

    public void disenroll(Trip trip, User user);
    public void uninvite(Trip trip, User user);

    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException;
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException;
}
