package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.businessLogic.exception.TripsException;
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
import be.kdg.trips.utility.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
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

    @Transactional
    @Override
    public Enrollment subscribe(Trip trip, User user) throws TripsException
    {
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
    public List<Enrollment> findEnrollmentsByUser(User user) throws TripsException
    {
        List<Enrollment> enrollments = null;
        if(userBL.isExistingUser(user.getEmail()))
        {
            enrollments = enrollmentDao.getEnrollmentsByUser(user);
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> findEnrollmentsByTrip(Trip trip) throws TripsException
    {
        List<Enrollment> enrollments = null;
        if(tripBL.isExistingTrip(trip.getId()))
        {
            enrollments = enrollmentDao.getEnrollmentsByTrip(trip);
        }
        return enrollments;
    }

    @Transactional
    @Override
    public void disenroll(Trip trip, User user) throws TripsException
    {
        if(!trip.getOrganizer().equals(user))
        {
            if(isExistingEnrollment(user, trip) && !trip.isActive())
            {
                Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
                if(enrollment.getStatus()!=Status.BUSY)
                {
                    if(trip.getPrivacy()==TripPrivacy.PRIVATE)
                    {
                        Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
                        invitation.setAnswer(Answer.DECLINED);
                        enrollmentDao.saveOrUpdateInvitation(invitation);
                    }
                    trip.removeEnrollment(enrollment);
                    enrollmentDao.deleteEnrollment(enrollment.getId());
                }
                else
                {
                    throw new TripsException("You can't disenroll from a trip which you are currently doing");
                }
            }
            else
            {
                throw new TripsException("You can't disenroll from a timebound trip that is currently active");
            }
        }
        else
        {
            throw new TripsException("You can't disenroll the organizer");
        }
    }

    @Transactional
    @Override
    public void addRequisiteToEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        if(isExistingEnrollment(user, trip) &&userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.addRequisite(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
    }

    @Transactional
    @Override
    public void removeRequisiteFromEnrollment(String name, int amount, Trip trip, User user, User organizer) throws TripsException
    {
        if(isExistingEnrollment(user, trip) &&userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.removeRequisite(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
    }

    @Transactional
    @Override
    public void addCostToEnrollment(String name, double amount, Trip trip, User user) throws TripsException
    {
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.addCost(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
    }

    @Transactional
    @Override
    public void removeCostFromEnrollment(String name, double amount, Trip trip, User user) throws TripsException
    {
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            enrollment.removeCost(name, amount);
            enrollmentDao.saveOrUpdateEnrollment(enrollment);
        }
    }

    @Transactional
    @Override
    public Invitation invite(Trip trip, User organizer, User user) throws TripsException, MessagingException
    {
        Invitation invitation = null;
        if(isUnexistingInvitation(user, trip))
        {
            if (userBL.isExistingUser(user.getEmail()) && userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer) && !trip.isActive() && trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                invitation = new Invitation(trip, user);
                enrollmentDao.saveOrUpdateInvitation(invitation);
                MailSender.sendMail("Trip invitation", "You have been invited by " + organizer.getFirstName() + " " + organizer.getLastName() + " for his trip named: '" + trip.getTitle() + "' (" + trip.getDescription() + ").\nGo to http://localhost:8080/login#"+trip.getId()+" if you're interested in joining.", user.getEmail());
            }
            else
            {
                throw new TripsException("Trip is already active or not private");
            }
        }
        return invitation;
    }

    /**
     * Used to invite the organizer into his own trip, ensuring editing and joining rights
     *
     * @param trip The trip that the organizer is being invited into
     * @param organizer the organizer that is being invited
     * @return the invitation created
     */
    @Override
    public Invitation selfInvite(Trip trip, User organizer)
    {
        Invitation invitation = new Invitation(trip, organizer);
        enrollmentDao.saveOrUpdateInvitation(invitation);
        return invitation;
    }

    @Override
    public List<Invitation> findInvitationsByUser(User user) throws TripsException
    {
        List<Invitation> invitations = null;
        if(userBL.isExistingUser(user.getEmail()))
        {
            invitations = enrollmentDao.getInvitationsByUser(user);
        }
        return invitations;
    }

    @Transactional
    @Override
    public Enrollment acceptInvitation(Trip trip, User user) throws TripsException
    {
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

    @Transactional
    @Override
    public void declineInvitation(Trip trip, User user) throws TripsException
    {
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

    @Transactional
    @Override
    public void uninvite(Trip trip, User organizer, User user) throws TripsException
    {
        if(!organizer.equals(user))
        {
            if(isExistingInvitation(user, trip))
            {
                if (userBL.isExistingUser(user.getEmail()) && userBL.isExistingUser(organizer.getEmail()) && tripBL.isOrganizer(trip, organizer)&& isUnexistingEnrollment(user, trip))
                {
                    Invitation invitation = enrollmentDao.getInvitationByUserAndTrip(user, trip);
                    enrollmentDao.deleteInvitation(invitation.getId());
                }
                else
                {
                    throw new TripsException("User already accepted the invitation");
                }
            }
        }
        else
        {
            throw new TripsException("You can't uninvite the organizer");
        }
    }

    @Transactional
    @Override
    public void startTrip(Trip trip, User user) throws TripsException
    {
        if(isExistingEnrollment(user, trip))
        {
            if(trip.isActive() || !trip.isTimeBoundTrip())
            {
                Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
                if(enrollment.getStatus()!=Status.BUSY)
                {
                    enrollment.setStatus(Status.BUSY);
                    enrollmentDao.saveOrUpdateEnrollment(enrollment);
                }
                else
                {
                    throw new TripsException("Enrollment is already started");
                }
            }
            else
            {
                throw new TripsException("Trip is not active yet");
            }
        }
    }

    @Transactional
    @Override
    public String stopTrip(Trip trip, User user) throws TripsException
    {
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(enrollment.getStatus()==Status.BUSY)
            {
                String fullScore = enrollment.getFullScore();
                enrollment.setScore(0);
                enrollment.setStatus(Status.FINISHED);
                enrollment.setLastLocationVisited(null);
                enrollment.setAnsweredQuestions(null);
                enrollmentDao.saveOrUpdateEnrollment(enrollment);
                return fullScore;
            }
            else
            {
                throw new TripsException("You can't finish a trip which you haven't begun yet");
            }
        }
        return "";
    }

    /**
     * Sets the location the user last visited in the model.
     *
     * @param trip The trip that is being executed
     * @param user the user executing the trip
     * @param location the location that is being visited
     * @throws TripsException if question is already answered, the trip hasn't been started yet, or the location hasn't been reached yet.
     */
    @Transactional
    @Override
    public void setLastLocationVisited(Trip trip, User user, Location location) throws TripsException
    {
        if(isExistingEnrollment(user, trip))
        {
            boolean locationExists=false;
            Trip foundTrip = tripBL.findTripById(trip.getId(), user);
            for(Location tripLocations: foundTrip.getLocations())
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

    /**
     * Returns true or false depending on whether or not the answer index is the correct one in the question.
     *
     * @param question The question that needs answering
     * @param answerIndex the index of the provided answer
     * @param user The user that provides the answer
     * @return true if answer is correct
     * @throws TripsException if question is already answered, the trip hasn't been started yet, or the location hasn't been reached yet.
     */
    @Transactional
    @Override
    public boolean checkAnswerFromQuestion(Question question, int answerIndex, User user) throws TripsException
    {
        Trip trip = question.getLocation().getTrip();
        boolean correct = false;
        if(isExistingEnrollment(user, trip))
        {
            Enrollment enrollment = enrollmentDao.getEnrollmentByUserAndTrip(user, trip);
            if(!enrollment.getAnsweredQuestions().containsKey(question))
            {
                if(enrollment.getStatus() == Status.BUSY)
                {
                    for(Location location: trip.getLocations())
                    {
                        if(location.getQuestion()!=null && location.getQuestion().equals(question))
                        {
                            Location lastLocationVisited = enrollment.getLastLocationVisited();
                            if(lastLocationVisited != null && lastLocationVisited.equals(location) && lastLocationVisited.getSequence() == location.getSequence())
                            {
                                if(question.checkAnswer(answerIndex))
                                {
                                    enrollment.incrementScore();
                                    correct = true;
                                }
                                enrollment.addAnsweredQuestion(question, correct);
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
        return correct;
    }

    @Override
    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException
    {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isExistingEnrollment(user, trip);
        }
        return false;
    }

    @Override
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException
    {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isUnexistingEnrollment(user, trip);
        }
        return false;
    }

    @Override
    public boolean isUserEnrolled(User user, Trip trip)
    {
        try {
            return enrollmentDao.isExistingEnrollment(user, trip);
        } catch (TripsException e) {
            return false;
        }
    }

    @Override
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException
    {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isExistingInvitation(user, trip);
        }
        return false;
    }

    @Override
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException
    {
        if(userBL.isExistingUser(user.getEmail()) && tripBL.isExistingTrip(trip.getId()))
        {
            return enrollmentDao.isUnexistingInvitation(user, trip);
        }
        return false;
    }

    private Enrollment enroll(Trip trip, User user) throws TripsException
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
                throw new TripsException("Trip is either not published or already active");
            }
        }
        return enrollment;
    }
}
