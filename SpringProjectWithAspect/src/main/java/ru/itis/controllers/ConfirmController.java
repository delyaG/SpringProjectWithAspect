package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.services.SignUpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ConfirmController {

    @Autowired
    private SignUpService signUpService;

    @RequestMapping(value = "/confirm/{token}", method = RequestMethod.GET)
    private ModelAndView setConfirmation(@PathVariable String token, HttpServletResponse resp) {
        signUpService.setConfirmation(token);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("confirmedPage");
        return modelAndView;
    }
}
