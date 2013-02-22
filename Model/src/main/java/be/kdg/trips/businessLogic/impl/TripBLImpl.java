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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
        }
        return trip;
    }

    @Override
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) throws TripsException {
        Trip trip=null;
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            if(startDate.after(new Date()))
            {
                if(startDate.before(endDate))
                {
                    trip = new TimeBoundTrip(title, description, privacy, organizer, startDate, endDate);
                    tripDao.saveOrUpdateTrip(trip);
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
        return trip;
    }

    @Override
    public List<Trip> findNonPrivateTripsByKeyword(String keyword) {
        List<Trip> trips = tripDao.getNonPrivateTripsByKeyword(keyword.toLowerCase());
        return trips;
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException {
        List trips = new ArrayList<Trip>();
        if(user==null)
        {
            List<Trip> publicTripsWithLocations = tripDao.getPublicTrips();
            List<Trip> protectedTripsWithoutLocations = tripDao.getProtectedTripsWithoutDetails();
            trips.addAll(publicTripsWithLocations);
            trips.addAll(protectedTripsWithoutLocations);
        }
        else if(userBL.isExistingUser(user.getEmail()))
        {
            List<Trip> publicTripsWithLocations = tripDao.getPublicTrips();
            List<Trip> protectedTripsWithLocations = tripDao.getProtectedTrips();
            trips.addAll(publicTripsWithLocations);
            trips.addAll(protectedTripsWithLocations);
        }
        return trips;
    }

    @Override
    public List<Trip> findPrivateTrips(User user) throws TripsException {
        List<Trip> trips = new ArrayList<>();
        User foundUser = userBL.findUserWithDetails(user.getEmail());
        for(Invitation invitation: user.getInvitations())
        {
            trips.add(invitation.getTrip());
        }
        return trips;
    }

    @Override
    public Trip findTripById(int id) throws TripsException {
        return tripDao.getTrip(id);

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
                    enrollmentBL.enroll(trip,trip.getOrganizer());
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
    public void addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            Location location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, province, country), title, description);
            trip.addLocation(location);
            tripDao.saveOrUpdateTrip(trip);
        }
    }

    @Override
    public void addLocationToTrip(User user, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String province, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            if(correctAnswerIndex<possibleAnswers.size())
            {
                Location location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, province, country), title, description, new Question(question, possibleAnswers, correctAnswerIndex));
                trip.addLocation(location);
                tripDao.saveOrUpdateTrip(trip);
            }
            else
            {
                throw new TripsException("The answer doesn't exist");
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

    private void sendMail(String subject, String text, List<InternetAddress[]> recipients) throws MessagingException {
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
}