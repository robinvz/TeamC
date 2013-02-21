package be.kdg.trips;

import be.kdg.trips.controllers.ErrorController;
import be.kdg.trips.controllers.HomeController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ErrorTest {

    private MockMvc mockMvc;

    ErrorController ec;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ec = new ErrorController();
        mockMvc = MockMvcBuilders.standaloneSetup(ec).build();
    }

    @Test
    public void errorView() throws Exception {
        assertEquals(ec.showError(), "errors/loginErrorView");
    }
}
