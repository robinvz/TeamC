package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
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
import org.springframework.transaction.TransactionSystemException;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestTrip {
    private final int FIRST_ELEMENT = 0;
    private final int SECOND_ELEMENT = 0;

    private static TripsService tripsService;
    private static DateFormat df;

    private static User organizer;
    private static User invitee;
    private static User user;
    private static User guest;

    @BeforeClass
    public static void createTripManager() throws TripsException
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
        df = new SimpleDateFormat("dd/MM/yyyy");

        organizer = tripsService.createUser(new User("Prospere@kdg.be", "Albert"));
        invitee = tripsService.createUser(new User("Odylon@kdg.be", "Filip"));
        user = tripsService.createUser(new User("Theofiel@student.kdg.be", "Leopold"));
        guest = tripsService.createUser(new User("Jef@student.kdg.be", "Boer"));
    }

    @Test
    public void successfulCreateTimelessTripPublic() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Stadswandeling", "Wandeling in 't Stad", TripPrivacy.PUBLIC, organizer);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimelessTripProtected() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Stadswandeling", "Wandeling in 't Stad", TripPrivacy.PROTECTED, organizer);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimelessTripPrivate() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Stadswandeling", "Wandeling in 't Stad", TripPrivacy.PRIVATE, organizer);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimelessTripInvalidTitle() throws TripsException
    {
        tripsService.createTimelessTrip("", "Wandeling in 't Stad", TripPrivacy.PUBLIC, organizer);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimelessTripInvalidDescription() throws TripsException
    {
        tripsService.createTimelessTrip("Stadswandeling", "", TripPrivacy.PUBLIC, organizer);
    }

    @Test(expected = TripsException.class)
    public void failedCreateTimelessTripInvalidOrganizer() throws TripsException
    {
        tripsService.createTimelessTrip("Stadswandeling", "Wandeling in 't Stad", TripPrivacy.PUBLIC, new User("hacker@gmail.com", "hacked"));
    }

    @Test
    public void successfulCreateTimeBoundTripPublic() throws TripsException, ParseException
    {
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimeBoundTripProtected() throws TripsException, ParseException {
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimeBoundTripPrivate() throws TripsException, ParseException {
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PRIVATE, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"));
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }


    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimeBoundTripInvalidTitle() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, user, df.parse("14/12/2014"), df.parse("15/12/2014"));
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimeBoundTripInvalidDescription() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "", TripPrivacy.PUBLIC, user, df.parse("14/12/2014"), df.parse("15/12/2014"));
    }

    @Test(expected = TripsException.class)
    public void failedCreateTimeBoundTripInvalidOrganizer() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, new User("hacker@gmail.com", "hacked"), df.parse("14/12/2014"), df.parse("15/12/2014"));
    }

    @Test(expected = TripsException.class)
    public void failedCreateTimeBoundTripInvalidDatesEndDateAfterStartDate() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, user, df.parse("14/05/2014"), df.parse("15/04/2014"));
    }

    @Test(expected = ParseException.class)
    public void failedCreateTimeBoundTripInvalidDatesEmptyDate() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, user, df.parse(""), df.parse("15/04/2014"));
    }

    @Test
    public void successfulGuestFindAllTrips() throws TripsException
    {
        boolean check = true;
        List<Trip> trips;
        Trip trip1 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PUBLIC, user);
        Trip trip2 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PROTECTED, user);
        Trip trip3 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PRIVATE, user);
        tripsService.publishTrip(trip1,user);
        tripsService.publishTrip(trip2,user);
        trips = tripsService.findAllNonPrivateTrips(null);
        for(Trip trip : trips)
        {
            if(trip.getPrivacy() == TripPrivacy.PRIVATE)
            {
                check = false;
            }
        }
        assertTrue(check);
    }

    @Test
    public void successfulUserFindAllTrips() throws TripsException
    {
        boolean check = true;
        List<Trip> trips;
        Trip trip1 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PUBLIC, user);
        Trip trip2 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PROTECTED, user);
        Trip trip3 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PRIVATE, user);
        tripsService.publishTrip(trip1,user);
        tripsService.publishTrip(trip2,user);
        User user1 = tripsService.createUser(new User("test@gmail.com","kokelengerb"));
        trips = tripsService.findAllNonPrivateTrips(user1);
        for(Trip trip : trips)
        {
            if(trip.getPrivacy() == TripPrivacy.PRIVATE)
            {
                check = false;
            }
        }
        assertTrue(check);
    }

    @Test
    public void successfulFindTripByKeywordInTitle() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Parkwandeling", "Wandeling in park", TripPrivacy.PROTECTED, user);
        tripsService.publishTrip(createdTrip, user);
        Trip foundTrip = (Trip) tripsService.findNonPrivateTripsByKeyword("Parkwandeling", user).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulFindTripByKeywordInDescription() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("GrasvlakteWandeling", "Wandeling in grasvlakte", TripPrivacy.PROTECTED, user);
        tripsService.publishTrip(createdTrip, user);
        Trip foundTrip = (Trip) tripsService.findNonPrivateTripsByKeyword("wandeling in grasvlakte", user).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }


    @Test
    public void successfulFindTripByKeywordInLabel() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Waterwandeling", "Wandeling in water", TripPrivacy.PROTECTED, user);
        tripsService.addLabelToTrip(createdTrip, user, "WandelingWATER");
        tripsService.publishTrip(createdTrip, user);
        Trip foundTrip = (Trip) tripsService.findNonPrivateTripsByKeyword("ANdelingWAT", user).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulFindTripById() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Boswandeling 2", "Wandeling in bos", TripPrivacy.PROTECTED, user);
        User user1 = tripsService.createUser(new User("lolzor@gmail.com", "lollol"));
        Trip foundTrip = (Trip) tripsService.findTripById(createdTrip.getId(), user1);
        assertEquals(createdTrip, foundTrip);
    }

    @Test(expected = TripsException.class)
    public void failedFindTripById() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Boswandeling 3", "Wandeling in bos", TripPrivacy.PRIVATE, user);
        User user1 = tripsService.createUser(new User("lalzar@gmail.com", "lallal"));
        Trip foundTrip = (Trip) tripsService.findTripById(createdTrip.getId(), user1);
    }

    @Test
    public void successfulFindPrivateTrips() throws TripsException, MessagingException {
        User organizer = tripsService.createUser(new User("bobby.lobby@hotmail.com", "xinus"));
        Trip trip = tripsService.createTimelessTrip("Trip", "TripDescription", TripPrivacy.PRIVATE, organizer);
        User user = tripsService.createUser(new User("gekke.trekke@hotmail.com","linus"));
        tripsService.invite(trip, organizer, user);
        assertEquals(1, tripsService.findPrivateTrips(user).size());
    }

    @Test
    public void failedFindTripByKeyword() throws TripsException {
        assertEquals(0,tripsService.findNonPrivateTripsByKeyword("Nachtdropping", user).size());
    }

    @Test
    public void successfulFindTripsByOrganizer1() throws TripsException {
        User organizer1 = tripsService.createUser(new User("gerard.depardieu@hotmail.com","spint"));
        Trip trip1 = tripsService.createTimelessTrip("Trip1","TripDescription",TripPrivacy.PUBLIC,organizer1);
        Trip trip2 = tripsService.createTimelessTrip("Trip2", "TripDescription", TripPrivacy.PRIVATE, organizer1);
        User organizer2 = tripsService.createUser(new User("steven.spielberg@msn.com","oscar"));
        Trip trip3 = tripsService.createTimelessTrip("Trip3","TripDescription",TripPrivacy.PUBLIC,organizer2);
        assertEquals(2,tripsService.findTripsByOrganizer(organizer1).size());
    }

    @Test
    public void successfulFindTripsByOrganizer2() throws TripsException {
        User organizer = tripsService.createUser(new User("femke@hotmail.com", "spint"));
        assertTrue(tripsService.findTripsByOrganizer(organizer).isEmpty());
    }

    @Test
    public void successfulEditTripDetails() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1","TripDescription",TripPrivacy.PUBLIC,user);
        tripsService.editTripDetails(trip, "", "TripDescriptionEdited", user);
        assertTrue(trip.getDescription().equals("TripDescriptionEdited"));
    }

    @Test
    public void successfulEditTripLocation() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1","TripDescription",TripPrivacy.PUBLIC,user);
        Location location1 = tripsService.addLocationToTrip(user, trip, 10.12131, 10.12131, "Nationalestraat", "1", "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.editTripLocationDetails(user, trip, location1, "", "2", "", "", "", "", "");
        assertTrue(tripsService.findTripById(trip.getId(), user).getLocations().get(FIRST_ELEMENT).getAddress().getHouseNr().equals("2"));
    }

    @Test
    public void successfulAddLabelsToTrip() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("The Spartacus", "N/A1", TripPrivacy.PUBLIC, user);
        tripsService.addLabelToTrip(trip,user,"Modder");
        tripsService.addLabelToTrip(trip,user,"Uitdaging");
        assertEquals(2, trip.getLabels().size());
    }

    @Test(expected = TripsException.class)
    public void failedAddLabelNoPermission() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("The Spartacus", "N/A1", TripPrivacy.PUBLIC, user);
        User otherUser = tripsService.createUser(new User("johny@gmail.com", "terry"));
        tripsService.addLabelToTrip(trip, otherUser, "Modder");
    }

    @Test
    public void successfulPublishTrip() throws TripsException, ParseException {
        Trip trip = tripsService.createTimeBoundTrip("carnaval", "carnaval met stoet", TripPrivacy.PROTECTED, user, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, user);
        assertTrue(trip.isPublished());
    }

    @Test(expected = TripsException.class)
    public void failedPublishTripTwice() throws TripsException, ParseException
    {
        Trip trip = tripsService.createTimeBoundTrip("carnaval", "carnaval met stoet", TripPrivacy.PUBLIC, user, df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, user);
        tripsService.publishTrip(trip, user);
    }

    @Test(expected = TripsException.class)
    public void failedPublishTripNoPermission() throws TripsException, ParseException
    {
        Trip trip = tripsService.createTimeBoundTrip("carnaval","carnaval met stoet",TripPrivacy.PUBLIC,user,df.parse("14/12/2014"), df.parse("15/12/2014"));
        tripsService.publishTrip(trip, user);
        User otherUser = tripsService.createUser(new User("john@gmail.com", "terry"));
        tripsService.publishTrip(trip, otherUser);
    }

    @Test
    public void successfulAddLocationWithoutQuestions() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("locationTripa", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addLocationToTrip(user, createdTrip, 10.12121, 10.12123, "Groenplaats", "53", null, "2000", null, "Titel", "Groenplaats");
        assertEquals(2, tripsService.findTripById(createdTrip.getId(), user).getLocations().size());
    }

    @Test
    public void successfulAddLocationWithQuestions() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("locationTripa", "trip with locations", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<String>();
        possibleAnswers.add("yes");
        possibleAnswers.add("no");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Schijnt de zon?", possibleAnswers, FIRST_ELEMENT);
        tripsService.addLocationToTrip(user, createdTrip, 10.12121, 10.12123, "Groenplaats", "53", null, "2000", null, "Titel", "Groenplaats", "Schijnt de zon nog steeds?",possibleAnswers, SECOND_ELEMENT);
        assertEquals(2, tripsService.findTripById(createdTrip.getId(), user).getLocations().size());
    }

    @Test(expected = TripsException.class)
    public void failedAddLocationNoPermission() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("locationTrip", "trip with locations", TripPrivacy.PUBLIC, user);
        User otherUser = tripsService.createUser(new User("jan.discort@hotmail.com", "janneman"));
        tripsService.addLocationToTrip(otherUser, trip, 131.131, 23.123, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedAddLocationInvalidLatitude() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("trip", "trip with location", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 1930.02, 23.123, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
    }

    @Test(expected = TripsException.class)
    public void failedAddLocationInvalidAnswer() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, 4);
    }

    @Test
    public void successfulAddDatesToTimeBoundTrip() throws ParseException, TripsException
    {
        Trip trip = tripsService.createTimeBoundTrip("trip met extra dates", "extra dates", TripPrivacy.PROTECTED, user, df.parse("01/01/2014"), df.parse("01/02/2014"));
        tripsService.publishTrip(trip, user);
        tripsService.addDateToTimeBoundTrip(df.parse("01/03/2014"), df.parse("01/04/2014"), trip, user);
        assertEquals(2, ((TimeBoundTrip) trip).getDates().size());
    }

    @Test(expected = TripsException.class)
    public void failedAddDatesToTimeBoundTripOccupiedDate() throws ParseException, TripsException
    {
        Trip trip = tripsService.createTimeBoundTrip("trip met extra dates", "extra dates", TripPrivacy.PROTECTED, user, df.parse("01/01/2014"), df.parse("01/02/2014"));
        tripsService.publishTrip(trip, user);
        tripsService.addDateToTimeBoundTrip(df.parse("15/01/2014"), df.parse("01/04/2014"), trip, user);
    }

    @Test
    public void successfulAddRequisiteToTrip() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PUBLIC, user);
        tripsService.addRequisiteToTrip("liters bier", 10, trip, user);
        tripsService.addRequisiteToTrip("liters bier", 5, trip, user);
        tripsService.addRequisiteToTrip("vrienden", 5, trip, user);
        assertTrue(trip.getRequisites().containsValue(15));
    }

    @Test
    public void successfulRemoveRequisiteFromTrip() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with requisites", "trip with requisites", TripPrivacy.PUBLIC, user);
        tripsService.addRequisiteToTrip("liters bier", 10, trip, user);
        tripsService.addRequisiteToTrip("liters bier", 5, trip, user);
        tripsService.addRequisiteToTrip("vrienden", 5, trip, user);
        tripsService.removeRequisiteFromTrip("liters bier", 12, trip, user);
        tripsService.removeRequisiteFromTrip("vrienden", 6, trip, user);
        assertEquals(1, trip.getRequisites().size());
    }

    @Test
    public void successfulGuessAnswer() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, FIRST_ELEMENT);
        assertTrue(createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion().checkAnswer(FIRST_ELEMENT));
    }

    @Test
    public void failGuessAnswer() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, 0);
        assertFalse(createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion().checkAnswer(1));
    }

    @Test
    public void successfulSwitchLocations1() throws TripsException
    {
        boolean check = true;
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 12.00, 13.00, null, null, null, null, null, "Location1", "Aangename location1");
        tripsService.addLocationToTrip(user, trip, 11.00, 13.00, null, null, null, null, null, "Location2", "Aangename location2");
        tripsService.addLocationToTrip(user, trip, 14.00, 13.00, null, null, null, null, null, "Location3", "Aangename location3");
        tripsService.addLocationToTrip(user, trip, 15.00, 13.00, null, null, null, null, null, "Location4", "Aangename location4");
        tripsService.addLocationToTrip(user, trip, 16.00, 13.00, null, null, null, null, null, "Location5", "Aangename location5");
        List<Location> oldLocations = tripsService.findTripById(trip.getId(), user).getLocations();
        tripsService.switchLocationSequence(trip, user, 1, 4);
        List<Location> newLocations = tripsService.findTripById(trip.getId(), user).getLocations();

        boolean correct = true;
        if(!oldLocations.get(0).equals(newLocations.get(0))) correct = false;
        if(!oldLocations.get(1).equals(newLocations.get(4))) correct = false;
        if(!oldLocations.get(2).equals(newLocations.get(1))) correct = false;
        if(!oldLocations.get(3).equals(newLocations.get(2))) correct = false;
        if(!oldLocations.get(4).equals(newLocations.get(3))) correct = false;

        assertTrue(correct);
    }

    @Test
    public void successfulSwitchLocations2() throws TripsException
    {
        boolean check = true;
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 12.00, 13.00, null, null, null, null, null, "Location1", "Aangename location1");
        tripsService.addLocationToTrip(user, trip, 11.00, 13.00, null, null, null, null, null, "Location2", "Aangename location2");
        tripsService.addLocationToTrip(user, trip, 14.00, 13.00, null, null, null, null, null, "Location3", "Aangename location3");
        tripsService.addLocationToTrip(user, trip, 15.00, 13.00, null, null, null, null, null, "Location4", "Aangename location4");
        tripsService.addLocationToTrip(user, trip, 16.00, 13.00, null, null, null, null, null, "Location5", "Aangename location5");
        List<Location> oldLocations = tripsService.findTripById(trip.getId(), user).getLocations();
        tripsService.switchLocationSequence(trip, user, 3, 1);
        List<Location> newLocations = tripsService.findTripById(trip.getId(), user).getLocations();

        boolean correct = true;
        if(!oldLocations.get(0).equals(newLocations.get(0))) correct = false;
        if(!oldLocations.get(1).equals(newLocations.get(2))) correct = false;
        if(!oldLocations.get(2).equals(newLocations.get(3))) correct = false;
        if(!oldLocations.get(3).equals(newLocations.get(1))) correct = false;
        if(!oldLocations.get(4).equals(newLocations.get(4))) correct = false;

        assertTrue(correct);
    }

    @Test
    public void successfulSwitchLocations3() throws TripsException
    {
        boolean check = true;
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 12.00, 13.00, null, null, null, null, null, "Location1", "Aangename location1");
        tripsService.addLocationToTrip(user, trip, 11.00, 13.00, null, null, null, null, null, "Location2", "Aangename location2");
        tripsService.addLocationToTrip(user, trip, 14.00, 13.00, null, null, null, null, null, "Location3", "Aangename location3");
        tripsService.addLocationToTrip(user, trip, 15.00, 13.00, null, null, null, null, null, "Location4", "Aangename location4");
        tripsService.addLocationToTrip(user, trip, 16.00, 13.00, null, null, null, null, null, "Location5", "Aangename location5");
        List<Location> oldLocations = tripsService.findTripById(trip.getId(), user).getLocations();
        tripsService.switchLocationSequence(trip, user, 3, 3);
        List<Location> newLocations = tripsService.findTripById(trip.getId(), user).getLocations();

        boolean correct = true;
        if(!oldLocations.get(0).equals(newLocations.get(0))) correct = false;
        if(!oldLocations.get(1).equals(newLocations.get(1))) correct = false;
        if(!oldLocations.get(2).equals(newLocations.get(2))) correct = false;
        if(!oldLocations.get(3).equals(newLocations.get(3))) correct = false;
        if(!oldLocations.get(4).equals(newLocations.get(4))) correct = false;

        assertTrue(correct);
    }

    @Test
    public void successfulSwitchLocations4() throws TripsException
    {
        boolean check = true;
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 12.00, 13.00, null, null, null, null, null, "Location1", "Aangename location1");
        tripsService.addLocationToTrip(user, trip, 11.00, 13.00, null, null, null, null, null, "Location2", "Aangename location2");
        tripsService.addLocationToTrip(user, trip, 14.00, 13.00, null, null, null, null, null, "Location3", "Aangename location3");
        tripsService.addLocationToTrip(user, trip, 15.00, 13.00, null, null, null, null, null, "Location4", "Aangename location4");
        tripsService.addLocationToTrip(user, trip, 16.00, 13.00, null, null, null, null, null, "Location5", "Aangename location5");
        List<Location> oldLocations = tripsService.findTripById(trip.getId(), user).getLocations();
        tripsService.switchLocationSequence(trip, user, 2, 0);
        List<Location> newLocations = tripsService.findTripById(trip.getId(), user).getLocations();

        boolean correct = true;
        if(!oldLocations.get(0).equals(newLocations.get(1))) correct = false;
        if(!oldLocations.get(1).equals(newLocations.get(2))) correct = false;
        if(!oldLocations.get(2).equals(newLocations.get(0))) correct = false;
        if(!oldLocations.get(3).equals(newLocations.get(3))) correct = false;
        if(!oldLocations.get(4).equals(newLocations.get(4))) correct = false;

        assertTrue(correct);
    }

    @Test
    public void successfulSwitchLocations5() throws TripsException
    {
        boolean check = true;
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, trip, 12.00, 13.00, null, null, null, null, null, "Location1", "Aangename location1");
        tripsService.addLocationToTrip(user, trip, 11.00, 13.00, null, null, null, null, null, "Location2", "Aangename location2");
        tripsService.addLocationToTrip(user, trip, 14.00, 13.00, null, null, null, null, null, "Location3", "Aangename location3");
        tripsService.addLocationToTrip(user, trip, 15.00, 13.00, null, null, null, null, null, "Location4", "Aangename location4");
        tripsService.addLocationToTrip(user, trip, 16.00, 13.00, null, null, null, null, null, "Location5", "Aangename location5");
        List<Location> oldLocations = tripsService.findTripById(trip.getId(), user).getLocations();
        tripsService.switchLocationSequence(trip, user, 3, 4);
        List<Location> newLocations = tripsService.findTripById(trip.getId(), user).getLocations();

        boolean correct = true;
        if(!oldLocations.get(0).equals(newLocations.get(0))) correct = false;
        if(!oldLocations.get(1).equals(newLocations.get(1))) correct = false;
        if(!oldLocations.get(2).equals(newLocations.get(2))) correct = false;
        if(!oldLocations.get(3).equals(newLocations.get(4))) correct = false;
        if(!oldLocations.get(4).equals(newLocations.get(3))) correct = false;

        assertTrue(correct);
    }

    @Test
    public void successfulDeleteTrip() throws TripsException, MessagingException, ParseException {
        User organizer = tripsService.createUser(new User("tripsteamc@gmail.com", "SDProject"));
        Trip createdTrip = tripsService.createTimeBoundTrip("Deer hunting", "I will be deleted", TripPrivacy.PROTECTED, organizer, df.parse("20/05/2013"), df.parse("21/05/2013"));
        tripsService.publishTrip(createdTrip, organizer);
        // Second user could be added in order to check if both receive notification mail
        // User user = tripsService.createUser("email2","x");
        // tripsService.enroll(createdTrip,user);
        tripsService.deleteTrip(createdTrip, organizer);
        assertEquals(0, tripsService.findNonPrivateTripsByKeyword("deletetrip", user).size());
    }

    @Test
    public void successfulDeleteLocation() throws TripsException {
        User organizer = tripsService.createUser(new User("keesflodder@hotmail.com","meloen"));
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, organizer);
        tripsService.addLocationToTrip(organizer, trip, 12.12, 13.00, null, null, null, null, null, "Location", "Delete please");
        tripsService.addLocationToTrip(organizer, trip, 13.00, 13.00, null, null, null, null, null, "Location", "Stay please");
        Location loc1 = tripsService.findTripById(trip.getId(), organizer).getLocations().get(FIRST_ELEMENT);
        tripsService.deleteLocation(trip, organizer, loc1);
        assertEquals(1,tripsService.findTripsByOrganizer(organizer).get(FIRST_ELEMENT).getLocations().size());
    }

    @Test
    public void successfulFindLocationById() throws TripsException {
        User organizer = tripsService.createUser(new User("retenflees@hotmail.com","viandel"));
        Trip trip = tripsService.createTimelessTrip("Trip with locations", "trip with locations", TripPrivacy.PUBLIC, organizer);
        Location loc1 = tripsService.addLocationToTrip(organizer, trip, 12.00, 13.00, null, null, null, null, null, "Location", "Aangename location1");
        assertNotNull(tripsService.findLocationById(1));
    }

    @Test
    public void successfulUpdateTripImage() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip", "trip", TripPrivacy.PUBLIC, organizer);
        File file = new File("src/test/resources/testimage.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripsService.addImageToTrip(trip, organizer, bFile);
        assertNotNull(tripsService.findTripById(trip.getId(), organizer).getImage());
    }
}

