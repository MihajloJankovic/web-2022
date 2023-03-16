package com.ftn.Pharmacy.Project.controller;

import java.io.IOException;
import java.io.PrintWriter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.Pharmacy.Project.model.User;
import com.ftn.Pharmacy.Project.service.implementation.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/LogInLogOut")
public class LogInLogOutController {
	
	public static final String USER_KEY = "user";
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	HttpSession session;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL;
	
	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}
	
	@GetMapping(value = "/login")
	public ModelAndView getLogin(HttpSession session, HttpServletResponse response) throws IOException{
		ModelAndView result = new ModelAndView("UserLogin");



		return result;
	}
	@GetMapping(value = "/loginwrong")
	public ModelAndView getwronglogin(HttpSession session, HttpServletResponse response) throws IOException{
		ModelAndView result = new ModelAndView("LoginWrong");



		return result;
	}
	@GetMapping(value = "/register")
	public ModelAndView getRegister(HttpSession session, HttpServletResponse response) throws IOException{
		ModelAndView result = new ModelAndView("register");



		return result;
	}
	@PostMapping(value = "/login")
	@ResponseBody
	public void  postLogin(@RequestParam(required = true) String username, @RequestParam(required = true) String password, HttpSession session,
			HttpServletResponse response) throws IOException {
		
		User loggedUser = userService.findUserByUsernameAndPassword(username, password);
		
		if(loggedUser == null) {
			response.sendRedirect(baseURL + "LogInLogOut/loginwrong");
			return;
		}



		if(loggedUser != null)
		{
			if (loggedUser.isLogged() == true)
			{
				response.sendRedirect(baseURL);
				return;
			}

			if(session.getAttribute(LogInLogOutController.USER_KEY) != null)
			{
				response.sendRedirect(baseURL);
				return;

			}



			loggedUser.setLogged(true);
			session.setAttribute(LogInLogOutController.USER_KEY, loggedUser);
			response.sendRedirect(baseURL);
			return;
		}

	}
	
	@GetMapping(value="/logout")
	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		User loggedUser = (User) req.getSession().getAttribute(LogInLogOutController.USER_KEY);

		
		if(loggedUser == null) {

			res.sendRedirect(baseURL + "/LogInLogOut/login");
			return;
		}
		



		
		req.getSession().removeAttribute(LogInLogOutController.USER_KEY);
		req.getSession().invalidate();
		res.sendRedirect(baseURL);
	}
}
