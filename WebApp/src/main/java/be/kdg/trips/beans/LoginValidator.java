package be.kdg.trips.beans;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@Component
public class LoginValidator implements Validator {

    @Autowired
    private TripsService tripsService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == LoginBean.class;
    }

    @Override
    public void validate(Object o, Errors errors) {

        LoginBean loginBean = (LoginBean) o;
        ValidationUtils.rejectIfEmpty(errors, "email", "NotEmpty.user.email");
        ValidationUtils.rejectIfEmpty(errors, "password", "NotEmpty.user.password");
        if (!errors.hasErrors()) {
            try {
                if (!tripsService.checkLogin(loginBean.getEmail(), loginBean.getPassword())) {
                    errors.rejectValue("email", "WrongLogin.user");
                }
            } catch (TripsException e) {
                errors.rejectValue("email", "WrongLogin.user");
            }
        }
    }

}
