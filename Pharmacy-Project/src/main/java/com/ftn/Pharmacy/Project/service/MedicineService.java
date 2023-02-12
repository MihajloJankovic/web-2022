package com.ftn.Pharmacy.Project.service;

import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.MedicineCategory;

import java.util.List;

public interface MedicineService {
    public Medicine findOneMedicineByID(Long id);
    public List<Medicine> findAllMedicine();
    public Medicine saveMedicine(Medicine mc);
    public Medicine updateMedicine(Medicine mc);
    public void Medicinedelete(Long id);

}
