package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.Status;
import be.kdg.trips.model.invitation.Answer;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.EnrollmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
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
        if(isUnexistingEnrollment(user, trip))
        {
            if(trip.isPublished() && !trip.isActive())
            {
                enrollment = new Enrollment(trip, user);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
            }
            else
            {
                throw new TripsException("Trip is either not published, already active");
            }
        }
        return enrollment;
    }

    @Override
    @Transactional
    public void disenroll(Trip trip, User user) throws TripsException {
        if(isExistingEnrollment(user, trip) && !trip.isActive())
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
                invitation.setAnswer(Answer.DECLINED);
                enrollmentDao.saveOrUpdateInvitation(invitation);
            }
            enrollmentDao.deleteEnrollment(enrollment.getId());
        }
        else
        {
            throw new TripsException("You can't disenroll from a trip that is currently active");
        }
    }

    @Override
    @Transactional
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException, MessagingException {
        Invitation invitation = null;
        if(isUnexistingInvitation(user, trip))
        {
            if (tripBL.isOrganizer(trip, organizer) && !trip.isActive() && trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                invitation = new Invitation(trip, user);
                enrollmentDao.saveOrUpdateInvitation(invitation);
                List<InternetAddress[]> recipients = new ArrayList<>();
                recipients.add(InternetAddress.parse(user.getEmail()));
                //give link to invitation!
                tripBL.sendMail("Trip invitation", "You have been invited by " + organizer.getFirstName() + " " + organizer.getLastName() + " for his trip named: '" + trip.getTitle() + "' (" + trip.getDescription() + ").\nClick here xxx if you're interested in joining.", recipients);
            }
            else
            {
                throw new TripsException("Trip is either not published, already active, not private or organizer doesn't exist");
            }
        }
        return invitation;
    }

    @Transactional
    @Override
    public Invitation selfInvite(Trip trip, User organizer) {
        Invitation invitation = new Invitation(trip, organizer);
        enrollmentDao.saveOrUpdateInvitation(invitation);
        return invitation;
    }

    @Override
    @Transactional
    public void uninvite(Trip trip, User organizer, User user) throws TripsException {
        if(isExistingInvitation(user, trip))
        {
            if (tripBL.isOrganizer(trip, organizer)&& isUnexistingEnrollment(user, trip))
            {
                Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
                enrollmentDao.deleteInvitation(invitation.getId());
            }
            else
            {
                throw new TripsException("Trip is either not published, already active, not private or organizer doesn't exist");
            }
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(User user) throws TripsException {
        List<Enrollment> enrollments = null;
        if(userBL.isExistingUser(user.getEmail()))
        {
            enrollments = enrollmentDao.getEnrollmentsByUser(user);
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByTrip(Trip trip) throws TripsException {
        List<Enrollment> enrollments = null;
        if(tripBL.isExistingTrip(trip.getId()))
        {
            enrollments = enrollmentDao.getEnrollmentsByTrip(trip);
        }
        return enrollments;
    }

    @Override
    public List<Invitation> getInvitationsByUser(User user) throws TripsException {
        List<Invitation> invitations = null;
        if(userBL.isExistingUser(user.getEmail()))
        {
            invitations = enrollmentDao.getInvitationsByUser(user);
        }
        return invitations;
    }

    @Override
    @Transactional
    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        if(isExistingEnrollment(user, trip) &&userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.addRequisite(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
    }

    @Override
    @Transactional
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        if(isExistingEnrollment(user, trip) &&userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.removeRequisite(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
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

    @Override
    @Transactional
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException {
        Enrollment enrollment = null;
        if(isExistingInvitation(user, trip) && !trip.isActive())
        {
            enrollment = enroll(trip, user);
            Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
            invitation.setAnswer(Answer.ACCEPTED);
            enrollmentDao.saveOrUpdateInvitation(invitation);
        }
        return enrollment;
    }

    @Override
    @Transactional
    public void declineInvitation(Trip trip, User user) throws TripsException {
        if(isExistingInvitation(user, trip) && isUnexistingEnrollment(user, trip))
        {
            Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
            if(invitation.getAnswer()!=Answer.DECLINED)
            {
                invitation.setAnswer(Answer.DECLINED);
                enrollmentDao.saveOrUpdateInvitation(invitation);
            }
            else
            {
                throw new TripsException("Invitation is already declined");
            }
        }
    }

    @Override
    @Transactional
    public Enrollment subscribe(Trip trip, User user) throws TripsException {
        Enrollment enrollment = null;
        if(trip.getPrivacy() == TripPrivacy.PROTECTED)
        {
            enrollment = enroll(trip, user);
        }
        else
        {
            throw new TripsException("You can't subscribe for a public or private trip");
        }
        return enrollment;
    }

    @Override
    @Transactional
    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException {
        if(isExistingEnrollment(user, trip))
        {
            boolean locationExists=false;
            for(Location tripLocations: trip.getLocations())
            {
                if(tripLocations.equals(location))
                {
                    locationExists = true;
                }
            }
            if(locationExists){
                Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
                enrollment.setLastLocationVisited(location);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
            }
            else
            {
                throw new TripsException("Location doesn't exist in selected trip");
            }
        }
    }

    @Override
    @Transactional
    public void checkAnswerFromQuestion(Question question, int answerIndex, User user) throws TripsException {
        Trip trip = tripBL.findTripByQuestion(question);
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(!enrollment.getAnsweredQuestions().contains(question.getId()))
            {
                if(enrollment.getStatus() == Status.BUSY)
                {
                    for(Location location: trip.getLocations())
                    {
                        if(location.getQuestion().equals(question))
                        {
                            if(enrollment.getLastLocationVisited().equals(location))
                            {
                                if(question.checkAnswer(answerIndex))
                                {
                                    enrollment.incrementScore();
                                }
                                enrollment.addAnsweredQuestion(question.getId());
                                enrollmentDao.saveOrUpdateEnrollment(enrollment);
                                break;
                            }
                            else
                            {
                                throw new TripsException("You haven't reached this location yet");
                            }
                        }
                    }
                }
                else
                {
                    throw new TripsException("You haven't started the trip yet");
                }

            }
            else
            {
                throw new TripsException("You already answered this question");
            }
        }
    }

    @Override
    @Transactional
    public void startTrip(Trip trip, User user) throws TripsException {
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(enrollment.getStatus()==Status.READY)
            {
                enrollment.setStatus(Status.BUSY);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
            }
            else
            {
                throw new TripsException("Enrollment is already started");
            }
        }
    }

    @Override
    @Transactional
    public String stopTrip(Trip trip, User user) throws TripsException {
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(enrollment.getStatus()==Status.BUSY)
            {
                enrollment.setStatus(Status.FINISHED);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
                return enrollment.getFullScore();
            }
            else
            {
                throw new TripsException("You can't finish a trip which you haven't begun yet");
            }
        }
        return "";
    }
}
