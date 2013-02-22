package be.kdg.trips;

import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

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

    @Test
    public void loginView() throws Exception {
        assertEquals(lg.login(), "loginView");
    }

    @Test
    public void registerView() throws Exception {
        assertEquals(lg.register(new User()), "registerView");
    }

    @Test
    public void loginServiceCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service/login").param("username", "bob").param("password", "bob");
        when(tripsService.checkLogin("bob", "bob")).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void loginServiceFalse() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service/login").param("username", "bob").param("password", "bob");
        when(tripsService.checkLogin("bob", "bobette")).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void loginUserCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").param("email", "bob").param("password", "bob");
        when(tripsService.checkLogin("bob", "bob")).thenReturn(true);
        when(tripsService.findUser("bob")).thenReturn(new User("bob", "bob"));
        mockMvc.perform(requestBuilder);
        assertNotNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void registerUserShortSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").param("email", "bobette@gmail.com").param("password", "bobb");
        User user = new User("bobette@gmail.com", "bobb");
        when(tripsService.createUser("bobette@gmail.com", "bobb")).thenReturn(user);
        mockMvc.perform(requestBuilder).andExpect(view().name("indexView"));
        assertNotNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void registerUserShortFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").param("email", "bob").param("password", "bob");
        when(tripsService.createUser("bob", "bob")).thenThrow(new TripsException("User Exists"));
        mockMvc.perform(requestBuilder).andExpect(view().name("registerView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void loginUserWrong() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").param("email", "bob").param("password", "bobette");
        when(tripsService.checkLogin("bob", "bob")).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }


    @Test
    public void logOutUser() throws Exception {
        mockHttpSession.setAttribute("user", new User("mathias", "mathias"));
        assertNotNull(mockHttpSession.getAttribute("user"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/logout");
        mockMvc.perform(requestBuilder);
        assertNull(mockHttpSession.getAttribute("user"));
    }



}
