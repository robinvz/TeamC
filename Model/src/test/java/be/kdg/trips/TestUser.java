package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionSystemException;

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
    public void successfulSetAndGetEmail()
    {
        User user = new User("k@gmail.com", "password");
        user.setEmail("k@gmail.com");
        assertEquals("k@gmail.com", user.getEmail());
    }

    @Test
    public void successfulSetAndGetFirstName()
    {
        User user = new User("ka@gmail.com", "password");
        user.setFirstName("Test");
        assertEquals("Test", user.getFirstName());
    }

    @Test
    public void successfulSetAndGetLastName()
    {
        User user = new User("kz@gmail.com", "password");
        user.setLastName("Test");
        assertEquals("Test", user.getLastName());
    }

    @Test
    public void successfulSetAndGetStreet()
    {
        User user = new User("ke@gmail.com", "password");
        user.getAddress().setStreet("Test");
        assertEquals("Test", user.getAddress().getStreet());
    }

    @Test
    public void successfulSetAndGetHouseNr()
    {
        User user = new User("kr@gmail.com", "password");
        user.getAddress().setHouseNr("1");
        assertEquals("1", user.getAddress().getHouseNr());
    }

    @Test
    public void successfulSetAndGetPostalCode()
    {
        User user = new User("kt@gmail.com", "password");
        user.getAddress().setPostalCode("2000");
        assertEquals("2000", user.getAddress().getPostalCode());
    }

    @Test
    public void successfulSetAndGetCity()
    {
        User user = new User("ky@gmail.com", "password");
        user.getAddress().setCity("Antwerpen");
        assertEquals("Antwerpen", user.getAddress().getCity());
    }

    @Test
    public void successfulSetAndGetCountry()
    {
        User user = new User("ki@gmail.com", "password");
        user.getAddress().setCountry("Test");
        assertEquals("Test", user.getAddress().getCountry());
    }

    @Test
    public void successfulRegister() throws TripsException
    {
        User user = new User("peter_luts@student.kdg.be", "pazw#rd");
        tripsService.createUser(user);
        assertEquals(user, tripsService.findUser("peter_luts@student.kdg.be"));
    }

    @Test(expected = TripsException.class)
    public void failedRegisterExistingUser() throws TripsException {
        User user = new User("keke.kokelenberg@student.kdg.be", "password");
        tripsService.createUser(user);
        tripsService.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidEmail() throws TripsException
    {
       tripsService.createUser(new User("jos", "password"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterEmptyEmail() throws TripsException
    {
        tripsService.createUser(new User("", "password"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidPassword() throws TripsException
    {
        tripsService.createUser(new User("robin.vanzype@student.kdg.be", "x"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterEmptyPassword() throws TripsException
    {
        tripsService.createUser(new User("robin.vanzypea@student.kdg.be", ""));
    }

    @Test
    public void successfulRegisterAllValues() throws TripsException
    {
        User user = new User("lola@hotmail.com","testest");
        user.setFirstName("Lola");
        user.setLastName("Van Der Kempen");
        user.getAddress().setStreet("Lolastraat");
        user.getAddress().setHouseNr("1a");
        user.getAddress().setPostalCode("2000");
        user.getAddress().setCity("Antwerpen");
        user.getAddress().setCountry("België");
        User createdUser = tripsService.createUser(user);
        User foundUser = tripsService.findUser("lola@hotmail.com");
        assertEquals(createdUser, foundUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidStreet() throws TripsException
    {
        User user = new User("lol@hotmail.com","testest");
        user.getAddress().setStreet("123");
        tripsService.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidHouseNr() throws TripsException
    {
        User user = new User("lolo@hotmail.com","testest");
        user.getAddress().setHouseNr("aa");
        tripsService.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidCity() throws TripsException
    {
        User user = new User("lolu@hotmail.com","testest");
        user.getAddress().setCity("123");
        tripsService.createUser(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failedRegisterInvalidCountry() throws TripsException
    {
        User user = new User("lolz@hotmail.com","testest");
        user.getAddress().setCountry("123");
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
    public void successfulFindUsersByKeyword() throws TripsException {
        User loggedInUser = tripsService.createUser(new User("gino.frank@docent.kdg.be", "password"));
        tripsService.createUser(new User("mathias.vandepol@docent.kdg.be", "password"));
        User createdUser2 = tripsService.createUser(new User("lore.coppens@student.kdg.be", "password"));
        tripsService.updateUser(createdUser2, "docent", "", "", "", "", "", "", null);
        User createdUser3 = tripsService.createUser(new User("lotte.verezen@student.kdg.be", "password"));
        tripsService.updateUser(createdUser3, "", "docent", "", "", "", "", "", null);
        assertEquals(3, tripsService.findUsersByKeyword("doCent", loggedInUser).size());
    }

    @Test
    public void failedFindUserByKeyword() throws TripsException
    {
        User user = tripsService.createUser(new User("gas.muys@student.kdg.be", "password"));
        assertEquals(0, tripsService.findUsersByKeyword("azerty", user).size());
    }


    @Test
    public void successfulUserUpdateNewValues() throws TripsException
    {
        User user = tripsService.createUser(new User("tony.mertens@student.kdg.be","password"));
        tripsService.updateUser(user, "hans", "martens", "beerstraat", "11", "Antwerpen", "2000","België",null);
        assertEquals("hans", tripsService.findUser("tony.mertens@student.kdg.be").getFirstName());
    }

    @Test(expected = TripsException.class)
    public void failedUserUpdateUnexistingUser() throws TripsException
    {
        tripsService.createUser(new User("ges.muys@student.kdg.be", "password"));
        tripsService.updateUser(new User("pffffft@gmail.com", "looool"), "", "", "", "", "", "", "", null);
    }

    @Test
    public void successfulUserUpdateNullValues() throws TripsException
    {
        User user = tripsService.createUser(new User("tony.martens@student.kdg.be","password"));
        tripsService.updateUser(user, "", "", "", "","", "", "", null);
        assertEquals(tripsService.findUser("tony.martens@student.kdg.be").getFirstName(), null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidFirstName() throws TripsException
    {
        User user = tripsService.createUser(new User("gus.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "123", "", "", "", "", "", "", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidLastName() throws TripsException
    {
        User user = tripsService.createUser(new User("gis.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "123", "", "", "", "", "", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidStreet() throws TripsException
    {
        User user = tripsService.createUser(new User("gos.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "", "123", "", "", "", "", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidHouseNr() throws TripsException
    {
        User user = tripsService.createUser(new User("gzs.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "", "", "aa", "", "", "", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidCity() throws TripsException
    {
        User user = tripsService.createUser(new User("grs.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "", "", "", "123", "", "", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidProvince() throws TripsException
    {
        User user = tripsService.createUser(new User("gxas.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "", "", "", "", "", "123", null);
    }

    @Test(expected = TransactionSystemException.class)
    public void failedUserUpdateInvalidCountry() throws TripsException
    {
        User user = tripsService.createUser(new User("gts.muys@student.kdg.be", "password"));
        tripsService.updateUser(user, "", "", "", "", "", "", "123", null);
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
        tripsService.updateUser(user, "", "", "", "","", "", "", bFile);
        assertNotNull(tripsService.findUser("gaston.leo@student.kdg.be").getProfilePicture());
    }

    @Test(expected = TripsException.class)
    public void failUserUpdateProfilePictureWrongContentType() throws TripsException
    {
        User user = tripsService.createUser(new User("laston.geo@student.kdg.be","password"));
        File file = new File("src/test/resources/testpdf.pdf");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripsService.updateUser(user, "", "", "", "","", "", "", bFile);
        assertNotNull(tripsService.findUser("laston.geo@student.kdg.be").getProfilePicture());
    }

    @Test(expected = TripsException.class)
    public void failUserUpdateProfilePictureSizeTooBig() throws TripsException
    {
        User user = tripsService.createUser(new User("hehe.geo@student.kdg.be","password"));
        File file = new File("src/test/resources/testimage6MB.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tripsService.updateUser(user, "", "", "", "","", "", "", bFile);
        assertNotNull(tripsService.findUser("hehe.geo@student.kdg.be").getProfilePicture());
    }

    @Test(expected = TripsException.class)
    public void successfulDeleteUser() throws TripsException
    {
        User user = tripsService.createUser(new User("email@hotmail.com","password"));
        tripsService.deleteUser(user);
        tripsService.findUser("email@hotmail.com");
    }


    @Test(expected = TripsException.class)
    public void failedDeleteUser() throws TripsException
    {
        tripsService.deleteUser(new User("broodje.aap@gmail.com", "password"));
    }


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
    public void failedPasswordChangeInvalidUser() throws TripsException {
        tripsService.createUser(new User("zeef@student.kdg.be","zeef"));
        tripsService.changePassword(new User("trisee@student.kdg.be", "trisee"),"zeef","newpw");
    }

    @Test(expected = TripsException.class)
    public void failedPasswordChangeInvalidOldPassword() throws TripsException {
        User user = tripsService.createUser(new User("zaag@student.kdg.be","tony"));
        tripsService.changePassword(user,"goethals","newpw");
    }
}
