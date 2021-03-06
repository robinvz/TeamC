package be.kdg.trips;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.controllers.TripController;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.*;
import org.mockito.Mockito;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
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
    private String startDate = "2013-01-01 01:01";
    private String endDate = "2013-02-02 01:01";
    private String requisite = "sampleRequisite";
    private String amount = "5";
    private String label = "voorbeeldLabel";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    public void getTripsFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trips");
        when(tripsService.findAllNonPrivateTrips(testUser)).thenThrow(new TripsException("No nonprivate trips found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void createTrip() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/createTrip");
        mockMvc.perform(requestBuilder).andExpect(view().name("/createTripView"));
    }

    @Test
    public void createTBTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        Repeatable rp = Repeatable.WEEKLY;
        int limit = 1;
        Trip trip = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate).param("repeat", "WEEKLY").param("limit", "1");
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd, rp, limit)).thenReturn(trip);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + trip.getId()));
    }

    @Test
    public void createTBTripNotLoggedIn() throws Exception {
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", startDate).param("endDate", endDate).param("repeat", "WEEKLY").param("limit", "1");
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd, null, null)).thenThrow(new TripsException("Not logged in"));
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void createTBTripStartDateAfterEndDate() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(endDate);
        Date endd = sdf.parse(startDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", endDate).param("endDate", startDate).param("repeat", "ONCE").param("limit", "");
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd, null, null)).thenThrow(new TripsException("Startdate after Enddate"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTBTripStartDateInPast() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        String PastDate = "2010-01-01 10:10";
        Date startd = sdf.parse(PastDate);
        Date endd = sdf.parse(endDate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("title", title).param("description", description).param("privacy", privacyString).param("startDate", PastDate).param("endDate", endDate).param("repeat", "ONCE").param("limit", "");
        when(tripsService.createTimeBoundTrip(title, description, privacy, testUser, startd, endd, null, null)).thenThrow(new TripsException("Startdate must be in future"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTLTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description", description).param("privacy", privacyString);
        when(tripsService.createTimelessTrip(title, description, privacy, testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/" + t.getId()));
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
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Enrollment enroll = new Enrollment(t, testUser);
        testUser.addEnrollment(enroll);
        enroll.addRequisite(requisite, Integer.parseInt(amount));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
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
    public void tripDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void tripNotDeletedTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteTrip/" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).deleteTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void tripNotDeletedMessagingException() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteTrip/" + t.getId());
        Mockito.doThrow(new MessagingException("Mail not sent")).when(tripsService).deleteTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void tripNotDeletedNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        User organizer = new User("email", "password");
        Trip t = new TimelessTrip(title, description, privacy, organizer);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteTrip/" + t.getId());
        Mockito.doThrow(new TripsException("not the organizer")).when(tripsService).deleteTrip(any(Trip.class), eq(testUser));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
        assertNotNull(t);
    }

    @Test
    public void subscribe() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subscribe?tripId=" + t.getId()).requestAttr("locale", Locale.ENGLISH);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void subscribeTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).subscribe(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void subscribeNotPublished() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("published")).when(tripsService).subscribe(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void subscribeNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("not logged in")).when(tripsService).subscribe(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void subscribeDouble() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subscribe?tripId=" + t.getId());
        when(tripsService.subscribe(t, testUser)).thenThrow(new TripsException("already subscribed"));
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
        assertEquals(0, tripsService.findAllNonPrivateTrips(testUser).size());
    }

    @Test
    public void unSubscribe() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/unSubscribe").param("tripId", "" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void unSubscribeNotEnrolled() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/unSubscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("not enrolled")).when(tripsService).disenroll(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void unSubscribeTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/unSubscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).disenroll(any(Trip.class), any((User.class)));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void unSubscribeNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/unSubscribe?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("not logged in")).when(tripsService).subscribe(t, testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void publish() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/publishTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void publishTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/publishTrip/" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).publishTrip(any(Trip.class), any((User.class)));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void publishNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/publishTrip/" + t.getId());
        Mockito.doThrow(new TripsException("is not the organizer")).when(tripsService).publishTrip(any(Trip.class), any((User.class)));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void publishAlreadyPublished() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/publishTrip/" + t.getId());
        Mockito.doThrow(new TripsException("already published")).when(tripsService).publishTrip(any(Trip.class), any((User.class)));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
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
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        List<String> list = new ArrayList();
        list.add("hallo");
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        ModelAndView mv = tc.createLocation(t.getId(), 0.0, 0.0, "street", "1", "city", "2000", "country", "title",
                multipartFile, "description", "question", "correctAnswer", list);
        assertEquals(mv.getViewName(), "redirect:/trip/" + t.getId() + "/locations");
    }

    @Test
    public void createLocationFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        List<String> list = new ArrayList();
        list.add("hallo");
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Could not find trip"));
        ModelAndView mv = tc.createLocation(t.getId(), 0.0, 0.0, "street", "1", "city", "2000", "country", "title",
                multipartFile, "description", "question", "correctAnswer", list);
        assertEquals(mv.getViewName(), "tripsView");
    }

    @Test
    public void locationDeletedSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("street", "1", "city", "2000", "country"), "", "", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + t.getId() + "/locations/" + 0 + "/deleteLocation");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations"));
    }

    @Test
    public void locationDeletedFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 1.00, 1.00, new Address("street", "1", "city", "2000", "country"), "", "", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + t.getId() + "/locations/" + 0 + "/deleteLocation");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).findTripById(anyInt(), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void participantsView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/participants").param("username", "mathias").param("password", "fred");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findEnrollmentsByTrip(t)).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("users/participantsView")).andExpect(model().attributeExists("enrollments"));
    }

    @Test
    public void participantsViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/participants").param("username", "mathias").param("password", "fred");
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).findTripById(anyInt(), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void participantsViewNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/participants").param("username", "mathias").param("password", "fred");
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void tripLocationsByTripIdFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.setLocations(new ArrayList<Location>());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations").param("username", "mathias").param("password", "fred").param("id", t.getId() + "");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void labelsViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/labels/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void labelsView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/labels/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/labelsView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void addLabel() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/labels/" + t.getId()).param("label", label);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/labelsView"));
    }

    @Test
    public void addLabelTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/labels/" + t.getId()).param("label", label);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addLabelNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/labels/" + t.getId()).param("label", label);
        Mockito.doThrow(new TripsException("is not the organizer")).when(tripsService).addLabelToTrip(any(Trip.class), any(User.class), anyString());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
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
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addRequirement() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void addRequirementAlreadyActive() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        Mockito.doThrow(new TripsException("active")).when(tripsService).addRequisiteToTrip(anyString(), anyInt(), any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        Mockito.doThrow(new TripsException("is not the organizer")).when(tripsService).addRequisiteToTrip(anyString(), anyInt(), any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requirements/" + t.getId()).param("requisite", requisite).param("amount", amount);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addRequirementToEnrollmentView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/addRequirementToEnrollmentView/" + testUser.getEmail() + "/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementToEnrollmentViewNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/addRequirementToEnrollmentView/" + testUser.getEmail() + "/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void addRequirementToEnrollmentViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/addRequirementToEnrollmentView/" + testUser.getEmail() + "/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addRequirementToEnrollment() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementToEnrollmentNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void addRequirementToEnrollmentTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addRequirementToEnrollmentUserNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("doesn't exist"));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementToEnrollmentNotOrganizer() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        User organizer = new User("email", "password");
        Trip t = new TimelessTrip(title, description, privacy, organizer);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        Mockito.doThrow(new TripsException("is not the organizer")).when(tripsService).addRequisiteToEnrollment(anyString(), anyInt(), any(Trip.class), any(User.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void addRequirementToEnrollmentUnExistingEnrollment() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addRequirementToEnrollment/" + testUser.getEmail() + "/" + t.getId()).param("requisite", requisite).param("amount", amount);
        Mockito.doThrow(new TripsException("")).when(tripsService).addRequisiteToEnrollment(anyString(), anyInt(), any(Trip.class), any(User.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("requirementsView"));
    }

    @Test
    public void inviteUserView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/inviteUser/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/inviteUserView"));
    }

    @Test
    public void inviteUserViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/inviteUser/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void startTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/startTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void startTripTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/startTrip/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void startTripAlreadyStarted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/startTrip/" + t.getId());
        Mockito.doThrow(new TripsException("is already started")).when(tripsService).startTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void startTripEnrollmentUnExisting() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/startTrip/" + t.getId());
        Mockito.doThrow(new TripsException("")).when(tripsService).startTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void stopTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/stopTrip/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void stopTripTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/stopTrip/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void stopTripNotStartedYet() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/stopTrip/" + t.getId());
        Mockito.doThrow(new TripsException("you haven't begun yet")).when(tripsService).stopTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void stopTripEnrollmentUnExisting() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/stopTrip/" + t.getId());
        Mockito.doThrow(new TripsException("")).when(tripsService).stopTrip(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void editLocationSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/editLocation").param("title", "testTitle").param("description", "testDescription");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId())).andExpect(model().attribute("trip", t)).andExpect(model().attribute("location", t.getLocations().get(0)));
    }

    @Test
    public void editLocationFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/editLocation").param("title", "testTitle").param("description", "testDescription");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void inviteFindUsers() throws Exception {
        TimelessTrip t = new TimelessTrip(title, description, privacy, testUser);
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/" + t.getId() + "/findUsersByKeyword").param("keyword", "test");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findUsersByKeyword("keyword", (User) mockHttpSession.getAttribute("user"))).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/inviteUserView"));
    }

    @Test
    public void sendInviteSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        User invitee = new User("email@email.com", "password");
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/inviteUser/" + t.getId() + "/sendInvite/").param("userByKeywordEmail", "testEmail");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findUser(anyString())).thenReturn(invitee);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/inviteUser/" + t.getId()));
    }

    @Test
    public void sendInviteFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/inviteUser/" + t.getId() + "/sendInvite/").param("userByKeywordEmail", "testEmail");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void sendInviteMessageFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/inviteUser/" + t.getId() + "/sendInvite/").param("userByKeywordEmail", "testEmail");
        when(tripsService.invite(any(Trip.class), any(User.class), any(User.class))).thenThrow(new MessagingException("could not send email"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void unInviteSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/inviteUser/" + t.getId() + "/uninvite").param("uninviteEmail", "test@test.be");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/inviteUser/" + t.getId()));
    }

    @Test
    public void unInviteFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/inviteUser/" + t.getId() + "/uninvite").param("uninviteEmail", "test@test.be");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addDateView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/addDate/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/addDateView"));
    }

    @Test
    public void addDateViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/addDate/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addDate() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addDate/" + t.getId()).param("startDate", "2013-10-10 20:20").param("endDate", "2013-11-11 20:20");
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/addDateView"));
    }

    @Test
    public void addDateTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addDate/" + t.getId()).param("startDate", "2013-10-10 20:20").param("endDate", "2013-11-11 20:20");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addDateTripStartDateInPast() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addDate/" + t.getId()).param("startDate", "2013-10-10 20:20").param("endDate", "2013-11-11 20:20");
        Mockito.doThrow(new TripsException("future")).when(tripsService).addDateToTimeBoundTrip(any(Date.class), any(Date.class), any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/addDateView"));
    }

    @Test
    public void addDateTripEndDateBeforeStartSate() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Date startd = sdf.parse(startDate);
        Date endd = sdf.parse(endDate);
        TimeBoundTrip t = new TimeBoundTrip(title, description, privacy, testUser, startd, endd);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addDate/" + t.getId()).param("startDate", "2013-10-10 20:20").param("endDate", "2013-11-11 20:20");
        Mockito.doThrow(new TripsException("before")).when(tripsService).addDateToTimeBoundTrip(any(Date.class), any(Date.class), any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/addDateView"));
    }

    @Test
    public void costsView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/costs/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("costsView"));
    }

    @Test
    public void costsViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/costs/" + t.getId());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void createCost() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/costs/" + t.getId() + "/createCost").param("name", "eenKost").param("amount", "2.5");
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/costs/" + t.getId()));
    }

    @Test
    public void createCostTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/costs/" + t.getId() + "/createCost").param("name", "eenKost").param("amount", "2.5");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void acceptInvitation() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/acceptInvitation?tripId=" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void acceptInvitationTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/acceptInvitation?tripId=" + t.getId());
        when(tripsService.acceptInvitation(any(Trip.class), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void acceptInvitationInvitationUnExisting() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/acceptInvitation?tripId=" + t.getId());
        when(tripsService.acceptInvitation(any(Trip.class), any(User.class))).thenThrow(new TripsException("Invitation for user"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void acceptInvitationTripAlreadyActive() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/acceptInvitation?tripId=" + t.getId());
        when(tripsService.acceptInvitation(any(Trip.class), any(User.class))).thenThrow(new TripsException(""));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void declineInvitation() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/declineInvitation?tripId=" + t.getId());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void declineInvitationTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/declineInvitation?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).declineInvitation(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void declineInvitationInvitationUnExisting() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/declineInvitation?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("Invitation for user")).when(tripsService).declineInvitation(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void declineInvitationEnrollmentUnExisting() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/declineInvitation?tripId=" + t.getId());
        Mockito.doThrow(new TripsException("")).when(tripsService).declineInvitation(any(Trip.class), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void getLocation() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + anyInt() + "/locations/" + anyInt());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/locationView")).andExpect(model().attribute("trip", t)).andExpect(model().attribute("location", t.getLocations().get(0)));
    }

    @Test
    public void getLocationFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + anyInt() + "/locations/" + anyInt());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void getLocationNotLoggedIn() throws Exception {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + anyInt() + "/locations/" + anyInt());
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void deleteQuestionSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/deleteQuestion");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId())).andExpect(model().attribute("trip", t)).andExpect(model().attribute("location", t.getLocations().get(0)));
    }

    @Test
    public void deleteQuestionFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/deleteQuestion");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void editQuestionSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/editQuestion").param("question", "testQuestion")
                .param("possibleAnswers", "testAnswers").param("correctAnswer", "testAnswer");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId())).andExpect(model().attribute("trip", t)).andExpect(model().attribute("location", t.getLocations().get(0)));
    }

    @Test
    public void editQuestionFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/editQuestion").param("question", "testQuestion")
                .param("possibleAnswers", "testAnswers").param("correctAnswer", "testAnswer");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void addQuestionSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        ModelAndView mv = tc.addQuestion(anyInt(), anyInt(), "question", multipartFile, new ArrayList<String>(), "correctAnswer");
        assertEquals(mv.getViewName(), "redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId());
        assertTrue(mv.getModel().containsKey("trip"));
        assertEquals(mv.getModel().get("trip"), t);
        assertTrue(mv.getModel().containsKey("location"));
        assertEquals(mv.getModel().get("location"), t.getLocations().get(0));
    }

    @Test
    public void addQuestionFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Could not find trip"));
        ModelAndView mv = tc.addQuestion(anyInt(), anyInt(), "question", multipartFile, new ArrayList<String>(), "correctAnswer");
        assertEquals(mv.getViewName(), "tripsView");
    }

    @Test
    public void showQuestionPicSuccess() throws Exception{
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        t.addLocation(new Location());
        byte[] b = new byte[1024];
        l.addQuestion(new Question("question",new ArrayList<String>(),0,b));
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/" + 0 + "/questionPic");
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        mockMvc.perform(requestBuilder).andExpect(content().bytes(b));
    }

    @Test
    public void showQuestionPicFail() throws Exception{
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Location l = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        t.addLocation(new Location());
        byte[] b = new byte[1024];
        l.addQuestion(new Question("question",new ArrayList<String>(),0,b));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/" + 0 + "/questionPic");
        when(tripsService.findLocationById(0)).thenThrow(new TripsException("Could not find location"));
        mockMvc.perform(requestBuilder).andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void editQuestionPicSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        ModelAndView mv = tc.editQuestionPic(anyInt(), anyInt(), multipartFile);
        assertEquals(mv.getViewName(), "redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId());
        assertTrue(mv.getModel().containsKey("trip"));
        assertEquals(mv.getModel().get("trip"), t);
        assertTrue(mv.getModel().containsKey("location"));
        assertEquals(mv.getModel().get("location"), t.getLocations().get(0));
    }

    @Test
    public void editQuestionPicFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Could not find trip"));
        ModelAndView mv = tc.editQuestionPic(anyInt(), anyInt(), multipartFile);
        assertEquals(mv.getViewName(), "tripsView");
    }

    @Test
    public void deleteQuestionImageSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/deleteQuestionImage");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/trip/" + t.getId() + "/locations/" + t.getLocations().get(0).getId())).andExpect(model().attribute("trip", t)).andExpect(model().attribute("location", t.getLocations().get(0)));
    }

    @Test
    public void deleteQuestionImageFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.addLocation(new Location());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/trip/" + anyInt() + "/locations/" + anyInt() + "/deleteQuestionImage");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(new Location());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException(""));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void editTrip() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/" + t.getId() + "/editTrip").param("title", "title").param("description", "description").param("chatAllowed", "false").param("positionVisible", "false");
        mockMvc.perform(requestBuilder).andExpect(view().name("tripView"));
    }

    @Test
    public void editTripFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/" + t.getId() + "/editTrip").param("title", "title").param("description", "description").param("chatAllowed", "false").param("positionVisible", "false");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void editTripTheme() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/editTripTheme/" + t.getId()).param("theme", "default");
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:editTripPicView/" + t.getId()));
    }

    @Test
    public void editTripThemeTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/editTripTheme/" + t.getId()).param("theme", "default");
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).findTripById(anyInt(), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void editTripThemeFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/editTripTheme/" + t.getId()).param("theme", "default");
        Mockito.doThrow(new TripsException("")).when(tripsService).changeThemeOfTrip(any(Trip.class), anyString());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void editTripPicView() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/editTripPic/" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/editTripPicView"));
    }

    @Test
    public void editTripPicViewTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/editTripPic/" + t.getId());
        Mockito.doThrow(new TripsException("Trip with id")).when(tripsService).findTripById(anyInt(), any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    /*
    @Test   //method returns Byte[]
    public void showProfilePic() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tripPic//" + t.getId());
        mockMvc.perform(requestBuilder).andExpect(new Byte[]{});
    }
    */
    @Test
    public void findUsersByKeywordSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/" + t.getId() + "/findUsersByKeyword").param("keyword", "testKeyword");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/inviteUserView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void findUsersByKeywordFailed() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/inviteUser/" + t.getId() + "/findUsersByKeyword").param("keyword", "testKeyword");
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip with id"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void getAllLocationsLatLngSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.setLocations(new ArrayList<Location>());
        Location l1 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        Location l2 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location2", 1);
        Location l3 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location3", 2);
        Location l4 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location4", 3);
        Location l5 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location5", 4);
        t.addLocation(l1);
        t.addLocation(l2);
        t.addLocation(l3);
        t.addLocation(l4);
        t.addLocation(l5);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/getLocationsLatLng").param("amount", "all").param("locationId", "0");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("[{\"latitude\":12,\"longitude\":13},{\"latitude\":12,\"longitude\":13},{\"latitude\":12,\"longitude\":13},{\"latitude\":12,\"longitude\":13},{\"latitude\":12,\"longitude\":13}]"));
    }

    @Test
    public void getOneLocationLatLngSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.setLocations(new ArrayList<Location>());
        Location l = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        t.addLocation(l);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/getLocationsLatLng").param("amount", "one").param("locationId", "0");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findLocationById(anyInt())).thenReturn(l);
        mockMvc.perform(requestBuilder).andExpect(content().string("[{\"latitude\":12,\"longitude\":13}]"));
    }

    @Test
    public void getLocationsLatLngFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        t.setLocations(new ArrayList<Location>());
        Location l1 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        Location l2 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location2", 1);
        Location l3 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location3", 2);
        Location l4 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location4", 3);
        Location l5 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location5", 4);
        t.addLocation(l1);
        t.addLocation(l2);
        t.addLocation(l3);
        t.addLocation(l4);
        t.addLocation(l5);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trip/" + t.getId() + "/locations/getLocationsLatLng").param("amount", "one").param("locationId", "0");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(content().string("redirect:/trips"));
    }

    @Test
    public void deleteCost() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Enrollment enrollment = new Enrollment(t, testUser);
        String name = "name";
        double amount = 10.5;
        enrollment.addCost(name, amount);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/costs/" + t.getId() + "/deleteCost/" + name + "/" + amount);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:/costs/" + t.getId()));
    }

    @Test
    public void deleteCostTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        Enrollment enrollment = new Enrollment(t, testUser);
        String name = "name";
        double amount = 10.5;
        enrollment.addCost(name, amount);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/costs/" + t.getId() + "/deleteCost/" + name + "/" + amount);
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Trip not found"));
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView"));
    }

    @Test
    public void switchLocationSuccess() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/switchLocation").param("id", anyInt() + "-" + anyInt()).param("fromPosition", "1")
                .param("toPosition", "2").param("direction", "testDirection");
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(view().name("locationsView")).andExpect(model().attribute("trip", t));
    }

    @Test
    public void switchLocationTripNotFound() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trip/switchLocation").param("id", anyInt() + "-" + anyInt()).param("fromPosition", "1")
                .param("toPosition", "2").param("direction", "testDirection");
        when(tripsService.findTripById(t.getId(), testUser)).thenThrow(new TripsException("Could not find trip"));
        mockMvc.perform(requestBuilder).andExpect(view().name("locationsView"));
    }

    @Test
    public void testEditTripPic() throws TripsException {
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        MockMultipartHttpServletRequest mockMultipartHttpServletRequest = (MockMultipartHttpServletRequest) request;
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenReturn(t);
        ModelAndView mv = tc.editTripPic(0, multipartFile, null);
        assertEquals(mv.getViewName(), "/users/editTripPicView");
        assertTrue(mv.getModel().containsKey("trip"));
        assertEquals(mv.getModel().get("trip"), t);
    }

    @Test
    public void testEditTripPicFailTripsException() throws TripsException {
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        when(tripsService.findTripById(anyInt(), any(User.class))).thenThrow(new TripsException("Fail"));
        ModelAndView mv = tc.editTripPic(0, multipartFile, null);
        assertEquals(mv.getViewName(), "tripsView");
        assertTrue(mv.getModel().containsKey("error"));
    }

    @Test
    public void getTripPic() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        byte[] b = new byte[1024];
        t.setImage(b);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tripPic/1");
        when(tripsService.findTripById(1, testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().bytes(b));
    }

    @Test
    public void getTripPicFail() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        Trip t = new TimelessTrip(title, description, privacy, testUser);
        byte[] b = new byte[1024];
        t.setImage(b);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tripPic/1");
        when(tripsService.findTripById(1, testUser)).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).equals(null);
    }

}