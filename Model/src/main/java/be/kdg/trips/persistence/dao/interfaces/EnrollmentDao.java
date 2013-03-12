package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.businessLogic.exception.TripsException;
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
public interface EnrollmentDao
{
    public List<Enrollment> getEnrollmentsByUser(User user);
    public List<Enrollment> getEnrollmentsByTrip(Trip trip);
    public Enrollment getEnrollmentByUserAndTrip(User user, Trip trip) throws TripsException;
    public List<Invitation> getInvitationsByUser(User user);
    public Invitation getInvitationByUserAndTrip(User user, Trip trip) throws TripsException;

    public void saveOrUpdateEnrollment(Enrollment enrollment);
    public void saveOrUpdateInvitation(Invitation invitation);

    public void deleteEnrollment(int id);
    public void deleteInvitation(int id);

    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException;
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException;
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException;
}
