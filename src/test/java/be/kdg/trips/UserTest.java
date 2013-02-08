package be.kdg.trips;

import be.kdg.trips.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class UserTest {
    @Autowired
    HttpSession session;
    String email;
    String password;

    @Before
    public void initTest(){
        email = "mathias.vandepol@student.kdg.be";
    }

    @Test
    public void loginNotNullTest(){
        assertNotNull(session.getAttribute("user"));
    }

    @Test
    public void loginUserCorrect(){
        User user = (User) session.getAttribute("user");
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void logoutTest(){
        assertNull(session.getAttribute("user"));
    }

    @Test
    public void editLoginTest(){
        User user = (User) session.getAttribute("user");
        User modified = new User("newEmail", "newPass");
        user.editCredentials(modified);
        User userafteredit = (User) session.getAttribute("user");
        assertEquals(userafteredit.getEmail(), "newEmail");
        assertEquals(userafteredit.getPassword(), "newPass");
    }

    @Test
    public void deleteUserTest(){
        User user = (User) session.getAttribute("user");
    //    userService.deleteUser(user);
        assertNull(session.getAttribute("user"));
    }






}
