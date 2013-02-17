package be.kdg.trips;

import org.junit.Before;
import org.junit.Test;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class UserTest {
    String email;
    String password;

    @Before
    public void initTest(){
        email = "mathias.vandepol@student.user.be";
    }

    @Test
    public void loginNotNullTest(){
        //User user = userService.getUserByEmail(email);
        //assertNotNull(user);
    }

    @Test
    public void loginUserCorrect(){
        //User user = userService.getUserByEmail(email);
        //assertEquals(user.email, email);
    }

    //CRUD testen in model?
    /*
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
        //userService.deleteUser(user);
        assertNull(session.getAttribute("user"));
    }
    */

}
