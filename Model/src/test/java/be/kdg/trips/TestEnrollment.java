package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Answer;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

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
    private final int FIRST_ELEMENT = 0;

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
        User subscriber = tripsService.createUser(new User("jereaamy@msn.be","zwaard"));
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PRIVATE, organizer);
        tripsService.subscribe(trip, subscriber);
    }

    @Test
    public void successfulDisenrollProtectedTrip() throws TripsException
    {
        User subscriber = tripsService.createUser(new User("bobette@msn.com","paard"));
        Trip trip = tripsService.createTimelessTrip("suske & wiske avontuur", "dulle griet", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(trip,organizer);
        tripsService.subscribe(trip, subscriber);
        tripsService.disenroll(trip, subscriber);
        assertTrue(tripsService.findEnrollmentsByUser(subscriber).isEmpty());
    }

    @Test
    public void successfulDisenrollPrivateTrip() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting3@msn.com","gans"));
        Trip trip = tripsService.createTimelessTrip("zalt gaan ja", "ddt", TripPrivacy.PRIVATE, organizer);
        tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.disenroll(trip, invitee);
        assertEquals(Answer.DECLINED,tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
    }

    @Test(expected = TripsException.class)
    public void failedDisenrollNotEnrolled() throws TripsException
    {
        User subscriber = tripsService.createUser(new User("clarkson@msn.com","gans"));
        Trip trip = tripsService.createTimelessTrip("zalt gaan ja", "ddt", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.disenroll(trip, subscriber);
    }

    @Test
    public void successfulFindEnrollmentByUser1() throws TripsException, ParseException {
        User subscriber = tripsService.createUser(new User("sergeant@kdg.be","pass"));
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
    public void successfulInvite() throws TripsException, MessagingException {
        User user = new User("unexisting1@student.kdg.be","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        assertEquals(1,tripsService.findPrivateTrips(invitee).size());
    }

    @Test(expected = TripsException.class)
    public void failedInviteInvalidTripPrivacy() throws TripsException, MessagingException {
        User user = new User("unexisting2@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, user);
    }

    @Test
    public void successfulUninvite() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting5@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacusaaa", "Lopen", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.uninvite(trip, organizer, invitee);
        assertTrue(tripsService.findPrivateTrips(invitee).isEmpty());
    }

    @Test(expected = TripsException.class)
    public void failedUninviteAlreadyEnrolled() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting7@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacusaaa", "Lopen", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.uninvite(trip, organizer, invitee);
        assertTrue(tripsService.findPrivateTrips(invitee).isEmpty());
    }

    @Test
    public void succesfulAcceptInvitation() throws TripsException, MessagingException {
        User user = new User("unexisting9@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        assertEquals(Answer.ACCEPTED,tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationNoInvitation() throws TripsException {
        User invitee = tripsService.createUser(new User("leorosto@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.acceptInvitation(trip, invitee);
    }

    @Test(expected = TripsException.class)
    public void failedAcceptInvitationAlreadyAccepted() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting22@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.acceptInvitation(trip, invitee);
    }

    @Test
    public void successfulDeclineInvitation() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting99@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.declineInvitation(trip, invitee);
        assertEquals(Answer.DECLINED,tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
    }

    @Test(expected = TripsException.class)
    public void failedDeclineInvitationNoInvitation() throws TripsException {
        User invitee = tripsService.createUser(new User("fergo@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.declineInvitation(trip, invitee);
    }

    @Test(expected = TripsException.class)
    public void failedDeclineInvitationAlreadyAccepted() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting101@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.declineInvitation(trip, invitee);
    }

    @Test(expected = TripsException.class)
    public void failedDeclineInvitationAlreadDeclined() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting201@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.declineInvitation(trip, invitee);
        tripsService.declineInvitation(trip, invitee);
    }

    @Test
    public void successfulSetLastLocationVisited() throws TripsException {
        User user = tripsService.createUser(new User("lampekap@hotmail.com","pass"));
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer);
        Location loc1 = tripsService.addLocationToTrip(organizer, trip, 12.00, 160.00, null, null, null, null, null, null, "Loc1","Location1");
        Location loc2 = tripsService.addLocationToTrip(organizer, trip, 12.00, 160.00, null, null, null, null, null, null, "Loc2","Location2");
        tripsService.publishTrip(trip,organizer);
        tripsService.subscribe(trip, user);
        tripsService.setLastLocationVisited(trip, user, loc1);
    }
    /*
    @Test
    public void successfulAddRequisiteToEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PUBLIC, organizer);
        User user = tripsService.createUser(new User("kiki@hotmail.com", "pass"));
        tripsService.addRequisiteToEnrollment("liters bier", 10, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("liters bier", 5, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("vrienden", 5, trip, user, organizer);
        assertEquals(2, trip.getRequisites().size());
    }

    @Test
    public void successfulRemoveRequisiteFromEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PUBLIC, organizer);
        User user = tripsService.createUser(new User("koko@hotmail.com", "pass"));
        tripsService.addRequisiteToEnrollment("liters bier", 10, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("liters bier", 5, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("vrienden", 5, trip, user, organizer);
        tripsService.removeRequisiteFromEnrollment("liters bier", 12, trip, user, organizer);
        tripsService.removeRequisiteFromEnrollment("vrienden", 6, trip, user, organizer);
        assertEquals(1, trip.getRequisites().size());
    }
    */
}
