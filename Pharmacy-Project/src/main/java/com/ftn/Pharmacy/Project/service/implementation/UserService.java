package com.ftn.Pharmacy.Project.service.implementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftn.Pharmacy.Project.dao.MedicineCategoryDAO;
import com.ftn.Pharmacy.Project.dao.UserDAO;
import com.ftn.Pharmacy.Project.dao.impl.UserDAOimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.Pharmacy.Project.model.User;
import com.ftn.Pharmacy.Project.model.UserRole;
import com.ftn.Pharmacy.Project.service.IUserService;
import com.ftn.Pharmacy.Project.dao.impl.UserDAOimpl;

@Service
public class UserService implements IUserService {


	@Autowired
	private UserDAOimpl userDAOimpl;

	public User findOne(Long id)
	{
		return userDAOimpl.findOne(id);
	}
	public User findUserByUsernameAndPassword(String username,String Password)
	{
		return userDAOimpl.usernamepassword(username,Password);
	}
	public List<User> findAll()
	{
		return userDAOimpl.findAll();
	}
	public int save(User user)
	{
		return userDAOimpl.save(user);
	}
	public int update(User user)
	{
		return userDAOimpl.update(user);
	}
	public int delete(Long id)
	{
		return userDAOimpl.delete(id);
	}



}
