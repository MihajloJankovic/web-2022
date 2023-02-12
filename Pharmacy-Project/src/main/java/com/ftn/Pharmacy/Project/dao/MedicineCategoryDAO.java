package com.ftn.Pharmacy.Project.dao;

import java.util.List;

import com.ftn.Pharmacy.Project.model.MedicineCategory;

public interface MedicineCategoryDAO {
	
	public MedicineCategory findOne(Long id);
	public List<MedicineCategory> findAll();
	public int save(MedicineCategory medicineCategory);
	public int update(MedicineCategory medicineCategory);
	public int delete(Long id);

}

