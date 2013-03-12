package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.Status;
import be.kdg.trips.model.invitation.Answer;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionSystemException;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    private final int SECOND_ELEMENT = 1;

    @BeforeClass
    public static void createEnrollmentManager() throws TripsException, ParseException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
        df = new SimpleDateFormat("dd/MM/yyyy");
        organizer  = tripsService.createUser(new User("keke.kokelenberg@student.kdg.be", "Keke"));
        trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        tripsService.publishTrip(trip, organizer);
    }

    @Test
    public void successfulSubscribe() throws TripsException, ParseException
    {
        User subscriber = tripsService.createUser(new User("eenGebruiker@kdg.be","twéè"));
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

    @Test(expected = TransactionSystemException.class)
    public void failedSubscribeInvalidPrivacyPublic() throws TripsException
    {
        User user = new User("jeremy@msn.be","zwaard");
        User subscriber = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("tijdloze trip", "aa", TripPrivacy.PUBLIC, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip,subscriber);
    }

    @Test(expected = ConstraintViolationException.class)
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
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, subscriber);
        tripsService.disenroll(trip, subscriber);
       // assertTrue(tripsService.findEnrollmentsByUser(subscriber).isEmpty());
    }

    @Test
    public void successfulDisenrollPrivateTrip() throws TripsException, MessagingException {
        User invitee = tripsService.createUser(new User("unexisting3@msn.com","gans"));
        Trip trip = tripsService.createTimelessTrip("zalt gaan ja", "ddtt", TripPrivacy.PRIVATE, organizer);
        tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        tripsService.disenroll(trip, invitee);
        assertEquals(Answer.DECLINED,tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
    }

    @Test(expected = TripsException.class)
    public void failedDisenrollNotEnrolled() throws TripsException
    {
        User subscriber = tripsService.createUser(new User("clarkson@msn.com","gans"));
        Trip trip = tripsService.createTimelessTrip("zalt gaan ja", "dimitri de tremmerie", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(trip, organizer);
        tripsService.disenroll(trip, subscriber);
    }

    @Test(expected = TripsException.class)
    public void failedDisenrollIsOrganizer() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("zalt gaan ja", "dimitri de tremmerie", TripPrivacy.PROTECTED, organizer);
        tripsService.disenroll(trip, organizer);
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
        Trip trip = tripsService.createTimeBoundTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, subscriber);
        assertEquals(2, tripsService.findEnrollmentsByTrip(trip).size());
    }

    @Test
    public void successfulInvite() throws TripsException, MessagingException {
        User user = new User("unexisting1@student.kdg.be","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        tripsService.invite(trip, organizer, invitee);
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

    @Test(expected = TripsException.class)
    public void failedUninviteIsOrganizer() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("Tenniswedstrijd", "tennis is geen ping pong", TripPrivacy.PRIVATE, organizer);
        tripsService.uninvite(trip, organizer, organizer);
    }

    @Test
    public void succesfulAcceptInvitation() throws TripsException, MessagingException {
        User user = new User("unexisting9@hotmail.com","pass");
        User invitee = tripsService.createUser(user);
        Trip trip = tripsService.createTimelessTrip("Spartacus run", "Lopen door de modder!", TripPrivacy.PRIVATE, organizer);
        Invitation invitation = tripsService.invite(trip, organizer, invitee);
        tripsService.acceptInvitation(trip, invitee);
        assertEquals(Answer.ACCEPTED, tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
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
        assertEquals(Answer.DECLINED, tripsService.findInvitationsByUser(invitee).get(FIRST_ELEMENT).getAnswer());
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
    public void rfailedDeclineInvitationAlreadDeclined() throws TripsException, MessagingException {
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
        Location loc1 = tripsService.addLocationToTrip(organizer, trip, 12.00, 160.00, null, null, null, null, null, "Loc1", "Location1");
        Location loc2 = tripsService.addLocationToTrip(organizer, trip, 12.00, 160.00, null, null, null, null, null, "Loc2","Location2");
        tripsService.publishTrip(trip,organizer);
        tripsService.subscribe(trip, user);
        tripsService.setLastLocationVisited(trip, user, loc1);
    }

    @Test
    public void successfulAddRequisiteToEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PROTECTED, organizer);
        User user = tripsService.createUser(new User("kiki@hotmail.com", "pass"));
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.addRequisiteToEnrollment("liters bier", 10, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("liters bier", 5, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("vrienden", 5, trip, user, organizer);
        Enrollment enrollment = tripsService.findEnrollmentsByUser(user).get(FIRST_ELEMENT);
        assertTrue(enrollment.getRequisites().containsValue(15));
    }

    @Test
    public void successfulRemoveRequisiteFromEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PROTECTED, organizer);
        User user = tripsService.createUser(new User("koko@hotmail.com", "pass"));
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.addRequisiteToEnrollment("liters bier", 10, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("liters bier", 5, trip, user, organizer);
        tripsService.addRequisiteToEnrollment("vrienden", 5, trip, user, organizer);
        tripsService.removeRequisiteFromEnrollment("liters bier", 12, trip, user, organizer);
        tripsService.removeRequisiteFromEnrollment("vrienden", 6, trip, user, organizer);
        Enrollment enrollment = tripsService.findEnrollmentsByUser(user).get(FIRST_ELEMENT);
        assertEquals(1, enrollment.getRequisites().size());
    }

    @Test
    public void successfulAddCostToEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with costs", "trip with costs", TripPrivacy.PROTECTED, organizer);
        User user = tripsService.createUser(new User("kuku@hotmail.com", "pass"));
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.addCostToEnrollment("bier", 10.0, trip, user);
        tripsService.addCostToEnrollment("bier", 5.0, trip, user);
        tripsService.addCostToEnrollment("meisjes van plezier", 50.0, trip, user);
        Enrollment enrollment = tripsService.findEnrollmentsByUser(user).get(FIRST_ELEMENT);
        assertTrue(enrollment.getCosts().containsValue(15.0));
    }

    @Test
    public void successfulRemoveCostFromEnrollment() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PROTECTED, organizer);
        User user = tripsService.createUser(new User("kk@hotmail.com", "pass"));
        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.addCostToEnrollment("bier", 10.0, trip, user);
        tripsService.addCostToEnrollment("bier", 5.0, trip, user);
        tripsService.addCostToEnrollment("meisjes van plezier", 50.0, trip, user);
        tripsService.removeCostFromEnrollment("bier", 12.0, trip, user);
        tripsService.removeCostFromEnrollment("meisjes van plezier", 50.0, trip, user);
        Enrollment enrollment = tripsService.findEnrollmentsByUser(user).get(FIRST_ELEMENT);
        assertEquals(1, enrollment.getCosts().size());
    }

    @Test
    public void successfulCheckAnswerFromQuestionCorrectAnswer() throws TripsException {
        User organizer = tripsService.createUser(new User("ginoendidier@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        //organizer is automatically enrolled
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(organizer, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, FIRST_ELEMENT, null);
        Location location = tripsService.findTripById(createdTrip.getId(), organizer).getLocations().get(FIRST_ELEMENT);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.setLastLocationVisited(createdTrip, organizer, location);
        Question question = location.getQuestion();
        tripsService.checkAnswerFromQuestion(question,FIRST_ELEMENT,organizer);
        assertEquals(1,tripsService.findEnrollmentsByUser(organizer).get(FIRST_ELEMENT).getScore());
    }

    @Test
    public void successfulCheckAnswerFromQuestionWrongAnswer() throws TripsException {
        User organizer = tripsService.createUser(new User("arrividrecthans@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who were I yesterday?", possibleAnswers, FIRST_ELEMENT, null);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.setLastLocationVisited(createdTrip, organizer, location);
        Question question = createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion();
        tripsService.checkAnswerFromQuestion(question,SECOND_ELEMENT,organizer);
        assertEquals(0,tripsService.findEnrollmentsByUser(organizer).get(FIRST_ELEMENT).getScore());
    }

    @Test(expected = TripsException.class)
    public void failedCheckAnswerFromQuestionTwiceAnswered() throws TripsException {
        User organizer = tripsService.createUser(new User("ditisdemooistedans@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who will I be tomorrow?", possibleAnswers, FIRST_ELEMENT, null);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.setLastLocationVisited(createdTrip, organizer, location);
        Question question = createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion();
        tripsService.checkAnswerFromQuestion(question,FIRST_ELEMENT,organizer);
        tripsService.checkAnswerFromQuestion(question,SECOND_ELEMENT,organizer);
    }

    @Test(expected = TripsException.class)
    public void failedCheckAnswerFromQuestionNotStartedYet() throws TripsException {
        User organizer = tripsService.createUser(new User("ditisdemooistedans@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(organizer, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who will I be tomorrow?", possibleAnswers, FIRST_ELEMENT, null);
        Question question = createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion();
        tripsService.checkAnswerFromQuestion(question,FIRST_ELEMENT,organizer);
    }

    @Test(expected = TripsException.class)
    public void failedCheckAnswerFromQuestionNotReachedLocation() throws TripsException {
        User organizer = tripsService.createUser(new User("ditisdemooistedans@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location loc1 = tripsService.addLocationToTrip(organizer, createdTrip, 23.12131, 11.12131, "Groenplaats", null, "Antwerp", "2000", "Belgium", "Titel", "Plein");
        Location loc2 = tripsService.addLocationToTrip(organizer, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who will I be tomorrow?", possibleAnswers, FIRST_ELEMENT, null);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.setLastLocationVisited(createdTrip, organizer, loc1);
        Question question = createdTrip.getLocations().get(SECOND_ELEMENT).getQuestion();
        tripsService.checkAnswerFromQuestion(question,FIRST_ELEMENT,organizer);
    }

    @Test
    public void successfulStartTrip() throws TripsException {
        User organizer = tripsService.createUser(new User("marielouisegaatopenneer@msn.com","herr"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip", "trip ", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        tripsService.startTrip(createdTrip, organizer);
        assertEquals(Status.BUSY, tripsService.findEnrollmentsByUser(organizer).get(FIRST_ELEMENT).getStatus());
    }

    @Test(expected = TripsException.class)
    public void failedStartTripAlreadyStarted() throws TripsException {
        User organizer = tripsService.createUser(new User("zeiljevoorheteerst@msn.com","herr"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip", "trip ", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.startTrip(createdTrip, organizer);
    }

    @Test
    public void successfulStopTrip() throws TripsException {
        User organizer = tripsService.createUser(new User("danslajeeenflater@msn.com","herr"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip", "trip ", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.stopTrip(createdTrip, organizer);
        assertEquals(Status.FINISHED, tripsService.findEnrollmentsByUser(organizer).get(FIRST_ELEMENT).getStatus());
    }

    @Test(expected = TripsException.class)
    public void failedStopTripAlreadyStopped() throws TripsException {
        User organizer = tripsService.createUser(new User("danslajeeenflater@msn.com","herr"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip", "trip ", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        tripsService.startTrip(createdTrip, organizer);
        tripsService.stopTrip(createdTrip, organizer);
        tripsService.stopTrip(createdTrip, organizer);
    }

    @Test(expected = TripsException.class)
    public void failedStopTripNotStarted() throws TripsException {
        User organizer = tripsService.createUser(new User("danslajeeenflater@msn.com","herr"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip", "trip ", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        tripsService.stopTrip(createdTrip, organizer);
    }
}
