package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class UserTest {
    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    String email;
    String password;

    @Before
    public void initTest(){
        email = "mathias.vandepol@student.user.be";
        password = "mathias";
        try {
            tripsService.createUser(email, password);
        } catch (TripsException e) {
            //Failed to create user
        }
    }

    @Test
    public void userLoggedIn(){
        User user = null;
        try {
            user = tripsService.findUser(email);
            tripsService.checkLogin(email, password);
            session.setAttribute("user", user);
        } catch (TripsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assertNotNull(session.getAttribute("user"));
    }

    @Test
    public void loginUserCorrect(){
        //User user = userService.getUserByEmail(email);
        //assertEquals(user.email, email);
    }

    @Test
    public void changePasswordTest(){
        User user = (User) session.getAttribute("user");
        String newPassword = "nieuw";
        try {
            tripsService.changePassword(user, password, newPassword);
        } catch (TripsException e) {
            //Change password failed
        }
        session.setAttribute("user", user);
        assertEquals(session.getAttribute("user"), "nieuw");
    }

    @Test
    public void deleteUserTest(){
        User user = (User) session.getAttribute("user");
        try {
            tripsService.deleteUser(user);
        } catch (TripsException e) {
            //Delete user failed
        }
        assertNull(session.getAttribute("user"));
    }


}
