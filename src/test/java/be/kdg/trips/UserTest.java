package be.kdg.trips;

import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private TripsService tripsService;

    private MockHttpSession mockHttpSession;

    private MockMvc mockMvc;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        LoginController lg = new LoginController();
        ReflectionTestUtils.setField(lg, "tripsService", tripsService);
        ReflectionTestUtils.setField(lg, "session", mockHttpSession);
        mockMvc = MockMvcBuilders.standaloneSetup(lg).build();
    }

    @Test
    public void userLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").param("email", "bob").param("password", "bob");
        when(tripsService.checkLogin("bob", "bob")).thenReturn(true);
        when(tripsService.findUser("bob")).thenReturn(new User("bob", "bob"));
        mockMvc.perform(requestBuilder);
        assertNotNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void loginUserCorrect() {
        //User user = userService.getUserByEmail(email);
        //assertEquals(user.email, email);
    }

    @Test
    public void changePasswordTest() {
     /*   User user = (User) session.getAttribute("user");
        String newPassword = "nieuw";

        session.setAttribute("user", user);
        assertEquals(session.getAttribute("user"), "nieuw");  */
    }

    @Test
    public void deleteUserTest() {
     /*   User user = (User) session.getAttribute("user");

        assertNull(session.getAttribute("user"));    */
    }


}
