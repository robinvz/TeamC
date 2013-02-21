package be.kdg.trips;

import be.kdg.trips.controllers.ProfileController;
import be.kdg.trips.exception.TripsException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
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

    private MockHttpSession mockHttpSession;

    private MockMvc mockMvc;

    ProfileController pc;

    User testUser = new User("joel@student.kdg.be", "oldPassword");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockHttpSession = new MockHttpSession(null);
        pc = new ProfileController();
        ReflectionTestUtils.setField(pc, "tripsService", tripsService);
        ReflectionTestUtils.setField(pc, "session", mockHttpSession);
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
        User userWithSamePassword = new User("joel@student.kdg.be", "oldPassword");
        when(tripsService.findUser(testUser.getEmail())).thenReturn(userWithSamePassword);
        mockMvc.perform(requestBuilder).andExpect(view().name("/users/profileView"));
        assertTrue(((User) mockHttpSession.getAttribute("user")).checkPassword("oldPassword"));
    }

    @Test
    public void userDeleted(){

    }

}
