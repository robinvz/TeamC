package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes
public class ProfileController {

    @RequestMapping(method = RequestMethod.GET)
        public ModelAndView showProfile(){
            return new ModelAndView();
        }

}
