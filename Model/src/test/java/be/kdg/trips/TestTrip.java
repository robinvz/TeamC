package be.kdg.trips;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.*;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import be.kdg.trips.utility.Fraction;
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
import java.util.*;

import static org.junit.Assert.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestTrip {
    private final int FIRST_ELEMENT = 0;
    private final int SECOND_ELEMENT = 1;

    private static TripsService tripsService;
    private static DateFormat df;

    private static User organizer;
    private static User invitee;
    private static User user;

    @BeforeClass
    public static void createTripManager() throws TripsException
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
        df = new SimpleDateFormat("dd/MM/yyyy");

        organizer = tripsService.createUser(new User("Prospere@kdg.be", "Albert"));
        invitee = tripsService.createUser(new User("Odylon@kdg.be", "Filip"));
        user = tripsService.createUser(new User("Theofiel@student.kdg.be", "Leopold"));
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
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimeBoundTripProtected() throws TripsException, ParseException {
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PROTECTED, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimeBoundTripPrivate() throws TripsException, ParseException {
        Trip createdTrip = tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PRIVATE, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), organizer);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulCreateTimeBoundWeeklyRepeatableTrip() throws ParseException, TripsException {
        User organizer = tripsService.createUser(new User("JeanBaptise@toko.com", "Jean"));
        tripsService.createTimeBoundTrip("Herhaalbaar iedere week", "repeatable each week", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), Repeatable.WEEKLY, 2);
        List<Trip> trips = tripsService.findTripsByOrganizer(organizer);
        assertEquals(3, trips.size());
    }

    @Test
    public void successfulCreateTimeBoundMonthlyRepeatableTrip() throws ParseException, TripsException {
        User organizer = tripsService.createUser(new User("JefTokomoto@toko.com","Jean"));
        tripsService.createTimeBoundTrip("Herhaalbaar iedere maand", "repeatable each month", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), Repeatable.MONTHLY, 4);
        List<Trip> trips = tripsService.findTripsByOrganizer(organizer);
        assertEquals(5, trips.size());
    }

    @Test
    public void successfulCreateTimeBoundYearlyRepeatableTrip() throws ParseException, TripsException {
        User organizer = tripsService.createUser(new User("LouisPaul@toko.com","Jean"));
        tripsService.createTimeBoundTrip("Herhaalbaar ieder jaar", "repeatable each year", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), Repeatable.ANNUALLY, 1);
        List<Trip> trips = tripsService.findTripsByOrganizer(organizer);
        assertEquals(2, trips.size());
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimeBoundTripInvalidTitle() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimeBoundTripInvalidDescription() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "", TripPrivacy.PUBLIC, organizer, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
    }

    @Test(expected = TripsException.class)
    public void failedCreateTimeBoundTripInvalidOrganizer() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, new User("hacker@gmail.com", "hacked"), df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
    }

    @Test(expected = TripsException.class)
    public void failedCreateTimeBoundTripInvalidDatesEndDateAfterStartDate() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse("14/05/2014"), df.parse("15/04/2014"), null, null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedCreateTimeBoundTripInvalidDatesStartDateNotInFuture() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse("14/05/2011"), df.parse("15/04/2014"), null, null);
    }

    @Test(expected = ParseException.class)
    public void failedCreateTimeBoundTripInvalidDatesEmptyDate() throws TripsException, ParseException
    {
        tripsService.createTimeBoundTrip("Langlauf", "Langlaufen in de Ardennen", TripPrivacy.PUBLIC, organizer, df.parse(""), df.parse("15/04/2014"), null, null);
    }

    @Test
    public void successfulGuestFindAllTrips() throws TripsException
    {
        boolean check = true;
        List<Trip> trips;
        Trip trip1 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PUBLIC, organizer);
        Trip trip2 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PROTECTED, organizer);
        Trip trip3 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip1,organizer);
        tripsService.publishTrip(trip2,organizer);
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
        Trip trip1 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PUBLIC, organizer);
        Trip trip2 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PROTECTED, organizer);
        Trip trip3 = tripsService.createTimelessTrip("Marathon des sables", "N/A1", TripPrivacy.PRIVATE, organizer);
        tripsService.publishTrip(trip1,organizer);
        tripsService.publishTrip(trip2,organizer);
        trips = tripsService.findAllNonPrivateTrips(user);
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
        Trip createdTrip = tripsService.createTimelessTrip("Parkwandeling", "Wandeling in park", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        Trip foundTrip = tripsService.findNonPrivateTripsByKeyword("Parkwandeling", organizer).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void successfulFindTripByKeywordInDescription() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("GrasvlakteWandeling", "Wandeling in grasvlakte", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(createdTrip, organizer);
        Trip foundTrip = tripsService.findNonPrivateTripsByKeyword("wandeling in grasvlakte", organizer).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }


    @Test
    public void successfulFindTripByKeywordInLabel() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Waterwandeling", "Wandeling in water", TripPrivacy.PROTECTED, organizer);
        tripsService.addLabelToTrip(createdTrip, organizer, "WandelingWATER");
        tripsService.publishTrip(createdTrip, organizer);
        Trip foundTrip = tripsService.findNonPrivateTripsByKeyword("ANdelingWAT", organizer).get(FIRST_ELEMENT);
        assertEquals(createdTrip, foundTrip);
    }

    @Test
    public void failedFindTripByKeywordUnexistingKeyword() throws TripsException
    {
        assertEquals(0, tripsService.findNonPrivateTripsByKeyword("Keke loopt binnenkort de spartacusrun!", organizer).size());
    }

    @Test
    public void successfulFindTripById() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Boswandeling 2", "Wandeling in bos", TripPrivacy.PROTECTED, organizer);
        Trip foundTrip = tripsService.findTripById(createdTrip.getId(), user);
        assertEquals(createdTrip, foundTrip);
    }

    @Test(expected = TripsException.class)
    public void failedFindTripByIdNoPermissionToViewPrivateTrip() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Boswandeling 3", "Wandeling in bos", TripPrivacy.PRIVATE, organizer);
        tripsService.findTripById(createdTrip.getId(), user);
    }

    @Test
    public void successfulFindPrivateTrips() throws TripsException, MessagingException {
        User organizer = tripsService.createUser(new User("bobby.lobby@hotmail.com", "xinus"));
        Trip trip = tripsService.createTimelessTrip("Trip", "TripDescription", TripPrivacy.PRIVATE, organizer);
        User user = tripsService.createUser(new User("gekke.trekke@hotmail.com", "linus"));
        tripsService.invite(trip, organizer, user);
        assertEquals(1, tripsService.findPrivateTrips(user).size());
    }

    @Test
    public void successfulFindTripsByOrganizer1() throws TripsException {
        User organizer1 = tripsService.createUser(new User("gerard.depardieu@hotmail.com", "spint"));
        Trip trip1 = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, organizer1);
        Trip trip2 = tripsService.createTimelessTrip("Trip2", "TripDescription", TripPrivacy.PRIVATE, organizer1);
        User organizer2 = tripsService.createUser(new User("steven.spielberg@msn.com", "oscar"));
        Trip trip3 = tripsService.createTimelessTrip("Trip3", "TripDescription", TripPrivacy.PUBLIC, organizer2);
        assertEquals(2, tripsService.findTripsByOrganizer(organizer1).size());
    }

    @Test
    public void successfulFindTripsByOrganizer2() throws TripsException {
        User organizer = tripsService.createUser(new User("femke@hotmail.com", "spint"));
        assertTrue(tripsService.findTripsByOrganizer(organizer).isEmpty());
    }

    @Test(expected = TripsException.class)
    public void failedFindTripsByOrganizerUnexistingUser() throws TripsException
    {
        tripsService.findTripsByOrganizer(new User("famke@hotmail.com", "spint"));
    }

    @Test
    public void successfulEditTripDetailsTitle() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "TripTitle", "", true, true, organizer);
        assertTrue(tripsService.findTripById(trip.getId(), organizer).getTitle().equals("TripTitle"));
    }

    @Test
    public void successfulEditTripDetailsDescription() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "", "TripDescriptionEdited", true, true, organizer);
        assertTrue(tripsService.findTripById(trip.getId(), organizer).getDescription().equals("TripDescriptionEdited"));
    }

    @Test
    public void successfulEditTripDetailsChatAllowed() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "", "", false, true, organizer);
        assertFalse(tripsService.findTripById(trip.getId(), organizer).isChatAllowed());
    }

    @Test
    public void successfulEditTripDetailsPositionVisible() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "", "", true, false, organizer);
        assertFalse(tripsService.findTripById(trip.getId(), organizer).isPositionVisible());
    }

    @Test(expected = TripsException.class)
    public void failedEditTripDetailsNoPermissionInvalidOrganizer() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip2", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "Trip2", "TripDescription", true, true, user);
    }

    @Test(expected = TripsException.class)
    public void failedEditTripDetailsNoPermissionInvalidTrip() throws TripsException
    {
        Trip trip = new TimelessTrip("Trip2", "TripDescription", TripPrivacy.PUBLIC, organizer);
        tripsService.editTripDetails(trip, "Trip2", "TripDescription", true, true, organizer);
    }

    @Test
    public void successfulEditTripLocation() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip1", "TripDescription", TripPrivacy.PUBLIC, user);
        Location location1 = tripsService.addLocationToTrip(user, trip, 10.12131, 10.12131, "Nationalestraat", "1", "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.editTripLocationDetails(user, trip, location1, "", "2", "", "", "", "", "");
        assertTrue(tripsService.findTripById(trip.getId(), user).getLocations().get(FIRST_ELEMENT).getAddress().getHouseNr().equals("2"));
    }

    @Test
    public void successfulAddLabelsToTrip() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("The Spartacus", "N/A1", TripPrivacy.PUBLIC, user);
        tripsService.addLabelToTrip(trip,user,"Modder");
        tripsService.addLabelToTrip(trip, user, "Uitdaging");
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
        Trip trip = tripsService.createTimeBoundTrip("carnaval", "carnaval met stoet", TripPrivacy.PROTECTED, user, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        tripsService.publishTrip(trip, user);
        assertTrue(trip.isPublished());
    }

    @Test(expected = TripsException.class)
    public void failedPublishTripTwice() throws TripsException, ParseException
    {
        Trip trip = tripsService.createTimeBoundTrip("carnaval", "carnaval met stoet", TripPrivacy.PUBLIC, user, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
        tripsService.publishTrip(trip, user);
        tripsService.publishTrip(trip, user);
    }

    @Test(expected = TripsException.class)
    public void failedPublishTripNoPermission() throws TripsException, ParseException
    {
        Trip trip = tripsService.createTimeBoundTrip("carnaval", "carnaval met stoet", TripPrivacy.PUBLIC, user, df.parse("14/12/2014"), df.parse("15/12/2014"), null, null);
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
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Schijnt de zon?", possibleAnswers, FIRST_ELEMENT, null);
        tripsService.addLocationToTrip(user, createdTrip, 10.12121, 10.12123, "Groenplaats", "53", null, "2000", null, "Titel", "Groenplaats", "Schijnt de zon nog steeds?", possibleAnswers, FIRST_ELEMENT, null);
        assertEquals(2, tripsService.findTripById(createdTrip.getId(), user).getLocations().size());
    }

    @Test
    public void successfulAddLocationWithQuestionAndImage() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("locationTripa", "trip with locations", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<String>();
        possibleAnswers.add("yes");
        possibleAnswers.add("no");
        File file = new File("src/test/resources/testimage.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Schijnt de zon?", possibleAnswers, FIRST_ELEMENT, bFile);
        assertNotNull(tripsService.findTripById(createdTrip.getId(), user).getLocations().get(FIRST_ELEMENT).getQuestion().getImage());
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
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, 4, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddLocationInvalidPossibleAnswers() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", new ArrayList(), 0, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddLocationInvalidAnswerIndex1() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, -2, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddLocationInvalidAnswerIndex2() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, 5, null);
    }

    @Test
    public void successfulAddDateToTimeBoundTrip() throws ParseException, TripsException
    {
        User organizer = tripsService.createUser(new User("GeoffreyWesle@toko.com", "Jean"));
        Trip trip = tripsService.createTimeBoundTrip("trip met extra dates spaghetti", "extra dates", TripPrivacy.PROTECTED, organizer, df.parse("01/01/2014"), df.parse("01/02/2014"), null, null);
        tripsService.addDateToTimeBoundTrip(df.parse("01/03/2014"), df.parse("01/04/2014"), trip, organizer);
        List<Trip> trips = tripsService.findTripsByOrganizer(organizer);
        assertEquals(2, trips.size());
    }

    @Test(expected = TripsException.class)
    public void failedAddDateToTimeBoundTripInvalidTripType() throws ParseException, TripsException
    {
        User organizer = tripsService.createUser(new User("GeoffWesle@toko.com", "Jean"));
        Trip trip = tripsService.createTimelessTrip("trip met extra dates macaroni", "extra dates", TripPrivacy.PROTECTED, organizer);
        tripsService.addDateToTimeBoundTrip(df.parse("01/03/2014"), df.parse("01/04/2014"), trip, organizer);
        List<Trip> trips = tripsService.findTripsByOrganizer(organizer);
        assertEquals(2, trips.size());
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

    @Test(expected = TripsException.class)
    public void failedAddRequisiteToTripAlreadyActive() throws TripsException, ParseException
    {
        Date inAMoment = new Date();
        inAMoment.setTime(inAMoment.getTime() + 50);
        Trip trip = tripsService.createTimeBoundTrip("Trip with requisites", "trip with requisites", TripPrivacy.PROTECTED, user, inAMoment, df.parse("01/01/2014"), null, null);
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
    public void successfulAddQuestionToLocation() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        String question = tripsService.findLocationById(location.getId()).getQuestion().getQuestion();
        assertEquals("Wie is hier den baas?", question);
    }

    @Test(expected = TripsException.class)
    public void failedAddQuestionToLocationOccupiedQuestion() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        tripsService.addQuestionToLocation(organizer, location, "failed question", new ArrayList<String>(), 0, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddQuestionToLocationInvalidPossibleAnswers() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "failed question", new ArrayList<String>(), 0, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddQuestionToLocationInvalidAnswerIndex1() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "failed question", possibleAnswers, -2, null);
    }

    @Test(expected = TripsException.class)
    public void failedAddQuestionToLocationInvalidAnswerIndex2() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "failed question", possibleAnswers, 5, null);
    }

    @Test
    public void successfulRemoveQuestionFromLocation() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with removed questions", "Trip with removed questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        location = tripsService.findTripById(trip.getId(),organizer).getLocations().get(FIRST_ELEMENT);
        tripsService.removeQuestionFromLocation(organizer, location);
        assertNull(tripsService.findLocationById(location.getId()).getQuestion());
    }

    @Test(expected = TripsException.class)
    public void failedRemoveQuestionFromLocation() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with removed questions", "Trip with removed questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        location = tripsService.findTripById(trip.getId(),organizer).getLocations().get(FIRST_ELEMENT);
        tripsService.removeQuestionFromLocation(organizer, location);
        tripsService.removeQuestionFromLocation(organizer, location);
    }

    @Test
    public void successfulEditQuestionTitle() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        location = tripsService.findTripById(trip.getId(),organizer).getLocations().get(FIRST_ELEMENT);
        Question question = tripsService.findLocationById(location.getId()).getQuestion();
        tripsService.editTripQuestionDetails(organizer, location, question, "Keke is den baas!", possibleAnswers, 0);
        String questionTitle = question.getQuestion();
        assertEquals("Keke is den baas!", questionTitle);
    }

    @Test
    public void successfulEditQuestionCorrectAnswerIndex() throws TripsException
    {
        Trip trip = tripsService.createTimelessTrip("Trip with added questions", "Trip with added questions", TripPrivacy.PROTECTED, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        Location location = tripsService.addLocationToTrip(organizer, trip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor");
        tripsService.addQuestionToLocation(organizer, location, "Wie is hier den baas?", possibleAnswers, 0, null);
        location = tripsService.findTripById(trip.getId(),organizer).getLocations().get(FIRST_ELEMENT);
        Question question = tripsService.findLocationById(location.getId()).getQuestion();
        tripsService.editTripQuestionDetails(organizer, location, question, "Wie is hier den baas?", possibleAnswers, 1);
        assertEquals(true, question.checkAnswer(1));
    }

    @Test
    public void successfulGuessAnswer() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, FIRST_ELEMENT, null);
        assertTrue(createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion().checkAnswer(FIRST_ELEMENT));
    }

    @Test
    public void failGuessAnswer() throws TripsException
    {
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PUBLIC, user);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(user, createdTrip, 10.12131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, FIRST_ELEMENT, null);
        assertFalse(createdTrip.getLocations().get(FIRST_ELEMENT).getQuestion().checkAnswer(1));
    }

    @Test
    public void successfulSwitchLocations1() throws TripsException
    {
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
        Trip createdTrip = tripsService.createTimeBoundTrip("Deer hunting", "I will be deleted", TripPrivacy.PROTECTED, organizer, df.parse("20/05/2013"), df.parse("21/05/2013"), null, null);
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

    @Test
    public void successfulChangeThemeOfTrip() throws TripsException {
        Trip trip = tripsService.createTimelessTrip("Stadswandeling", "Wandeling in 't Stad", TripPrivacy.PUBLIC, organizer);
        tripsService.changeThemeOfTrip(trip, "green");
        assertEquals("green", trip.getTheme());
    }

    @Test
    public void successfulGetQuestionsWithAnswerPercentage() throws TripsException
    {
        User organizer = tripsService.createUser(new User("taxileo@msn.com","fraulein"));
        Trip createdTrip = tripsService.createTimelessTrip("Trip with loc with ?", "trip with location and question", TripPrivacy.PROTECTED, organizer);
        //organizer is automatically enrolled
        tripsService.publishTrip(createdTrip, organizer);
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Gijs");
        possibleAnswers.add("Keke");
        tripsService.addLocationToTrip(organizer, createdTrip, 10.131, 10.12131, "Nationalestraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I?", possibleAnswers, FIRST_ELEMENT, null);
        Location location1 = tripsService.findTripById(createdTrip.getId(), organizer).getLocations().get(FIRST_ELEMENT);
        tripsService.addLocationToTrip(organizer, createdTrip, 10.1231, 10.131, "Nationalearrstraat", null, "Antwerp", "2000", "Belgium", "Titel", "Lange straat met tramspoor", "Who am I now?", possibleAnswers, FIRST_ELEMENT, null);
        Location location2 = tripsService.findTripById(createdTrip.getId(), organizer).getLocations().get(SECOND_ELEMENT);
        Question question1 = location1.getQuestion();
        Question question2 = location2.getQuestion();

        //organizer
        tripsService.startTrip(createdTrip, organizer);
        tripsService.setLastLocationVisited(createdTrip, organizer, location1);
        tripsService.checkAnswerFromQuestion(question1,FIRST_ELEMENT,organizer);

        //subscriber
        User user = tripsService.createUser(new User("capgem@hotmail.com","XDo)a"));
        tripsService.subscribe(createdTrip, user);
        tripsService.startTrip(createdTrip, user);
        tripsService.setLastLocationVisited(createdTrip, user, location1);
        tripsService.checkAnswerFromQuestion(question1,SECOND_ELEMENT,user);
        tripsService.setLastLocationVisited(createdTrip, user, location2);
        tripsService.checkAnswerFromQuestion(question2,FIRST_ELEMENT,user);

        Map<Question, Fraction> questions = tripsService.getQuestionsWithAnswerPercentage(createdTrip, organizer);
        Iterator it = questions.entrySet().iterator();
        boolean correct = true;
        int i = 0;
        while(it.hasNext())
        {

            switch(i)
            {
                case 0:if(((Fraction)it.next()).getPercentage() != 50) correct = false;break;
                case 1:if(((Fraction)it.next()).getPercentage() != 100) correct = false;break;
            }
        }
        assertTrue(correct);
    }

    @Test
    public void successfulGetDenominator()
    {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void successfulGetDivisor()
    {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(3, fraction.getDivisor());
    }

    @Test
    public void successfulGetDecimalFractionValue1()
    {
        Fraction fraction = new Fraction();
        assertEquals(100, fraction.getPercentage(), 0);
    }

    @Test
    public void successfulGetDecimalFractionValue2()
    {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(66.66, fraction.getPercentage(), 2);
    }
}

