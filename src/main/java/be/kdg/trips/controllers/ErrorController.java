package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 17/02/13
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ErrorController {
    @RequestMapping(value = "/errors/loginError", method = RequestMethod.GET)
    public String showError() {
        return "/errors/loginError";
    }

}
