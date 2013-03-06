package be.kdg.trips;

import be.kdg.trips.controllers.TripController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import com.sun.org.apache.regexp.internal.RETest;
import org.mockito.Mockito;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TripTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private TripsService tripsService;
    private MockHttpSession mockHttpSession;
    private MockMvc mockMvc;
    private String title = "title";
    private String description = "description";
    private String privacyString = "PUBLIC";
    private TripPrivacy privacy = TripPrivacy.PUBLIC;
    private String startDate = "2013-01-01";
    private String endDate = "2013-02-02";
    private String requisite = "sampleRequisite";
    private String amount = "5";
    private String label = "voorbeeldLabel";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    User testUser = new User("test@student.kdg.be", "password");
    TripController tc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        tc = new TripController();
        ReflectionTestUtils.setField(tc, "tripsService", tripsService);
        ReflectionTestUtils.setField(tc, "session", mockHttpSession);
        ReflectionTestUtils.setField(tc, "messageSource", messageSource);
        mockMvc = MockMvcBuilders.standaloneSetup(tc).build();
    }

    @Test
    public void getTrips() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trips");
        when(tripsService.findAllNonPrivateTrips(testUser)).thenReturn(new ArrayList());
        when(tripsService.findPrivateTrips(testUser)).thenReturn(new ArrayList());
        when(tripsService.findTripsByOrganizer(testUser)).thenReturn(new ArrayList());
        when(tripsService.findEnrollmentsByUser(testUser)).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView")).andExpect(model().attributeExists("allNonPrivateTrips")).andExpect(model().attributeExists("allPrivateTrips")).andExpect(model().attributeExists("allOrganizedTrips")).andExpect(model().attributeExists("allEnrollments"));
    }

    @Test
    public void createTrip() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/createTrip");
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTBTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        Trip trip = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenReturn(trip);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + trip.getId()));
    }

    @Test
    public void createTBTripNotLoggedIn() throws Exception {
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        Trip trip = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenThrow(new TripsException("Not logged in"));
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void createTBTripStartDateAfterEndDate() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(endDate);
        Date endd = sdf.parse(startDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", endDate).param("endDate", startDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenThrow(new TripsException("Startdate after Enddate"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTBTripStartDateInPast() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        String PastDate = "2010-01-01";
        Date startd = sdf.parse(PastDate);
        Date endd = sdf.parse(endDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", PastDate).param("endDate", endDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenThrow(new TripsException("Startdate must be in future"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }
    /*
        @Test
        public void createTBTripOneDate() throws Exception {
            mockHttpSession.setAttribute("user", testUser);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate);
            when(tripsService.createTimeBoundTrip(anyString(), anyString(), any(TripPrivacy.class), any(User.class), any(Date.class), any(Date.class))).thenThrow(new ParseException("Unable to parse date: ", anyInt()));
            mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
        }
        */
    @Test
    public void createTLTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description", description).param("privacy", privacyString);
        when(tripsService.createTimelessTrip(title, description, privacy, testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + t.getId()));
    }

    @Test
    public void createTLTripNotLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description", description).param("privacy", privacyString);
        when(tripsService.createTimelessTrip(title, description, privacy, testUser)).thenThrow(new TripsException("Not logged in"));
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void createTLTripWrongUser() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description", description).param("privacy", privacyString);
        when(tripsService.createTimelessTrip(title, description, privacy, testUser)).thenThrow(new TripsException("Users does not exist"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void getTrip() throws Exception {
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId());
        mockHttpSession.setAttribute("user", testUser);
        when(tripsService.findTripById(t.getId(), (User) mockHttpSession.getAttribute("user"))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void getTripFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/-1");
        when(tripsService.findTripById(-1, testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void getTripUserNotAllowed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, new User("hallo", "hallo"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void timelessTripDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/deleteTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void tripNotDeletedNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        User organizer = new User("email", "password");
        Trip t = new TimelessTrip(title, description, privacy, organizer);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/deleteTrip/" + t.getId());
        Mockito.doThrow(new TripsException("You're not the organizer")).when(tripsService).deleteTrip(any(Trip.class), eq(testUser));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
        assertNotNull(t);
    }

    @Test
    public void timeboundTripDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(endDate);
        Date endd = sdf.parse(startDate);
        Trip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/deleteTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void timelessTripNotDeletedNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/deleteTrip/" + t.getId());
        Mockito.doThrow(new TripsException("Failed to delete trip")).when(tripsService).deleteTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
        assertNotNull(t);
    }

    @Test
    public void subscribeTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subscribe?tripId=" + t.getId()).requestAttr("locale", Locale.ENGLISH);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void subscribeTripFailedNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Cannot subsribe to trip when not logged in")).when(tripsService).subscribe(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void subscribeTripDouble() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subscribe?tripId=" + t.getId());
        when(tripsService.subscribe(t, testUser)).thenThrow(new TripsException("already subscribed"));
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void unSubscribeTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/unSubscribe?tripId=" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void unSubscribeTripFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/unSubscribe?tripId=" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        Mockito.doThrow(new TripsException("Failed to unsubscribe to trip")).when(tripsService).disenroll(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void unSubscribeTripFailedNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/unSubscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Cannot unsubsribe to trip when not logged in")).when(tripsService).subscribe(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void publishTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/publishTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void publishTripFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/publishTrip/" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        Mockito.doThrow(new TripsException("Failed to publish trip")).when(tripsService).publishTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void publishTripFailedNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/publishTrip/" + t.getId());
        Mockito.doThrow(new TripsException("Not logged in")).when(tripsService).publishTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void createLocationView() throws Exception {
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/createLocation");
        mockHttpSession.setAttribute("user", testUser);
        when(tripsService.findTripById(t.getId(), (User) mockHttpSession.getAttribute("user"))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("createLocationView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void createLocationSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("", "", "", "", ""), "", "", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/" + t.getId() + "/locations/createLocation").param("user", "testUser").param("trip", "t")
                .param("latitude", "1.00").param("longitude", "1.00").param("street", "testStreet").param("houseNr", "1").param("city", "testCity")
                .param("postalCode", "2000").param("country", "testCountry").param("title", "testTitle").param("description", "testDescription")
                .param("question", "testQuestion").param("correctAnswer", "testCorrectAnswer").param("request", "testRequest");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations"));
        assertEquals(1, t.getLocations().size());
    }

    @Test
    public void createLocationFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("", "", "", "", ""), "", "", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/createLocation").param("id", t.getId() + "");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
        assertEquals(1, t.getLocations().size());
    }


    @Test
    public void locationDeletedSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("street", "1", "city", "2000", "country"), "", "", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/" + l.getId() + "/deleteLocation");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations"));
    }

    @Test
    public void allTripsServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip 2", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        ArrayList<Trip> trips = new ArrayList<>();
        trips.add(t);
        trips.add(t2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/alltrips").param("username", "mathias").param("password", "fred");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findAllNonPrivateTrips(testUser)).thenReturn(trips);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip 1\",\"id\":" + t.getId() + "},{\"title\":\"Trip 2\",\"id\":" + t2.getId() + "}]}"));
    }

    @Test
    public void enrolledTripsServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip 2", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Enrollment enroll = new Enrollment(t, testUser);
        Enrollment enroll2 = new Enrollment(t2, testUser);
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enroll);
        enrollments.add(enroll2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/enrolledtrips").param("username", "mathias").param("password", "fred");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findEnrollmentsByUser(testUser)).thenReturn(enrollments);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip 1\",\"id\":" + t.getId() + "},{\"title\":\"Trip 2\",\"id\":" + t2.getId() + "}]}"));
    }

    @Test
    public void participantsTest() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/participants").param("username", "mathias").param("password", "fred");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findEnrollmentsByTrip(t)).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("users/participantsView")).andExpect(model().attributeExists("enrollments"));
    }

    @Test
    public void createdTripServiceTest() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip 2", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        ArrayList<Trip> trips = new ArrayList<>();
        trips.add(t);
        trips.add(t2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/createdtrips").param("username", "mathias").param("password", "fred");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findTripsByOrganizer(testUser)).thenReturn(trips);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip 1\",\"id\":" + t.getId() + "},{\"title\":\"Trip 2\",\"id\":" + t2.getId() + "}]}"));
    }

    @Test
    public void tripbyIdServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/trip").param("username", "mathias").param("password", "fred").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"id\":" + t.getId() + ",\"title\":\"" + t.getTitle() + "\",\"description\":\"Beschrijving\",\"enrollments\":0,\"privacy\":\"PUBLIC\"}"));
    }

    @Test
    public void tripLocationsByTripIdSucces() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        t.setLocations(new ArrayList<Location>());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations").param("username", "mathias").param("password", "fred").param("id", t.getId() + "");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("locationsView")).andExpect(model().attributeExists("trip")).andExpect(model().attributeExists("locations"));
    }

    @Test
    public void tripLocationsByTripIdFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        t.setLocations(new ArrayList<Location>());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations").param("username", "mathias").param("password", "fred").param("id", t.getId() + "");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void subscribeTripServiceSuccess() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/enroll").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void subscribeTripServiceFail() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/enroll").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.subscribe(t, testUser)).thenThrow(new TripsException("Could not subscribe"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void unsubscribeTripServiceSuccess() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/unsubscribe").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void unsubscribeTripServiceFail() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/unsubscribe").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        doThrow(new TripsException("Cannot unsubscribe")).when(tripsService).disenroll(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void startTripServiceSuccess() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/start").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void startTripServiceFail() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/start").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        doThrow(new TripsException("Cannot start trip")).when(tripsService).startTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void stopTripServiceSuccess() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/stop").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void stopTripServiceFail() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/stop").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        doThrow(new TripsException("Cannot stop trip")).when(tripsService).stopTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void getLocationsService() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        t.setLocations(new ArrayList<Location>());
        Location l1 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        Location l2 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location2", 1);
        Location l3 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location3", 2);
        Location l4 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location4", 3);
        Location l5 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location5", 4);
        List<String> antwoorden = new ArrayList<>();
        antwoorden.add("Groep A");
        antwoorden.add("Groep B");
        antwoorden.add("Groep C");
        antwoorden.add("Groep D");
        l1.setQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l2.setQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l3.setQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l4.setQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l5.setQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        t.addLocation(l1);
        t.addLocation(l2);
        t.addLocation(l3);
        t.addLocation(l4);
        t.addLocation(l5);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/locations").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"locations\":[{\"id\":0,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location1\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"]},{\"id\":0,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location2\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"]},{\"id\":0,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location3\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"]},{\"id\":0,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location4\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"]},{\"id\":0,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location5\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"]}]}"));
    }

    @Test
    public void searchServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip Een", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip Twee", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        ArrayList<Trip> trips = new ArrayList<>();
        trips.add(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/searchtrips").param("username", "mathias").param("password", "fred").param("keyword", "een");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findNonPrivateTripsByKeyword(anyString(), any(User.class))).thenReturn(trips);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip Een\",\"id\":" + t.getId() + "}]}"));
    }

    @Test
    public void labelsViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/labels/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void labelsView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/labels/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("labelsView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void labelsViewNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/labels/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void addLabel() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/labels/" + t.getId()).param("label", label);
        mockMvc.perform(requestBuilder).andExpect(view().name("labelsView"));
    }

    @Test
    public void addLabelTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/labels/" + t.getId()).param("label", label);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("labelsView"));
    }

    @Test
    public void addLabelNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/labels/" + t.getId()).param("label", label);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void requirementsView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requirements/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void requirementsViewNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requirements/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void requirementsViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requirements/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirement() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/"+t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void addRequirementFailed() throws Exception {
        User notOrganizer = new User("username", "password");
        mockHttpSession.setAttribute("user", notOrganizer);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/"+t.getId()).param("requisite", requisite).param("amount", amount);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void inviteUserView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/inviteUserView"));
    }

    @Test
    public void inviteUserViewNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/"+t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void inviteUserViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/"+t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/inviteUserView"));
    }

    @Test
    public void startTrip() {

    }

    @Test
    public void stopTrip() {

    }

    @Test
    public void editLocationSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("street", "houseNr", "city", "postalCode", "country"), "title", "description", 0);
        l.setDescription("newDescription");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/" + t.getId() + "/locations/editLocation").param("value", "ingevuldeWaarde")
                .param("id", "1-29").param("rowId", "1").param("columnPosition", "2").param("columnId", "2").param("columnName", "Beschrijving");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations"));
    }
}