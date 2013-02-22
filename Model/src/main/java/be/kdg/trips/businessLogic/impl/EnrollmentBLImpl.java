package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.EnrollmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class EnrollmentBLImpl implements EnrollmentBL
{
    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private UserBL userBL;

    @Autowired
    private TripBL tripBL;

    @Override
    public Enrollment enroll(Trip trip, User user) throws TripsException
    {
        Enrollment enrollment = null;
        if(isUnexistingEnrollment(user,trip))
        {
            if(trip.isPublished() && !trip.isActive() && trip.getPrivacy()!= TripPrivacy.PUBLIC)
            {
                enrollment = new Enrollment(trip, user);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
            }
            else
            {
                throw new TripsException("Trip is either not published, already active, or public");
            }
        }
        return enrollment;
    }

    @Override
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException
    {
        Invitation invitation = null;
        if(isUnexistingInvitation(user, trip))
        {
            if (userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer) && trip.isPublished() && !trip.isActive() && trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                invitation = new Invitation(trip, user);
                enrollmentDao.saveOrUpdateInvitation(invitation);
            }
            else
            {
                throw new TripsException("Trip is either not published, already active, private or organizer doesn't exist");
            }
        }
        return invitation;
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(User user) throws TripsException {
        List<Enrollment> enrollments = null;
        if(userBL.isExistingUser(user.getEmail())){
            enrollments = enrollmentDao.getEnrollmentsByUser(user);
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByTrip(Trip trip) throws TripsException {
        List<Enrollment> enrollments = null;
        if(tripBL.isExistingTrip(trip.getId())){
            enrollments = enrollmentDao.getEnrollmentsByTrip(trip);
        }
        return enrollments;
    }


    @Override
    public void disenroll(Trip trip, User user)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void uninvite(Trip trip, User user)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isExistingEnrollment(user, trip);
        }
        return false;
    }

    @Override
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isUnexistingEnrollment(user, trip);
        }
        return false;
    }

    @Override
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isExistingInvitation(user, trip);
        }
        return false;
    }

    @Override
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isUnexistingInvitation(user, trip);
        }
        return false;
    }
}
