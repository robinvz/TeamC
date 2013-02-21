package be.kdg.trips.persistence.dao.interfaces;

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
    public void saveOrUpdateEnrollment(Enrollment enrollment);
    public List getEnrollmentsByUser(User user);
    public List getEnrollmentsByTrip(Trip trip);
    public Enrollment getEnrollmentByUserAndTrip(User user, Trip trip);
    public Invitation getInvitationByUserAndTrip(User user, Trip trip);
    public void saveOrUpdateInvitation(Invitation invitation);
}
