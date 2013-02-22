package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.ConstraintViolationException;

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
        User user = tripsService.createUser("jos", "password");
    }


    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidPassword() throws TripsException
    {
        User user = tripsService.createUser("robin.vanzype@student.kdg.be", "x");
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
        User createdUser = tripsService.createUser("mathias.vandepol@student.kdg.be", "password");
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
        User user = tripsService.createUser("tony.mertens@student.kdg.be","password");
        tripsService.updateUser(user, "hans", "martens", "beerstraat", "11", "Antwerpen", "2000", "Antwerpen","België");
        assertEquals(tripsService.findUser("tony.mertens@student.kdg.be").getFirstName(), "hans");
    }

    @Test
    public void successfulUserUpdateNullValues() throws TripsException
    {
        User user = tripsService.createUser("tony.martens@student.kdg.be","password");
        tripsService.updateUser(user, null, null, null, null, null, null, null, null);
        assertEquals(tripsService.findUser("tony.martens@student.kdg.be").getFirstName(), null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedUserUpdateInvalidStreet() throws TripsException {
        User user = tripsService.createUser("louis.martens@student.kdg.be","password");
        tripsService.updateUser(user, null,null,"straat1212",null,null,null,null,null);
    }

    @Test(expected = TripsException.class)
    public void successfulDeleteUser() throws TripsException {
        User user = tripsService.createUser("email@hotmail.com","password");
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
        tripsService.createUser("joris@student.kdg.be", "password");
        assertTrue(tripsService.checkLogin("joris@student.kdg.be", "password"));
    }

    @Test
    public void failedLoginInvalidPassword() throws TripsException {
        tripsService.createUser("joel@student.kdg.be", "password");
        assertFalse(tripsService.checkLogin("joel@student.kdg.be", "joel"));
    }

    @Test
    public void failedLoginInvalidUsername() throws TripsException {
        tripsService.createUser("peter@gmail.com", "password");
        assertFalse(tripsService.checkLogin("eddy@gmail.com", "password"));
    }

    @Test
    public void successfulPasswordChange() throws TripsException {
        User user = tripsService.createUser("rené@student.kdg.be","goethals");
        tripsService.changePassword(user,"goethals","newpw");
        assertTrue(tripsService.findUser("rené@student.kdg.be").checkPassword("newpw"));
    }

    @Test(expected = TripsException.class)
    public void failedPasswordChangeWrongOldPassword() throws TripsException {
        User user = tripsService.createUser("zaag@student.kdg.be","tony");
        tripsService.changePassword(user,"goethals","newpw");
    }

}
