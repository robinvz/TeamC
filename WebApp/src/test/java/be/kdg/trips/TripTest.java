package be.kdg.trips;

import be.kdg.trips.controllers.TripController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
    public void createTimeBoundTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        Trip trip = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenReturn(trip);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + trip.getId()));
    }

    @Test
    public void createTimeBoundStartDateAfterEndDate() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(endDate);
        Date endd = sdf.parse(startDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", endDate).param("endDate", startDate);
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd)).thenThrow(new TripsException("Startdate after Enddate"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTimelessTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description", description).param("privacy", privacyString);
        when(tripsService.createTimelessTrip(title, description, privacy, testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + t.getId()));
    }

    @Test
    public void createTimelessTripWrongUser() throws Exception {
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
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
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
        Mockito.doThrow(new TripsException("Cannot publish trips when not logged in")).when(tripsService).publishTrip(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void createLocationView() throws Exception {
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/createLocation");
        mockHttpSession.setAttribute("user", testUser);
        when(tripsService.findTripById(t.getId(), (User) mockHttpSession.getAttribute("user"))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("createLocationView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void createLocation() throws Exception {
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/" + t.getId() + "/createLocation").param("user", "testUser").param("trip", "t")
                .param("latitude", "1.00").param("longitude", "1.00").param("street", "testStreet").param("houseNr", "1").param("city", "testCity")
                .param("postalCode", "2000").param("province", "testProvince").param("country", "testCountry").param("title", "testTitle").param("description", "testDescription");
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations"));
        t.addLocation(new Location(t, 1.00, 1.00, new Address("street", "1", "city", "2000", "province", "country"), title, description, 1));
        assertEquals(1, t.getLocations().size());
    }

    /*@Test
    public void locationDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        //Location l = new Location(t, 1.00, 1.00, new Address("street", "1", "city", "2000", "province", "country"), "", "", 0);
        tripsService.addLocationToTrip(testUser, t, 1.00, 1.00, "","","","","","","","");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/" + tripsService.findTripById(t.getId(),testUser).getLocations().get(0) + "/deleteLocation");
        mockMvc.perform(requestBuilder).andExpect(view().name("locationsView"));
        assertEquals(0, tripsService.findLocationById(l.getId()));
    }   */


    @Test
    public void allTripsServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip 2", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        ArrayList<Trip> trips = new ArrayList<>();
        trips.add(t);
        trips.add(t2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service/alltrips").param("username", "mathias").param("password", "fred");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findAllNonPrivateTrips(testUser)).thenReturn(trips);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip 1\",\"id\":"+ t.getId()+"},{\"title\":\"Trip 2\",\"id\":"+ t2.getId()+ "}]}"));
    }

    @Test
    public void enrolledTripsServiceSucces() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Trip t2 = new TimelessTrip("Trip 2", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Enrollment enroll = new Enrollment(t, testUser);
        Enrollment enroll2 = new Enrollment(t2, testUser) ;
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enroll);
        enrollments.add(enroll2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service/enrolledtrips").param("username", "mathias").param("password", "fred");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findEnrollmentsByUser(testUser)).thenReturn(enrollments);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"trips\":[{\"title\":\"Trip 1\",\"id\":"+ t.getId()+"},{\"title\":\"Trip 2\",\"id\":"+ t2.getId()+"}]}"));
    }

    @Test
    public void participantsTest() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/"+ t.getId() +"/participants").param("username", "mathias").param("password", "fred");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findEnrollmentsByTrip(t)).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("participantsView")).andExpect(model().attributeExists("enrollments"));
    }

}