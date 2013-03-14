package be.kdg.trips;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.controllers.ContactController;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.mail.MessagingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ContactTest {
    private MockMvc mockMvc;
    ContactController cc;

    @Mock
    private TripsService tripsService;

    @Mock
    private MessageSource messageSource;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        cc = new ContactController();
        ReflectionTestUtils.setField(cc, "tripsService", tripsService);
        ReflectionTestUtils.setField(cc, "messageSource", messageSource);

        mockMvc = MockMvcBuilders.standaloneSetup(cc).build();
    }

    @Test
    public void contactView() throws Exception {
        assertEquals(cc.contact(), "contactView");
    }

    @Test
    public void sendMailSucces() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/contact/sendContactMail").param("email", "mathias").param("type", "Hallo").param("message", "ik ben er");
        mockMvc.perform(requestBuilder).andExpect(view().name("indexView"));
    }

    @Test
    public void sendMailFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/contact/sendContactMail").param("email", "mathias").param("type", "Hallo").param("message", "ik ben er");
        Mockito.doThrow(new MessagingException()).when(tripsService).sendContactMail(anyString(), anyString(), anyString());
       mockMvc.perform(requestBuilder).andExpect(view().name("contactView"));

    }

}
