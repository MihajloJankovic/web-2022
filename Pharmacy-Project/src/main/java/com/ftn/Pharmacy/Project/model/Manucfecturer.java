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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Manucfecturer() {

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
