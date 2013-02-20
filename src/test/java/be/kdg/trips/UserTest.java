package be.kdg.trips;

import be.kdg.trips.controllers.LoginController;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class UserTest {

    private MockMvc mockMvc;


    @Autowired
    private HttpSession session;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build();
    }

    @Test
    public void userLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login?username=bob&password=bob");
        this.mockMvc.perform(requestBuilder);
        assertNotNull(session.getAttribute("user"));
    }

    @Test
    public void loginUserCorrect(){
        //User user = userService.getUserByEmail(email);
        //assertEquals(user.email, email);
    }

    @Test
    public void changePasswordTest(){
     /*   User user = (User) session.getAttribute("user");
        String newPassword = "nieuw";

        session.setAttribute("user", user);
        assertEquals(session.getAttribute("user"), "nieuw");  */
    }

    @Test
    public void deleteUserTest(){
     /*   User user = (User) session.getAttribute("user");

        assertNull(session.getAttribute("user"));    */
    }


}
