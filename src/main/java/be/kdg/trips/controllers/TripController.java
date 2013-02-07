package be.kdg.trips.controllers;

import be.kdg.trips.model.Trip;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import sun.awt.ModalityListener;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes
public class TripController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTrips(){
        return new ModelAndView();
    }

    @RequestMapping(value = "/selectTrip", method = RequestMethod.POST)
    public String selectTrip(@ModelAttribute("trip") Trip trip, BindingResult result) {

        return "trip";
    }

}
