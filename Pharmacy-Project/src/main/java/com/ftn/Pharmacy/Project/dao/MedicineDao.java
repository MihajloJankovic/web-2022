package com.ftn.Pharmacy.Project.dao;

import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.MedicineCategory;

import java.util.List;

public interface MedicineDao {
    public Medicine findOne(Long id);
    public List<Medicine> findAll();
    public int save(Medicine medicine);
    public int update(Medicine medicine);
    public int delete(Long id);
    public Medicine findOneByName(String name);
    public int update2(Medicine medicine);
    public List<Medicine> findAllForShop();


}

