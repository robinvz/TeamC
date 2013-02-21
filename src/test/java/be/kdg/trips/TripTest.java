package be.kdg.trips;

import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.Assert.assertNotNull;


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

    LoginController lg;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        lg = new LoginController();
        ReflectionTestUtils.setField(lg, "tripsService", tripsService);
        ReflectionTestUtils.setField(lg, "session", mockHttpSession);
        mockMvc = MockMvcBuilders.standaloneSetup(lg).build();
    }


}
