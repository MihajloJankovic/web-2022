package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.model.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineServiceimpl {
    @Autowired
    public MedicineDao medicineDao;


    public Medicine findOneMedicineByID(Long id) {
        return medicineDao.findOne(id);
    }


    public List<Medicine> findAllMedicine() {
        return medicineDao.findAll();
    }

    public Medicine findOneByName(String name)
    {
        return  medicineDao.findOneByName(name);
    }
    public Medicine saveMedicine(Medicine mc) {
        medicineDao.save(mc);
        return mc;
    }


    public Medicine updateMedicine(Medicine mc) {
        medicineDao.update(mc);
        return mc;
    }


    public Medicine deleteMedicine(Long id) {
        Medicine medicine = medicineDao.findOne(id);
        medicineDao.delete(id);
        return medicine;
    }
}
