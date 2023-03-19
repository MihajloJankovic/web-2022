package com.ftn.Pharmacy.Project.model;


import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {


    private Long id;
    private Map<String, Integer> mapa = new LinkedHashMap<>();
    private String AdminComment;
    private String FarmacistComment;
    private boolean status;
    private boolean declined;
    private User Farmacist ;
    private LocalDateTime ordertime;

    public Order(String farmacistComment, boolean status, boolean declined, User farmacist, LocalDateTime ordertime,Map<String,Integer> p) {
        FarmacistComment = farmacistComment;
        this.status = status;
        this.declined = declined;
        Farmacist = farmacist;
        this.ordertime = ordertime;
        this.mapa = p;
    }
    public Order()
    {

    }

    public LocalDateTime getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(LocalDateTime ordertime) {
        this.ordertime = ordertime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order(Long id, String adminComment, String farmacistComment, boolean status, boolean declined, User farmacist, LocalDateTime ordertime) {
        this.id = id;
        AdminComment = adminComment;
        FarmacistComment = farmacistComment;
        this.status = status;
        this.declined = declined;
        Farmacist = farmacist;
        this.ordertime = ordertime;
    }

    public User getFarmacist() {
        return Farmacist;
    }

    public void setFarmacist(User farmacist) {
        Farmacist = farmacist;
    }

    public Map<String, Integer> getMapa() {
        return mapa;
    }

    public String getAdminComment() {
        return AdminComment;
    }

    public void setAdminComment(String adminComment) {
        AdminComment = adminComment;
    }

    public String getFarmacistComment() {
        return FarmacistComment;
    }

    public void setFarmacistComment(String farmacistComment) {
        FarmacistComment = farmacistComment;
    }

    public boolean isStatus() {
        return status;
    }

    public void setMapa(Map<String, Integer> mapa) {
        this.mapa = mapa;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }
}
