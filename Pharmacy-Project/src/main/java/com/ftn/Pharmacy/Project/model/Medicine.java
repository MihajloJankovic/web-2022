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

    public Medicine(String name, String description, String contraindications, Type type, MedicineCategory pp, int broj, int cena, String pro) {
        Name = name;
        Description = description;
        Contraindications = contraindications;
        this.type = type;
        this.grade = 0;
        this.medicineCategory = pp;
        this.NumberofItems = broj;
        this.price = cena;
        this.manufecturer.Name = pro;
        this.id = (long) (Math.random() * 100000000000000L);

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
