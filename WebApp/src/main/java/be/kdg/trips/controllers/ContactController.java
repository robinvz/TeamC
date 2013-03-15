package be.kdg.trips.controllers;

import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/contact")
public class ContactController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TripsService tripsService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String contact() {
        return "contactView";
    }

    @RequestMapping(value = "/sendContactMail", method = RequestMethod.POST)
    public ModelAndView sendContactMail(@RequestParam String email, @RequestParam String type, @RequestParam String message,  Locale locale) {
        try {
            tripsService.sendContactMail(type, message, email);
        } catch (MessagingException e) {
            return new ModelAndView("contactView", "error", messageSource.getMessage("CouldNotSendMail", null, locale));
        }
        return new ModelAndView("indexView");
    }

}
