package com.ftn.Pharmacy.Project.service;

import com.ftn.Pharmacy.Project.dao.impl.ManDao;
import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.User;

import java.util.List;

public interface ManucfecturerServiseI {
    public Manucfecturer findOne(Long id);

    public List<Manucfecturer> findAll();
    public Manucfecturer findOnebyName(String name);
}
