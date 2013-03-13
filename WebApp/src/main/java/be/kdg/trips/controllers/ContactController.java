package be.kdg.trips.controllers;

import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.mail.MessagingException;

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
    private TripsService tripsService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String contact() {
        return "contactView";
    }

    @RequestMapping(value = "/sendContactMail", method = RequestMethod.POST)
    public String sendContactMail(@RequestParam String email, @RequestParam String type, @RequestParam String message) {
        try {
            tripsService.sendContactMail(type, message, email);
        } catch (MessagingException e) {
            //error occurred
        }
        return "indexView";
    }

}
