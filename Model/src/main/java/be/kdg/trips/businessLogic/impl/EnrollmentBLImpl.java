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
        if (userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()) && trip.isPublished() && !trip.isActive() && trip.getPrivacy()!= TripPrivacy.PUBLIC)
        {
            if(!isExistingEnrollment(user,trip))
            {
                enrollment = new Enrollment(trip, user);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
            }
            else
            {
                throw new TripsException("User is already enrolled for the trip");
            }
        }
        else
        {
            throw new TripsException("Trip is either not published, already active, or public");
        }

        return enrollment;
    }

    @Override
    public Invitation invite(Trip trip, User user) throws TripsException
    {
        Invitation invitation = null;
        if (userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()) && trip.isPublished() && !trip.isActive() && trip.getPrivacy()==TripPrivacy.PRIVATE)
        {
            if(!isExistingInvitation(user, trip)){
                invitation = new Invitation(trip, user);
                enrollmentDao.saveOrUpdateInvitation(invitation);
            }
            else
            {
                throw new TripsException("User is already invited for the trip");
            }
        }
        else
        {
            throw new TripsException("Trip is either not published, already active, or private");
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

    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException {
        if(!enrollmentDao.getEnrollmentByUserAndTrip(user, trip).isNull())
        {
            throw new TripsException("Enrollment already exists");
        }
        return false;
    }

    public boolean isExistingInvitation(User user, Trip trip) throws TripsException {
        if(!enrollmentDao.getInvitationByUserAndTrip(user, trip).isNull())
        {
            throw new TripsException("Invitation already exists");
        }
        return false;
    }
    /*
    private boolean checkUserAndTrip(User user, Trip trip) throws TripsException
    {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            if (trip.isPublished() && !trip.isActive())
            {
                return true;
            }
            else
            {
                throw new TripsException("Trip is either unpublished or already active");
            }
        }
        else
        {
            throw new TripsException("User doesn't exist");
        }
    }
    */
}
