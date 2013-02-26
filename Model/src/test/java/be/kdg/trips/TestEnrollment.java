package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestEnrollment
{
    private static TripsService tripsService;
    private static DateFormat df;
    private static User organizer;
    private static Trip trip;

    @BeforeClass
    public static void createEnrollmentManager() throws TripsException, ParseException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
        df = new SimpleDateFormat("dd/MM/yyyy");
        organizer  = tripsService.createUser(new User("keke.kokelenberg@student.kdg.be", "Keke"));
        trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, organizer);
    }

    @Test
    public void successfulSubscribe() throws TripsException, ParseException
    {
        User user = new User("eenGebruiker@kdg.be","twéè");
        User subscriber = tripsService.createUser(user);
        Enrollment enrollment = tripsService.subscribe(trip, subscriber);
        assertNotNull(enrollment);
    }

    @Test(expected = TripsException.class)
    public void failedSubscribeNoPermission() throws TripsException, ParseException
    {
        User user = new User("lol@kdg.be", "lolapsch");
        tripsService.subscribe(trip, user);
    }
    /*
    @Test(expected = TripsException.class)
    public void failedTripSubscribe() throws TripsException, ParseException
    {
        User organizer = tripsService.createUser("lepel","lapal");
        User user = tripsService.createUser("lopol", "lopol");
        Trip trip = new TimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, organizer);
        tripsService.enroll(trip, user);
    }
    */
    @Test(expected = TripsException.class)
    public void failedSubscribeTwice() throws TripsException, ParseException
    {
        User user = new User("zorro@kdg.be","zwaard");
        User subscriber = tripsService.createUser(user);
        tripsService.subscribe(trip, subscriber);
        tripsService.subscribe(trip, subscriber);
    }

    @Test(expected = TripsException.class)
    public void failedSubscribeInvalidPrivacyPublic() throws TripsException
    {
        User user = new User("jeremy@msn.be","zwaard");
        User subscriber = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip,subscriber);
    }

    @Test(expected = TripsException.class)
    public void failedSubscribeInvalidPrivacyPrivate() throws TripsException
    {
        User user = new User("jereaamy@msn.be","zwaard");
        User subscriber = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, subscriber);
    }

    @Test
    public void successfulFindEnrollmentByUser1() throws TripsException, ParseException {
        User user = new User("sergeant@kdg.be","pass");
        User subscriber = tripsService.createUser(user);
        Enrollment enrollment = tripsService.subscribe(trip, subscriber);
        assertEquals(1, tripsService.findEnrollmentsByUser(subscriber).size());
    }

    @Test
    public void successfulFindEnrollmentByUser2() throws TripsException, ParseException {
        User user = new User("zolow@kdg.be","pass");
        User subscriber = tripsService.createUser(user);
        assertTrue(tripsService.findEnrollmentsByUser(subscriber).isEmpty());
    }

    @Test
    public void successfulFindEnrollmentByTrip() throws ParseException, TripsException {
        User user = new User("general@kdg.be","pass");
        User subscriber = tripsService.createUser(user);
        Trip trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, organizer);
        Enrollment enrollment = tripsService.subscribe(trip, subscriber);
        assertEquals(2, tripsService.findEnrollmentsByTrip(trip).size());
    }

    @Test
    public void successfulInvite() throws TripsException {
        User user = new User("leopard@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        assertEquals(1,tripsService.findPrivateTrips(invitee).size());
    }

    @Test(expected = TripsException.class)
    public void failedInviteInvalidTripPrivacy() throws TripsException {
        User user = new User("leopardo@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
    }

    @Test
    public void succesfulAcceptInvitation() throws TripsException {
        User user = new User("leoguardo@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationNoInvitation() throws TripsException {
        User user = new User("leorosto@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.acceptInvitation(trip, invitee);
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationAlreadyAccepted() throws TripsException {
        User user = new User("leovago@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.acceptInvitation(trip, invitee);
    }
}
