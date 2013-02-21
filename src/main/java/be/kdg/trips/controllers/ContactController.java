package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ContactController {

    @RequestMapping(value="/contact", method=RequestMethod.GET)
    public String contact() {
        return "contactView";
    }
}
