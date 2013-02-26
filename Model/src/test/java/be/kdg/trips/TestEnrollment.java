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
        organizer  = tripsService.createUser("keke.kokelenberg@student.kdg.be", "Keke");
        trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, organizer);
    }

    @Test
    public void successfulSubscribe() throws TripsException, ParseException
    {
        User user = tripsService.createUser("eenGebruiker@kdg.be","twéè");
        Enrollment enrollment = tripsService.subscribe(trip, user);
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
        User user = tripsService.createUser("zorro@kdg.be","zwaard");
        tripsService.subscribe(trip, user);
        tripsService.subscribe(trip, user);
    }

    @Test(expected = TripsException.class)
    public void failedSubscribeInvalidPrivacyPublic() throws TripsException
    {
        User user = tripsService.createUser("jeremy@msn.be","zwaard");
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip,user);
    }

    @Test(expected = TripsException.class)
    public void failedSubscribeInvalidPrivacyPrivate() throws TripsException
    {
        User user = tripsService.createUser("jeremay@msn.be","zwaard");
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip,user);
    }

    @Test
    public void successfulFindEnrollmentByUser1() throws TripsException, ParseException {
        User user = tripsService.createUser("sergeant@kdg.be","pass");
        Enrollment enrollment = tripsService.subscribe(trip, user);
        assertEquals(1, tripsService.findEnrollmentsByUser(user).size());
    }

    @Test
    public void successfulFindEnrollmentByUser2() throws TripsException, ParseException {
        User user = tripsService.createUser("zolow@kdg.be","pass");
        assertTrue(tripsService.findEnrollmentsByUser(user).isEmpty());
    }

    @Test
    public void successfulFindEnrollmentByTrip() throws ParseException, TripsException {
        User user = tripsService.createUser("general@kdg.be","pass");
        Trip trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, organizer);
        Enrollment enrollment = tripsService.subscribe(trip, user);
        assertEquals(2, tripsService.findEnrollmentsByTrip(trip).size());
    }

    @Test
    public void successfulInvite() throws TripsException {
        User user = tripsService.createUser("leopard@hotmail.com","pass");
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
        assertEquals(1,tripsService.findPrivateTrips(user).size());
    }

    @Test(expected = TripsException.class)
    public void failedInviteInvalidTripPrivacy() throws TripsException {
        User user = tripsService.createUser("leopardo@hotmail.com","pass");
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
    }

    @Test
    public void succesfulAcceptInvitation() throws TripsException {
        User user = tripsService.createUser("leoguardo@hotmail.com","pass");
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
        tripsService.acceptInvitation(trip, user);
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationNoInvitation() throws TripsException {
        User user = tripsService.createUser("leorosto@hotmail.com","pass");
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.acceptInvitation(trip, user);
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationAlreadyAccepted() throws TripsException {
        User user = tripsService.createUser("leovago@hotmail.com","pass");
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
        tripsService.acceptInvitation(trip, user);
        tripsService.acceptInvitation(trip, user);
    }
}
