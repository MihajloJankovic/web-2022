package com.ftn.Pharmacy.Project.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.ftn.Pharmacy.Project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.Pharmacy.Project.service.implementation.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/users")
public class UserController implements ServletContextAware {

	public static final String USER_KEY = "user";
	private UserRole role;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private UserService userService;
	@Autowired
	HttpSession session;
	@Autowired
	private ServletContext servletContext;
	private String baseURL;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@GetMapping("changeLang")
	public void promeniJezik(@RequestParam(defaultValue="en") String jezik, HttpSession session,HttpServletRequest request, HttpServletResponse response) throws IOException {
		Locale lokalizacija = localeResolver.resolveLocale(request);
		if(jezik.equals("sr")) {
			lokalizacija = Locale.forLanguageTag("sr");
		} else if (jezik.equals("en")) {
			lokalizacija = Locale.ENGLISH;
		}
		session.setAttribute("loc", lokalizacija );
		response.sendRedirect(baseURL);
		return;
	}
	@PostMapping(value = "/register")
	public void register(@RequestParam(required = true) String username,
						 @RequestParam(required = true) String password, @RequestParam(required = true) String email,
						 @RequestParam(required = true) String name, @RequestParam(required = true) String surname,
						 @RequestParam(required = true) String birthDateStr, @RequestParam(required = true) String street,
						 @RequestParam(required = true) String streetNumber, @RequestParam(required = true) String city,
						 @RequestParam(required = true) String country, @RequestParam(required = true) String phoneNumber,HttpSession session, HttpServletResponse res) throws IOException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateNow = LocalDateTime.now();
		String formatDateTime = dateNow.format(formatter);
		LocalDateTime registeredTime = LocalDateTime.parse(formatDateTime, formatter);
		LocalDate birthDate = LocalDate.parse(birthDateStr);
		if(userService.username(username) != null)
		{
			res.sendRedirect(baseURL + "users/registerWrong");
			return;
		}
	else{


			User userForRegister = new User(username, password, email, name, surname, birthDate, street, streetNumber, city, country, phoneNumber,UserRole.CUSTOMER, registeredTime);
			userService.save(userForRegister);

			res.sendRedirect(baseURL + "users/allUsers");
		}
	}

	@GetMapping(value = "/homepage")
	@ResponseBody
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView result = new ModelAndView("UserLogin");
		User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
		if (loggedUser == null) {

			response.sendRedirect(baseURL + "LogInLogOut/login");
			return result;


		}
		if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
		{

			response.sendRedirect(baseURL + "LogInLogOut/login");
			return result;

		}
//		if(loggedUser.getRole().toString() != "ADMINISTRATOR") {
//			response.sendRedirect(baseURL+"index");
//			return "";
//		}

		List<User> users = userService.findAll();
		ModelAndView result1 = new ModelAndView("HomeUser");
		 result1.addObject("User", users);
		 result1.addObject("user",loggedUser);

		return result1;



	}

	@PostMapping(value = "/edit")
	public void edit(@RequestParam(required = true) String id,@RequestParam(required = true) String username,
						 @RequestParam(required = true) String pass, @RequestParam(required = true) String email,
						 @RequestParam(required = true) String name, @RequestParam(required = true) String surname,
						 @RequestParam(required = true) String bdate, @RequestParam(required = true) String street,
						 @RequestParam(required = true) String streetnum, @RequestParam(required = true) String city,
						 @RequestParam(required = true) String country, @RequestParam(required = true) String Phone,HttpSession session, HttpServletResponse res) throws IOException {


		LocalDate birthDate = LocalDate.parse(bdate);

			User userForedit = new User(Long.valueOf(id),username, pass, email, name, surname, birthDate, street, streetnum, city, country, Phone,UserRole.CUSTOMER,LocalDateTime.now(),false);
			userService.update(userForedit);

			res.sendRedirect(baseURL + "users/homepage");

	}
	@GetMapping(value = "/details")
	@ResponseBody
	public ModelAndView Details(HttpServletRequest request, HttpServletResponse response,Integer id) throws IOException {
		ModelAndView result = new ModelAndView("UserLogin");
		User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
		if (loggedUser == null) {

			response.sendRedirect(baseURL + "LogInLogOut/login");
			return result;


		}
		if(loggedUser.getRole() != UserRole.ADMINISTRATOR)
		{

			response.sendRedirect(baseURL + "LogInLogOut/login");
			return result;

		}
//		if(loggedUser.getRole().toString() != "ADMINISTRATOR") {
//			response.sendRedirect(baseURL+"index");
//			return "";
//		}

		User users = userService.findOne(Long.valueOf(id));
		ModelAndView result1 = new ModelAndView("UserDetails");
		result1.addObject("User", users);
		result1.addObject("user",loggedUser);
		result1.addObject("kat",UserRole.values());

		return result1;



	}

	@GetMapping(value = "allUsers")
	@ResponseBody
	public String getUsers(HttpSession session, HttpServletResponse response) throws IOException {

		User loggedUser = (User) session.getAttribute(LogInLogOutController.USER_KEY);
		if (loggedUser == null) {
			response.sendRedirect(baseURL+"LogInLogOut/login");

		}


		String pera = "test";
		List<User> users = userService.findAll();
		return pera;
	}
	@GetMapping(value = "registerWrong")
	@ResponseBody
	public ModelAndView registerWrong(HttpSession session, HttpServletResponse response) throws IOException {

		ModelAndView result1 = new ModelAndView("RegisterWrong");


		return result1;
	}
	@PostMapping(value="/delete")
	public void delete(Long id, HttpServletResponse response) throws IOException {
		User deleted = userService.findOne(id);
		if( deleted.isDeleted() == false)
		{
			userService.delete(deleted.getUserID());
		}
		else {
			userService.activate(deleted.getUserID());
		}
		response.sendRedirect(baseURL+"users/homepage");
	}
}

