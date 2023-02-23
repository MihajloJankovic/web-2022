package com.ftn.Pharmacy.Project.model;

public class MedicineCategory {

	private Long medicineID;
	private String medicineName;
	private String medicinePurpose;
	private String medicineDescription;
	private Boolean deleted;
	
	public MedicineCategory() {
		
		this.medicineName = "";
		this.medicinePurpose = "";
		this.medicineDescription = "";
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public MedicineCategory(String medicineName, String medicinePurpose, String medicineDescription, Boolean d) {
		
		this.medicineName = medicineName;
		this.medicinePurpose = medicinePurpose;
		this.medicineDescription = medicineDescription;
		this.deleted = d;
	}

	public MedicineCategory(Long medicineID, String medicineName, String medicinePurpose, String medicineDescription,Boolean d) {

		this.medicineID = medicineID;
		this.medicineName = medicineName;
		this.medicinePurpose = medicinePurpose;
		this.medicineDescription = medicineDescription;
		this.deleted = d;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getMedicinePurpose() {
		return medicinePurpose;
	}

	public void setMedicinePurpose(String medicinePurpose) {
		this.medicinePurpose = medicinePurpose;
	}

	public String getMedicineDescription() {
		return medicineDescription;
	}

	public void setMedicineDescription(String medicineDescription) {
		this.medicineDescription = medicineDescription;
	}

	public Long getMedicineID() {
		return medicineID;
	}

	public void setMedicineID(Long medicineID) {
		this.medicineID = medicineID;
	}

	@Override
	public String toString() {
		return this.getMedicineName() + "|" + this.getMedicinePurpose() + "|" + this.getMedicineDescription();
	}
}
