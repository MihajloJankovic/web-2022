package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.dao.impl.ManDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class Manucfecturere {


    @Autowired
    private ManDao man;

    public Manucfecturer findOne(Long id) {
        return man.findOne(id);
    }


    public List<Manucfecturer> findAll() {
        return man.findAll();
    }
    public Manucfecturer findOneByName(String na)
    {
        return man.findOneByName(na);
    }
}

