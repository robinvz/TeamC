package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.TripDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class TripBLImpl implements TripBL
{
    @Autowired
    private TripDao tripDao;

    @Autowired
    private UserBL userBL;

    @Autowired
    private EnrollmentBL enrollmentBL;

    @Override
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException {
        Trip trip = null;
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            trip = new TimelessTrip(title, description, privacy, organizer);
            tripDao.saveOrUpdateTrip(trip);
            if(trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                enrollmentBL.enroll(trip, organizer);
            }
        }
        return trip;
    }

    @Override
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripsException {
        Trip trip=null;
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            if(areDatesValid(startDate, endDate))
            {
                trip = new TimeBoundTrip(title, description, privacy, organizer, startDate, endDate);
                tripDao.saveOrUpdateTrip(trip);
                if(trip.getPrivacy()==TripPrivacy.PRIVATE)
                {
                    enrollmentBL.enroll(trip, organizer);
                }
            }
        }
        return trip;
    }

    @Override
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException
    {
        List<Trip> trips = new ArrayList<Trip>();
        if(user == null)
        {
            trips.addAll(tripDao.getPublicTripsByKeyword(keyword.toLowerCase()));
            trips.addAll(tripDao.getProtectedTripsWithoutDetailsByKeyword(keyword.toLowerCase()));
        }
        else if(userBL.isExistingUser(user.getEmail()))
        {
            trips.addAll(tripDao.getPublicTripsByKeyword(keyword.toLowerCase()));
            trips.addAll(tripDao.getProtectedTripsByKeyword(keyword.toLowerCase()));
        }
        return trips;
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException {
        List trips = new ArrayList<Trip>();
        if(user==null)
        {
            trips.addAll(tripDao.getPublicTrips());
            trips.addAll(tripDao.getProtectedTripsWithoutDetails());
        }
        else if(userBL.isExistingUser(user.getEmail()))
        {
            trips.addAll(tripDao.getPublicTrips());
            trips.addAll(tripDao.getProtectedTrips());
        }
        return trips;
    }

    @Override
    public List<Trip> findPrivateTrips(User user) throws TripsException {
        List<Trip> trips = new ArrayList<>();
        User foundUser = userBL.findUserWithDetails(user.getEmail());
        for(Invitation invitation: foundUser.getInvitations())
        {
            trips.add(invitation.getTrip());
        }
        return trips;
    }

    @Override
    public Trip findTripById(int id, User user) throws TripsException
    {
        Trip trip = tripDao.getTrip(id);
        switch(trip.getPrivacy())
        {
            case PUBLIC:
                return trip;
            case PROTECTED:
                if(user == null)
                {
                    trip.setLocations(null);
                    trip.setEnrollments(null);
                    return trip;
                }
                else if(userBL.isExistingUser(user.getEmail()))
                {
                    return trip;
                }
                break;
            case PRIVATE:
                user = userBL.findUser(user.getEmail());
                if(enrollmentBL.isExistingInvitation(user, trip))
                {
                    return trip;
                }
        }
        throw new TripsException("You do not have viewing rights for this trip");
    }

    @Override
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException {
        List<Trip> trips = new ArrayList<>();
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            trips = tripDao.getTripsByOrganizer(organizer);
        }
        return trips;
    }

    @Override
    public void publishTrip(Trip trip, User user) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            if(!trip.isPublished())
            {
                trip.setPublished(true);
                if(trip.getPrivacy()!=TripPrivacy.PUBLIC)
                {
                    User organizer = userBL.findUserWithDetails(user.getEmail());
                    enrollmentBL.enroll(trip, organizer);
                }
                tripDao.saveOrUpdateTrip(trip);
            }
            else
            {
                throw new TripsException("Trip is already published");
            }
        }
    }

    @Override
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            trip.addLabel(label);
            tripDao.saveOrUpdateTrip(trip);
        }
    }

    @Override
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException {
        Location location = null;
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, province, country), title, description, trip.getLocations().size()+1);
            trip.addLocation(location);
            tripDao.saveOrUpdateTrip(trip);
        }
        return location;
    }

    @Override
    public Location addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException {
        Location location = null;
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            if(correctAnswerIndex<possibleAnswers.size())
            {
                location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, province, country), title, description,trip.getLocations().size()+1, new Question(question, possibleAnswers, correctAnswerIndex));
                trip.addLocation(location);
                tripDao.saveOrUpdateTrip(trip);
            }
            else
            {
                throw new TripsException("The answer doesn't exist");
            }
        }
        return location;
    }

    @Override
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            if(areDatesUnoccupied(startDate, endDate, trip))
            {
                ((TimeBoundTrip) trip).addDates(startDate, endDate);
                tripDao.saveOrUpdateTrip(trip);
            }
        }
    }

    @Override
    public void deleteTrip(Trip trip, User user) throws TripsException, MessagingException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            List<InternetAddress[]> recipients = new ArrayList<>();
            for (Enrollment e: trip.getEnrollments())
            {
                recipients.add(InternetAddress.parse(e.getUser().getEmail()));
            }
            tripDao.deleteTrip(trip);
            sendMail("Trip '"+trip.getTitle()+ "'", "We regret to inform you that the following trip: '"+trip.getTitle()+" - "+trip.getDescription()+"' has been canceled by the organizer.", recipients);
        }
    }

    @Override
    public boolean isExistingTrip(int id) throws TripsException {
        return tripDao.isExistingTrip(id);
    }

    @Override
    public boolean isOrganizer(Trip trip, User organizer) throws TripsException
    {
        if(trip.getOrganizer().getEmail().equals(organizer.getEmail()))
        {
            return true;
        }
        throw new TripsException("User with email '"+organizer.getEmail()+"' is not the organizer of the selected trip");
    }

    private boolean areDatesValid(Date startDate, Date endDate) throws TripsException {
        if(startDate.after(new Date()))
        {
            if(startDate.before(endDate))
            {
                return true;
            }
            else
            {
                throw new TripsException("Start date must be before end date");
            }
        }
        else
        {
            throw new TripsException("Start date must be in the future");
        }
    }

    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            int locationsSize = trip.getLocations().size()+1;
            List<Location> locations = trip.getLocations();
            if(location1 == location2)
            {

            }
            else if(locationsSize < 1 || (location1 <= locationsSize && location1 > 0) || (location2 <= locationsSize && location2 > 0))
            {
                trip.getLocations().get(location1-1).setSequence(location2);
                trip.getLocations().get(location2-1).setSequence(location1);
                tripDao.saveOrUpdateTrip(trip);
            }
            else
            {
                throw new TripsException("Locations couldn't be switched because they don't exist");
            }
        }
    }

    private boolean areDatesUnoccupied(Date startDate, Date endDate, Trip trip) throws TripsException {
        if(trip.isTimeBoundTrip())
        {
            Map<Date, Date> dates = ((TimeBoundTrip) trip).getDates();
            for(Map.Entry datePair : dates.entrySet())
            {
                if((((Date)datePair.getKey()).before(startDate) && ((Date)datePair.getValue()).after(startDate) || (((Date)datePair.getKey()).before(endDate) && ((Date)datePair.getValue()).after(endDate))))
                {
                    throw new TripsException("Dates are already occupied");
                }
            }
        }
        else
        {
            throw new TripsException("Trip is not a TimeBound trip");
        }
        return true;
    }

    @Override
    public void sendMail(String subject, String text, List<InternetAddress[]> recipients) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("tripsnoreply@gmail.com", "tripstrips");
                    }
                });

        try
        {
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            message.setText(text);
            for (InternetAddress[] recipient :recipients)
            {
                message.addRecipients(Message.RecipientType.TO, recipient);
            }
            Transport.send(message);
        }
        catch(MessagingException msgex)
        {
            throw new MessagingException("Failed to send email");
        }
    }
}
