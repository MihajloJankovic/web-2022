package com.ftn.Pharmacy.Project.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.ftn.Pharmacy.Project.model.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.Pharmacy.Project.model.User;
import com.ftn.Pharmacy.Project.model.UserRole;
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


		User userForRegister = new User(username, password, email, name, surname, birthDate, street, streetNumber, city, country, phoneNumber,UserRole.CUSTOMER, registeredTime);
		userService.save(userForRegister);

		res.sendRedirect(baseURL + "users/allUsers");
	}

	@GetMapping(value = "/homepage")
	@ResponseBody
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {

		User loggedUser = (User) request.getSession().getAttribute(LogInLogOutController.USER_KEY);
		if (loggedUser == null) {
			response.sendRedirect(baseURL + "LogInLogOut/login");

		}
//		if(loggedUser.getRole().toString() != "ADMINISTRATOR") {
//			response.sendRedirect(baseURL+"index");
//			return "";
//		}

		List<User> users = userService.findAll();
		ModelAndView result = new ModelAndView("HomeUser");
		 result.addObject("User", users);

		return result;



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
}

