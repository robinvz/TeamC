package be.kdg.trips;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.controllers.MobileController;
import be.kdg.trips.controllers.TripController;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.Status;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class MobileTest {
    @Mock
    private MessageSource messageSource;
    @Mock
    private TripsService tripsService;
    private MobileController tc;
    private MockMvc mockMvc;
    User testUser = new User("test@student.kdg.be", "password");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tc = new MobileController();
        ReflectionTestUtils.setField(tc, "tripsService", tripsService);
        ReflectionTestUtils.setField(tc, "messageSource", messageSource);
        mockMvc = MockMvcBuilders.standaloneSetup(tc).build();
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
        Enrollment enroll = new Enrollment(t, testUser);
        enroll.setStatus(Status.BUSY);
        Enrollment enroll2 = new Enrollment(t, new User("Mathias", "Bert"));
        enroll2.setStatus(Status.BUSY);
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enroll);
        enrollments.add(enroll2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/trip").param("username", "mathias").param("password", "fred").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findEnrollmentsByUser(testUser)).thenReturn(enrollments);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"id\":"+ t.getId() +",\"title\":\"Trip 1\",\"description\":\"Beschrijving\",\"enrollments\":2,\"organizer\":\"test@student.kdg.be\",\"privacy\":\"PUBLIC\",\"isenrolled\":true,\"isstarted\":true,\"isactive\":false,\"istimeless\":true}"));
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
    public void unSubscribeTripServiceSuccess() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/unsubscribe").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void unSubscribeTripServiceFail() throws Exception {
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
        Enrollment enroll = new Enrollment(t, testUser);
        enroll.setStatus(Status.BUSY);
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enroll);
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
        l1.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l2.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l3.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        l5.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        t.addLocation(l1);
        t.addLocation(l2);
        t.addLocation(l3);
        t.addLocation(l4);
        t.addLocation(l5);
        Map answeredQuestions = new HashMap<Question, Boolean>();
        answeredQuestions.put(l1.getQuestion(), Boolean.TRUE);
        answeredQuestions.put(l2.getQuestion(), Boolean.FALSE);
        answeredQuestions.put(l3.getQuestion(), Boolean.TRUE);
        answeredQuestions.put(l5.getQuestion(), Boolean.TRUE);
        enroll.setAnsweredQuestions(answeredQuestions);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/locations").param("username", testUser.getEmail()).param("password", "password").param("id", t.getId() + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        when(tripsService.findUser(testUser.getEmail())).thenReturn(testUser);
        when(tripsService.findEnrollmentsByUser(testUser)).thenReturn(enrollments);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"locations\":[{\"id\":null,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location1\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"],\"answered\":true,\"correct\":true},{\"id\":null,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location2\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"],\"answered\":true,\"correct\":true},{\"id\":null,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location3\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"],\"answered\":true,\"correct\":true},{\"id\":null,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location4\",\"question\":null},{\"id\":null,\"title\":\"Location\",\"latitude\":12,\"longitude\":13,\"description\":\"Aangename location5\",\"question\":\"Welke groep is de beste?\",\"possibleAnswers\":[\"Groep A\",\"Groep B\",\"Groep C\",\"Groep D\"],\"answered\":true,\"correct\":true}]}"));
    }

    @Test
    public void getLocationServiceFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/locations").param("username", testUser.getEmail()).param("password", "password").param("id", 0 + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void searchServiceSuccess() throws Exception {
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
    public void serviceContacts() throws Exception {
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        User user1 = new User("Polle", "Hallo");
        user1.setFirstName("Polledvdp");
        user1.setLastName("Van de Pol");
        User user2 = new User("Bert", "Hallo");
        Enrollment enroll = new Enrollment(t, testUser);
        Enrollment enroll2 = new Enrollment(t, user1);
        Enrollment enroll3 = new Enrollment(t, user2);
        enroll.setStatus(Status.BUSY);
        enroll2.setStatus(Status.BUSY);
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enroll);
        enrollments.add(enroll2);
        enrollments.add(enroll3);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/contacts").param("username", "mathias").param("password", "fred").param("id", t.getId()+"");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findEnrollmentsByTrip(t)).thenReturn(enrollments);
        when(tripsService.findTripById(t.getId(), testUser)).thenReturn(t);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"contacts\":[{\"firstName\":\"Polledvdp\",\"lastName\":\"Van de Pol\",\"email\":\"Polle\",\"latitude\":0,\"longitude\":0}]}"));
    }


    @Test
    public void serviceContactsFail() throws Exception{
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/contacts").param("username", "mathias").param("password", "fred").param("id", t.getId()+"");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("Hallo"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void questionServiceSucces() throws Exception{
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Location l1 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        List<String> antwoorden = new ArrayList<>();
        antwoorden.add("Groep A");
        antwoorden.add("Groep B");
        antwoorden.add("Groep C");
        antwoorden.add("Groep D");

        l1.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findLocationById(anyInt())).thenReturn(l1);
        when(tripsService.checkAnswerFromQuestion(any(Question.class), anyInt(), any(User.class))).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/answerQuestion").param("username", "mathias").param("password", "fred").param("answerIndex", 2+"").param("locationId", 0 + "");
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true,\"correct\":true}"));
    }

    @Test
    public void questionServiceFail() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/answerQuestion").param("username", "mathias").param("password", "fred").param("answerIndex", 2+"").param("locationId", 0 + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void moveServiceSucces() throws Exception{
        Trip t = new TimelessTrip("Trip 1", "Beschrijving", TripPrivacy.PUBLIC, testUser);
        Location l1 = new Location(t, 12.00, 13.00, null, "Location", "Aangename location1", 0);
        List<String> antwoorden = new ArrayList<>();
        antwoorden.add("Groep A");
        antwoorden.add("Groep B");
        antwoorden.add("Groep C");
        antwoorden.add("Groep D");

        l1.addQuestion(new Question("Welke groep is de beste?", antwoorden, 2, null));
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        when(tripsService.findLocationById(anyInt())).thenReturn(l1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/move").param("username", "mathias").param("password", "fred").param("locationId", 0 + "");
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void moveServiceFail() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/move").param("username", "mathias").param("password", "fred").param("locationId", 0 + "");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void updateServiceSuccses() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/updateLocation").param("username", "mathias").param("password", "fred").param("latitude", "5").param("longitude", "5");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void updateLocationFail() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/updateLocation").param("username", "mathias").param("password", "fred").param("latitude", "5").param("longitude", "5");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }
}
