package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.IgradeDao;
import com.ftn.Pharmacy.Project.dao.impl.ManDao;
import com.ftn.Pharmacy.Project.dao.impl.gradeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class gradeservice {
    @Autowired
    private gradeDAO dao;
    public int save(Long id,int gradee)
    {
        return dao.save(id,gradee);
    }
}
