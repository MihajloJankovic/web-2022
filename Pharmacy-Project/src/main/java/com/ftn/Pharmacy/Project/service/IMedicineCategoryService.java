package com.ftn.Pharmacy.Project.service;

import com.ftn.Pharmacy.Project.model.MedicineCategory;

import java.util.List;

public interface IMedicineCategoryService {
    public MedicineCategory findOneMedicineCategoryByID(Long id);
    public List<MedicineCategory> findAllMedicineCategories();
    public MedicineCategory saveMedicineCategory(MedicineCategory mc);
    public MedicineCategory updateMedicineCategory(MedicineCategory mc);
    public MedicineCategory deleteMedicineCategory(Long id);
    public MedicineCategory findOneMedicineCategoryByName(String name);
    public List<MedicineCategory> findAllUNDELETED();
    public int activate(Long id);

}
