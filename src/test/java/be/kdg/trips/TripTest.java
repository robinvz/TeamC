package be.kdg.trips;

import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.controllers.TripController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.TimeBoundTrip;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;
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
    private TripsService tripsService;



    private MockHttpSession mockHttpSession;

    private MockMvc mockMvc;

    TripController tc;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        tc = new TripController();
        ReflectionTestUtils.setField(tc, "tripsService", tripsService);
        ReflectionTestUtils.setField(tc, "session", mockHttpSession);
        mockMvc = MockMvcBuilders.standaloneSetup(tc).build();
    }

    @Test
    public void getTrips() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/trips");
        when(tripsService.findAllTimelessNonPrivateTrips()).thenReturn(new ArrayList());
        when(tripsService.findAllTimeBoundPublishedNonPrivateTrips()).thenReturn(new ArrayList());
        mockMvc.perform(requestBuilder).andExpect(view().name("tripsView")).andExpect(model().attributeExists("timeboundTrips")).andExpect(model().attributeExists("timelessTrips"));
    }

    @Test
    public void createTrip() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/createTrip");
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTimeBoundTrip() throws Exception {
        User user = new User("bob", "bob");
        mockHttpSession.setAttribute("user", user);
        String title= "Test Trip";
        String description = "Dit is een test";
        String privacy = "PUBLIC";
        String startdate = "21/2/2013 08:00:00";
        String enddate = "22/2/2013 08:00:00";
        TripPrivacy pr = TripPrivacy.PUBLIC;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startd = sdf.parse(startdate);
        Date endd = sdf.parse(enddate);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("startDate", startdate).param("endDate",enddate ).param("title", title).param("description",description ).param("privacy", privacy);
        when(tripsService.createTimeBoundTrip(title,description, pr, user, startd, endd)).thenReturn(new TimeBoundTrip(title, description, pr, user, startd, endd));
        mockMvc.perform(requestBuilder).andExpect(view().name("trip/0"));
    }

    @Test
    public void createTimeBoundStartDateAfterEndDate() throws Exception {
        User user = new User("bob", "bob");
        mockHttpSession.setAttribute("user", user);
        String title= "Test Trip";
        String description = "Dit is een test";
        String privacy = "PUBLIC";
        String enddate = "21/2/2013 08:00:00";
        String startdate = "22/2/2013 08:00:00";
        TripPrivacy pr = TripPrivacy.PUBLIC;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startd = sdf.parse(startdate);
        Date endd = sdf.parse(enddate);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeBoundTrip").param("startDate", startdate).param("endDate",enddate ).param("title", title).param("description",description ).param("privacy", privacy);
        when(tripsService.createTimeBoundTrip(title,description, pr, user, startd, endd)).thenThrow(new TripsException("Startdate after Enddate"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/createTripView"));
    }

    @Test
    public void createTimelessTrip() throws Exception {
        User user = new User("bob", "bob");
        mockHttpSession.setAttribute("user", user);
        String title= "Test Trip";
        String description = "Dit is een test";
        String privacy = "PUBLIC";
        TripPrivacy pr = TripPrivacy.PUBLIC;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description",description ).param("privacy", privacy);
        when(tripsService.createTimelessTrip(title,description, pr, user)).thenReturn(new TimelessTrip(title, description, pr, user));;
        mockMvc.perform(requestBuilder).andExpect(view().name("redirect:trip/1"));
    }

    @Test
    public void createTimelessTripWrongUser() throws Exception {
        User user = new User("bob", "bob");
        mockHttpSession.setAttribute("user", user);
        String title= "Test Trip";
        String description = "Dit is een test";
        String privacy = "PUBLIC";
        TripPrivacy pr = TripPrivacy.PUBLIC;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createTimeLessTrip").param("title", title).param("description",description ).param("privacy", privacy);
        when(tripsService.createTimelessTrip(title,description, pr, user)).thenThrow(new TripsException("Users does not exist"));;
        mockMvc.perform(requestBuilder).andExpect(view().name("/errors/loginErrorView"));
    }

    @Test
    public void getTrip(){


    }



}
