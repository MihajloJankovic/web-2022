package com.ftn.Pharmacy.Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.ftn.Pharmacy.Project.service.implementation.UserService;


@SpringBootApplication
@ComponentScan(basePackages= {"com.ftn.Pharmacy.Project"})
public class PharmacyProjectApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PharmacyProjectApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(PharmacyProjectApplication.class, args);
		
		
	}

}
