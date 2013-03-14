package be.kdg.trips;

import be.kdg.trips.controllers.ContactController;
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
public class ContactTest {
    private MockMvc mockMvc;
    ContactController cc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        cc = new ContactController();
        mockMvc = MockMvcBuilders.standaloneSetup(cc).build();
    }

    @Test
    public void contactView() throws Exception {
        assertEquals(cc.contact(), "contactView");
    }

}
