package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.services.SignUpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    private ModelAndView getSignUpPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signUp");
        return modelAndView;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    private void signUp(HttpServletResponse resp, HttpServletRequest req) {
        signUpService.signUp(req.getParameter("name"), req.getParameter("mail"), req.getParameter("password"));
    }
}
