package com.ftn.Pharmacy.Project.model;

import java.awt.font.TextHitInfo;

public class Manucfecturer {
    String Name;
    Long id;
    String Country;

    public Manucfecturer(String name) {
        Name = name;
    }

    public Manucfecturer(Long id, String name, String country) {
        this.Name = name;
        this.Country = country;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
