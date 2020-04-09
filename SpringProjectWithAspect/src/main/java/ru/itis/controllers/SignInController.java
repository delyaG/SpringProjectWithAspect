package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.User;
import ru.itis.services.SignInService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class SignInController {

    @Autowired
    private SignInService signInService;

    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    private ModelAndView getSignInPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signIn");
        return modelAndView;
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    private void signIn(HttpServletResponse resp, HttpServletRequest req) {
        Optional<User> user = signInService.signIn(req.getParameter("mail"), req.getParameter("password"));
        if (user.isPresent()) {
            req.getSession().setAttribute("auth", user.get().getId());
        } else {
            try {
                System.out.println("cannot find user");
                resp.sendRedirect("/signUp");
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
