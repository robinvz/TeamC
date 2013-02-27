package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.ConstraintViolationException;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestUser {
    private static TripsService tripsService;

    @BeforeClass
    public static void createTripManager(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
    }

    @Test
    public void successfulRegister() throws TripsException
    {
        User testUser = new User("gijs.muys@student.kdg.be", "pazw#rd");
        User user = tripsService.createUser(testUser);
        assertNotNull(user);
    }

    @Test(expected = TripsException.class)
    public void failedRegisterExistingUser() throws TripsException {
        User testUser = new User("keke.kokelenberg@student.kdg.be", "password");
        User user1 = tripsService.createUser(testUser);
        User user2 = tripsService.createUser(testUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidEmail() throws TripsException
    {
        User user = tripsService.createUser(new User("jos", "password"));
    }


    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidPassword() throws TripsException
    {
        User user = tripsService.createUser(new User("robin.vanzype@student.kdg.be", "x"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidHouseNr1() throws TripsException
    {
        User user = new User("lol@hotmail.com","testest");
        user.getAddress().setHouseNr("aa");
        tripsService.createUser(user);
    }

    @Test
    public void succesfulRegisterHouseNr() throws TripsException
    {
        User user = new User("lol@hotmail.com","testest");
        user.getAddress().setHouseNr("1a");
        tripsService.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidHouseNr() throws TripsException
    {
        User user = new User("lol@hotmail.com","testest");
        user.getAddress().setHouseNr("aa");
        tripsService.createUser(user);
    }

    @Test
    public void successfulFindUser() throws TripsException {
        User createdUser = tripsService.createUser(new User("mathias.vandepol@student.kdg.be", "password"));
        User foundUser = tripsService.findUser("mathias.vandepol@student.kdg.be");
        assertEquals(createdUser, foundUser);
    }

    @Test(expected = TripsException.class)
    public void failedFindUser() throws TripsException {
        tripsService.findUser("mathias");
    }

    @Test
    public void successfulUserUpdateNewValues() throws TripsException
    {
        User user = tripsService.createUser(new User("tony.mertens@student.kdg.be","password"));
        tripsService.updateUser(user, "hans", "martens", "beerstraat", "11", "Antwerpen", "2000", "Antwerpen","België",null);
        assertEquals("hans", tripsService.findUser("tony.mertens@student.kdg.be").getFirstName());
    }

    @Test
    public void successfulUserUpdateNullValues() throws TripsException
    {
        User user = tripsService.createUser(new User("tony.martens@student.kdg.be","password"));
        tripsService.updateUser(user, "", "", "", "","", "", "", "", null);
        assertEquals(tripsService.findUser("tony.martens@student.kdg.be").getFirstName(), null);
    }

    @Test
    public void successfulUserUpdateProfilePicture() throws TripsException
    {
        User user = tripsService.createUser(new User("gaston.leo@student.kdg.be","password"));
        File file = new File("src/test/resources/testimage.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripsService.updateUser(user, "", "", "", "","", "", "", "", bFile);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedUserUpdateInvalidStreet() throws TripsException {
        User user = tripsService.createUser(new User("louis.martens@student.kdg.be","password"));
        tripsService.updateUser(user, "","","straat1212","","","","","", null);
    }

    @Test(expected = TripsException.class)
    public void successfulDeleteUser() throws TripsException {
        User user = tripsService.createUser(new User("email@hotmail.com","password"));
        tripsService.deleteUser(user);
        tripsService.findUser("email@hotmail.com");
    }
      /*
      @Test(expected = TripsException.class)
      public void failedDeleteUser() throws TripsException
      {

      }
      */

    @Test
    public void successfulLogin() throws TripsException {
        tripsService.createUser(new User("joris@student.kdg.be", "password"));
        assertTrue(tripsService.checkLogin("joris@student.kdg.be", "password"));
    }

    @Test
    public void failedLoginInvalidPassword() throws TripsException {
        tripsService.createUser(new User("joel@student.kdg.be", "password"));
        assertFalse(tripsService.checkLogin("joel@student.kdg.be", "joel"));
    }

    @Test
    public void failedLoginInvalidUsername() throws TripsException {
        tripsService.createUser(new User("peter@gmail.com", "password"));
        assertFalse(tripsService.checkLogin("eddy@gmail.com", "password"));
    }

    @Test
    public void successfulPasswordChange() throws TripsException {
        User user = tripsService.createUser(new User("rené@student.kdg.be","goethals"));
        tripsService.changePassword(user,"goethals","newpw");
        assertTrue(tripsService.findUser("rené@student.kdg.be").checkPassword("newpw"));
    }

    @Test(expected = TripsException.class)
    public void failedPasswordChangeWrongOldPassword() throws TripsException {
        User user = tripsService.createUser(new User("zaag@student.kdg.be","tony"));
        tripsService.changePassword(user,"goethals","newpw");
    }

}
