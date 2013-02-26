package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.beans.LoginValidator;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
//LoginController
public class LoginController {

    @Autowired LoginValidator loginValidator;

    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    }

    @InitBinder()
    protected void loginBinder( WebDataBinder binder) throws Exception {
        if (binder.getTarget() instanceof LoginBean) {
            binder.setValidator(loginValidator);

        }
    }

    @Autowired
    private TripsService tripsService;

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("loginView", "loginBean", new LoginBean());
    }

    @RequestMapping(value = "/service/login", method = RequestMethod.GET)
    public
    @ResponseBody
    String loginService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        return js.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@Valid LoginBean loginBean, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()){
            return "loginView";
        }
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            if (tripsService.checkLogin(email, password)) {  //wrong password, redirect to index
                User user = tripsService.findUser(email);
                session.setAttribute("user", user);
            } else {
                ObjectError error = new ObjectError("login", "Invalid login credentials.");
                result.addError(error);
                return "loginView";
            }
        } catch (TripsException e) {
            //will never throw
        }
        return "indexView";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        return "indexView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(User user) {
        session.invalidate();
        return "registerView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User validUser, BindingResult result, HttpServletRequest request) {
        String pass = request.getParameter("password");
        if (result.hasErrors()) {
            return "registerView";
        }
        if (request.getParameter("password").isEmpty()) {
            ObjectError objectError = new ObjectError("password", "Password is empty");
            result.addError(objectError);
            return "registerView";
        }
        try {
            validUser.setPassword(request.getParameter("password"));
            User user = tripsService.createUser(validUser);
            session.setAttribute("user", user);
            return "indexView";
        } catch (TripsException e) {
            ObjectError error = new ObjectError("email", "An account already exists for this email.");
            result.addError(error);
            return "registerView";
        }
    }


}
