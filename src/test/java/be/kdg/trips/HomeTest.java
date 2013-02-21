package be.kdg.trips;

import be.kdg.trips.controllers.HomeController;
import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class HomeTest {


    private MockMvc mockMvc;

    HomeController hc;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        hc = new HomeController();
        mockMvc = MockMvcBuilders.standaloneSetup(hc).build();
    }

    @Test
    public void homeView() throws Exception {
        assertEquals(hc.index(), "index");
    }
}
