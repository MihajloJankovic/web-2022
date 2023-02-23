package com.ftn.Pharmacy.Project.model;

public class Medicine {
    private Long id;
    private String Name;
    private String  Description ;
    private String Contraindications;
    private Type type;
    private double grade;
    private MedicineCategory medicineCategory;

    private int NumberofItems;
    private int price;
    Manucfecturer manufecturer;

    public Medicine(Long id, String name, String description, String contraindications, Type type, double grade, MedicineCategory medicineCategory, int numberofItems, int price, Manucfecturer manufecturer) {
        this.id = id;
        Name = name;
        Description = description;
        Contraindications = contraindications;
        this.type = type;
        this.grade = grade;
        this.medicineCategory = medicineCategory;
        NumberofItems = numberofItems;
        this.price = price;
        this.manufecturer = manufecturer;
    }

    public Medicine() {

    }

    public Medicine(long id, String medicineName, String description, String contraindications, Type valueOf, MedicineCategory oneMedicineCategoryByID, int numberofItems, int price, Manucfecturer man) {
        this.id = id;
        Name = medicineName;
        Description = description;
        Contraindications = contraindications;
        this.type = valueOf;
        this.grade = grade;
        this.medicineCategory = oneMedicineCategoryByID;
        NumberofItems = numberofItems;
        this.price = price;
        this.manufecturer = man;
    }

    public Medicine(String medicineName, String description, String contraindications, Type valueOf, MedicineCategory oneMedicineCategoryByID, int numberofItems, int price, Manucfecturer man) {

        Name = medicineName;
        Description = description;
        Contraindications = contraindications;
        this.type = valueOf;
        this.grade = 0;
        this.medicineCategory = oneMedicineCategoryByID;
        NumberofItems = numberofItems;
        this.price = price;
        this.manufecturer = man;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setContraindications(String contraindications) {
        Contraindications = contraindications;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public MedicineCategory getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(MedicineCategory medicineCategory) {
        this.medicineCategory = medicineCategory;
    }

    public int getNumberofItems() {
        return NumberofItems;
    }

    public void setNumberofItems(int numberofItems) {
        NumberofItems = numberofItems;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Manucfecturer getManufecturer() {
        return manufecturer;
    }

    public void setManufecturer(Manucfecturer manufecturer) {
        this.manufecturer = manufecturer;
    }





    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getContraindications() {
        return Contraindications;
    }

    public Type getType() {
        return type;
    }
}
