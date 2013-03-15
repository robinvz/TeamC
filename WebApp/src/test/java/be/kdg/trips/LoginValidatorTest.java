package be.kdg.trips;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.beans.LoginValidator;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class LoginValidatorTest {
    private Validator validator;


    @Mock
    TripsServiceImpl tripsService;

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        validator = new LoginValidator();
        ReflectionTestUtils.setField(validator, "tripsService", tripsService);
        mockMvc = MockMvcBuilders.standaloneSetup(validator).build();
    }

    @Test
    public void supports() {
        assertTrue(validator.supports(LoginBean.class));
        assertFalse(validator.supports(Object.class));

    }

    @Test
    public void emailNull() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(null); // Already null, but only to be explicit here...
        loginBean.setPassword("Whatever");
        BindException errors = new BindException(loginBean, "email");
        ValidationUtils.invokeValidator(validator, loginBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("email"));
        assertEquals("NotEmpty.user.email", errors.getFieldError("email").getCode());
    }

    @Test
    public void passwordNull() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("Whatever"); // Already null, but only to be explicit here...
        loginBean.setPassword(null);
        BindException errors = new BindException(loginBean, "password");
        ValidationUtils.invokeValidator(validator, loginBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("password"));
        assertEquals("NotEmpty.user.password", errors.getFieldError("password").getCode());
    }

    @Test
    public void passwordWrong() throws TripsException {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("Whatever"); // Already null, but only to be explicit here...
        loginBean.setPassword("HelloPassword");
        BindException errors = new BindException(loginBean, "loginBean");
        ValidationUtils.invokeValidator(validator, loginBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("email"));
        when(tripsService.checkLogin("Whatever", "Jo")).thenReturn(false);
        assertEquals("WrongLogin.user", errors.getFieldError("email").getCode());
    }

    @Test
    public void emailWrong() throws TripsException {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("Whatever"); // Already null, but only to be explicit here...
        loginBean.setPassword("HelloPassword");
        BindException errors = new BindException(loginBean, "loginBean");
        ValidationUtils.invokeValidator(validator, loginBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getFieldErrorCount("email"));
        when(tripsService.checkLogin("Whatever", "HelloPassword")).thenThrow(new TripsException("User does not exist"));
        assertEquals("WrongLogin.user", errors.getFieldError("email").getCode());
    }

}
