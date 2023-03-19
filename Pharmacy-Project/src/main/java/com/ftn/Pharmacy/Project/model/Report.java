package com.ftn.Pharmacy.Project.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Report {
    private Long id;

    public Report(User customer, LocalDate date, Map<Medicine, Map<Integer, Integer>> mapa) {
        Customer = customer;
        this.date = date;
        this.mapa = mapa;
    }

    public Report(Long id, User customer, LocalDate date, Map<Medicine, Map<Integer, Integer>> mapa) {
        this.id = id;
        Customer = customer;
        this.date = date;
        this.mapa = mapa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private User Customer;
    private LocalDate date;
    private Map<Medicine,Map<Integer,Integer>> mapa;

    public User getCustomer() {
        return Customer;
    }

    public void setCustomer(User customer) {
        Customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<Medicine, Map<Integer, Integer>> getMapa() {
        return mapa;
    }

    public void setMapa(Map<Medicine, Map<Integer, Integer>> mapa) {
        this.mapa = mapa;
    }
}
