package be.kdg.trips;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.controllers.ProfileController;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 21/02/13
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileTest {
    @Mock
    private TripsService tripsService;
    @Mock
    private MessageSource messageSource;
    private MockHttpSession mockHttpSession;
    private MockMvc mockMvc;
    User testUser = new User("joel@student.kdg.be", "oldPassword");
    ProfileController pc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        pc = new ProfileController();
        ReflectionTestUtils.setField(pc, "tripsService", tripsService);
        ReflectionTestUtils.setField(pc, "session", mockHttpSession);
        ReflectionTestUtils.setField(pc, "messageSource", messageSource);
        mockMvc = MockMvcBuilders.standaloneSetup(pc).build();
    }

    @Test
    public void profileView() throws Exception {
        assertEquals(pc.showProfile(), "/users/profileView");
    }

    @Test
    public void editCredentialsView() throws Exception {
        assertEquals(pc.showEditCredentials(), "/users/editCredentialsView");
    }

    @Test
    public void editCredentialsCorrect() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editCredentials")
                .param("oldPassword", "oldPassword").param("newPassword", "newPassword");
        User userWithNewPassword = new User("joel@student.kdg.be", "newPassword");
        when(tripsService.findUser(testUser.getEmail())).thenReturn(userWithNewPassword);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        assertTrue(((User) mockHttpSession.getAttribute("user")).checkPassword("newPassword"));
    }

    @Test
    public void editCredentialsWrongPassword() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editCredentials")
                .param("oldPassword", "wrongPassword").param("newPassword", "newPassword");
        Mockito.doThrow(new TripsException("Old password incorrect")).when(tripsService).changePassword(testUser, "wrongPassword", "newPassword");
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        assertTrue(((User) mockHttpSession.getAttribute("user")).checkPassword("oldPassword"));
    }

    @Test
    public void editCredentialsNotLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editCredentials");
        Mockito.doThrow(new TripsException("Cannot edit credentials when not logged in")).when(tripsService).changePassword(testUser, "oldPw", "newPw");
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void profileEdited() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editProfile")
                .param("firstName", "robke").param("lastName", "zype").param("street", "straat").param("houseNr", "22")
                .param("city", "Brugge").param("postalCode", "3300").param("country", "Belgie");
        User userAfterEdit = new User("joel@student.kdg.be", "oldPassword");
        userAfterEdit.setFirstName("robke");
        userAfterEdit.setLastName("zype");
        userAfterEdit.setAddress(new Address("street", "22", "Brugge", "3300", "Belgie"));
        when(tripsService.findUser(testUser.getEmail())).thenReturn(userAfterEdit);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        assertEquals((mockHttpSession.getAttribute("user")), userAfterEdit);
    }

    @Test
    public void profileNotEdited() throws Exception {
        testUser.setFirstName("jan");
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editProfile")
                .param("firstName", "robke").param("lastName", "zype").param("street", "straat").param("houseNr", "22")
                .param("city", "Brugge").param("postalCode", "3300").param("country", "Belgie");
        Mockito.doThrow(new TripsException("Cannot edit unexisting user")).when(tripsService)
                .updateUser(testUser, "robke", "zype", "straat", "22", "Brugge", "3300", "Belgie", null);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        User notEditedUser = new User();
        notEditedUser.setFirstName("jan");
        assertEquals(notEditedUser.getFirstName(), testUser.getFirstName());
    }

    @Test
    public void profileNotEditedNotLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editProfile")
                .param("firstName", "robke").param("lastName", "zype").param("street", "straat").param("houseNr", "22")
                .param("city", "Brugge").param("postalCode", "3300").param("country", "Belgie");
        Mockito.doThrow(new TripsException("Cannot update user when not logged in")).when(tripsService).updateUser(testUser, "robke", "zype", "straat", "22", "Brugge", "3300", "Belgie", null);
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void userDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteProfile");
        mockMvc.perform(requestBuilder).andExpect(view().name("indexView"));
        assertNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void userNotDeleted() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteProfile");
        Mockito.doThrow(new TripsException("Cannot delete unexisting user")).when(tripsService).deleteUser(any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        assertNotNull(mockHttpSession.getAttribute("user"));
    }

    @Test
    public void userNotDeletedNotLoggedIn() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/deleteProfile");
        Mockito.doThrow(new TripsException("Not logged in")).when(tripsService).deleteUser(any(User.class));
        mockMvc.perform(requestBuilder).andExpect(view().name("loginView"));
    }

    @Test
    public void showEditProfilePic() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/editProfilePic");
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/editProfilePicView"));
    }


    @Test
    public void editProfilePic() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("Hallo", "Hallo".getBytes());
        String str = pc.editProfilePic(multipartFile) ;
        assertEquals(str, "/users/profileView");
    }

    @Test
    public void showProfilePic() throws Exception{
        mockHttpSession.setAttribute("user", testUser);
        User u = (User) mockHttpSession.getAttribute("user");
        byte[] b = new byte[1024];
        u.setProfilePicture(b);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/profilePic") ;
       // when(tripsService.findTripById(1, testUser)).thenThrow(new TripsException("Error"));
        mockMvc.perform(requestBuilder).andExpect(content().bytes(b));
    }


    /*
    @Test
    public void editProfilePic() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editProfilePic").param("picPath", "../../main/webapp/resources/res/img/dragon.png");
        when(tripsService.findUser(anyString())).thenReturn(testUser);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
    }

    @Test
    public void editProfilePicNoUserInSession() throws Exception {
        mockHttpSession.setAttribute("user", testUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/editProfilePic").param("picPath", "../../main/webapp/resources/res/img/dragon.png");
        when(tripsService.findUser(anyString())).thenThrow(new TripsException("user does not exist"));
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
    }
    */
}
