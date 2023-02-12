package com.ftn.Pharmacy.Project.listeners;

import org.springframework.boot.web.servlet.ServletContextInitializer;

import com.ftn.Pharmacy.Project.controller.UserController;
import com.ftn.Pharmacy.Project.model.User;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public final class InitServletContextInitializer implements ServletContextInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println("Initialization Context...");
		servletContext.setAttribute(UserController.USER_KEY, new User());
		System.out.println("Success ServletContextInitializer!");
	}

}
