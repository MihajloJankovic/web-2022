package com.ftn.Pharmacy.Project.service;

import java.util.List;

import com.ftn.Pharmacy.Project.model.User;

public interface IUserService {
	public User findOne(Long id);

	public List<User> findAll();
	public User findUserByUsernameAndPassword(String username,String Password);

	public int save(User user);

	public int update(User user);

	public int delete(Long id);
}

