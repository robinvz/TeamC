package be.kdg.trips;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.controllers.LoginController;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
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

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    @Mock
    private TripsService tripsService;

    private MockHttpSession mockHttpSession;

    private MockMvc mockMvc;

    @Mock
    private BindingResult mockBindingResult;


    LoginController lg;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        lg = new LoginController();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(lg, "tripsService", tripsService);
        ReflectionTestUtils.setField(lg, "session", mockHttpSession);
        mockMvc = MockMvcBuilders.standaloneSetup(lg).build();
    }

    @Test
    public void loginView() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login");
        mockMvc.perform(requestBuilder).andExpect(model().attributeExists("loginBean")).andExpect(view().name("loginView"));

    }

    @Test
    public void registerView() throws Exception {
        assertEquals(lg.register(new User()), "registerView");
    }

    @Test
    public void loginServiceCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/login").param("username", "bob").param("password", "bob");
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
        public void loginServiceFalse() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service/login").param("username", "bob").param("password", "bob");
        when(tripsService.checkLogin("bob", "bobette")).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":false}"));
    }

    @Test
    public void facebookloginCorrect() throws Exception {
        User user = new User("bobette@gmail.com", "bobb");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/facebooklogin").param("username", "bob").param("password", "bob");
        when(tripsService.findUser(anyString())).thenReturn(user);
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        when(tripsService.createUser(any(User.class))).thenThrow(new TripsException("User already exists"));
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }


    @Test
    public void facebookloginNewUser() throws Exception {
        User user = new User("bobette@gmail.com", "bobb");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/facebooklogin").param("username", "bob").param("password", "bob");
        when(tripsService.findUser(anyString())).thenReturn(user);
        when(tripsService.checkLogin(anyString(), anyString())).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(content().string("{\"valid\":true}"));
    }

    @Test
    public void facebookloginUserExistsWithOtherPassword() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/facebooklogin").param("username", "bob").param("password", "bob");
        when(tripsService.checkLogin(anyString(), anyString())).thenThrow(new TripsException("User already exists"));
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
        User user = new User("bobette@gmail.com", "bobb");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").param("password", "password");
        when(tripsService.createUser(any(User.class))).thenReturn(user);
        mockMvc.perform(requestBuilder).andExpect(view().name("indexView"));
        assertNotNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void registerUserShortFail() throws Exception {
        User user = new User("bobette@gmail.com", "bobb");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").param("password", "password");
        when(tripsService.createUser(any(User.class))).thenThrow(new TripsException("User Exists"));
        mockMvc.perform(requestBuilder).andExpect(view().name("registerView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }


    @Test
    public void registerUserEmptyPassword() throws Exception {
        User user = new User("bobette@gmail.com", "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").requestAttr("validUser", user).param("password", "");
        mockMvc.perform(requestBuilder).andExpect(view().name("registerView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void registerWithErrors() throws Exception {
        User user = new User("bobette@gmail.com", "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").requestAttr("validUser", user).param("password", "");
        when(mockBindingResult.hasErrors()).thenReturn(true);
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

    @Test
    public void loginWithErrors() throws Exception {
        User user = new User("bobette@gmail.com", "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login");
        when(mockBindingResult.hasErrors()).thenReturn(true);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }



}
