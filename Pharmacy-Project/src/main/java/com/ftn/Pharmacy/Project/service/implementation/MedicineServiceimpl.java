package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.model.Medicine;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MedicineServiceimpl {
    @Autowired
    private MedicineDao medicineDao;


    public Medicine findOneMedicineByID(Long id) {
        return medicineDao.findOne(id);
    }


    public List<Medicine> findAllMedicine() {
        return medicineDao.findAll();
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
