package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.beans.LoginValidator;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONObject;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;


/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
public class LoginController {
    @Autowired
    LoginValidator loginValidator;

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @InitBinder()
    protected void loginBinder(WebDataBinder binder) throws Exception {
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

    @RequestMapping(value = "/service/login", method = RequestMethod.POST)
    public
    @ResponseBody
    String loginService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        return js.toString();
    }

    @RequestMapping(value = "/facebooklogin", method = RequestMethod.POST)
    @ResponseBody
    public String facebookLogin(@RequestParam String username, @RequestParam String password) {
        User user = new User(username, password);
        JSONObject js = new JSONObject();
        try {
            tripsService.createUser(user);   //User never logged in so create a new user
        } catch (Exception e) {
            //User exists in database so just login
        }
        try {
            if (tripsService.checkLogin(user.getEmail(), password)) {
                user = tripsService.findUser(user.getEmail());
                session.setAttribute("user", user);
                js.accumulate("valid", tripsService.checkLogin(user.getEmail(), password));
            }
        } catch (TripsException e) {
            js.accumulate("valid", false);
            return js.toString();  //email exists in database but with other password
        }
        return js.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@Valid LoginBean loginBean, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "loginView";
        }
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            if (tripsService.checkLogin(email, password)) {  //wrong password, redirect to index
                session.setAttribute("user", tripsService.findUser(email));
            } else {
                ObjectError error = new ObjectError("login", "Invalid login credentials.");
                result.addError(error);
                return "loginView";
            }
        } catch (TripsException e) {
            return "redirect:/";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/retrievePassword", method = RequestMethod.GET)
    public String forgotPassword()
    {
        return "retrievePasswordView";
    }

    @RequestMapping(value = "/retrievePassword", method = RequestMethod.POST)
    public ModelAndView retrievePassword(HttpServletRequest req,
                                         @RequestParam String email, @RequestParam("recaptcha_challenge_field") String challenge,
                                         @RequestParam("recaptcha_response_field") String response, Locale locale)
    {
        String remoteAddr = req.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LfJ2N4SAAAAAEk4MsvrkJp3V6_5RiJWhTM5bCso");

        if(response==null)
        {
            return new ModelAndView("retrievePasswordView", "error", messageSource.getMessage("CaptchaError",null, locale));
        }

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);

        if (reCaptchaResponse.isValid())
        {
            if(email!=null)
            {
                try{
                    tripsService.forgotPassword(email);
                } catch (TripsException e) {
                    //User doesn't exist - Don't show!
                } catch (MessagingException e) {
                    return new ModelAndView("retrievePasswordView", "error", messageSource.getMessage("MessageError", null, locale));
                }
            }
            else
            {
                return new ModelAndView("retrievePasswordView", "error", messageSource.getMessage("EmptyEmail", null, locale));
            }
        }
        else
        {
            return new ModelAndView("retrievePasswordView", "error", messageSource.getMessage("CaptchaError",null, locale));
        }
        ModelAndView mav = new ModelAndView("loginView", "loginBean", new LoginBean());
        mav.addObject("success", messageSource.getMessage("MailSent", null, locale));
        return mav;
    }

    @RequestMapping(value = "/login/{tripId}", method = RequestMethod.POST)
    public String handleLoginRedirect(@Valid LoginBean loginBean, @PathVariable int tripId, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
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
                return "redirect:/login#"+tripId;
            }
        } catch (TripsException e) {
            return "redirect:/login#"+tripId;
        }
        return "redirect:/trip/"+tripId;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(User newUser) {
        session.invalidate();
        return "registerView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User validUser, BindingResult result, HttpServletRequest request) {
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
