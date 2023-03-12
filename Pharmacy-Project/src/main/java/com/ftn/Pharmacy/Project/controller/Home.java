package com.ftn.Pharmacy.Project.controller;

import com.ftn.Pharmacy.Project.model.User;
import com.ftn.Pharmacy.Project.model.UserRole;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class Home {
    private String baseURL;
    @Autowired
    private ServletContext servletContext;
    public void init() {
        baseURL = servletContext.getContextPath() + "/";

    }
    @GetMapping
    public ModelAndView getLogin(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);

        ModelAndView result = new ModelAndView("index");

            result.addObject("log",loggedUser);






        return result;
    }
}
