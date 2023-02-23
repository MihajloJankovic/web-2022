package com.ftn.Pharmacy.Project.dao;

import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.model.User;

import java.util.List;

public interface UserDAO {
    public User findOne(Long id);
    public List<User> findAll();
    public int save(User user);
    public int update(User user);
    public int delete(Long id);
    public User username(String username);
    public User usernamepassword(String username,String password);

}

