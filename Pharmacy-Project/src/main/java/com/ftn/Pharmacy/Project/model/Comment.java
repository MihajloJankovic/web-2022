package com.ftn.Pharmacy.Project.model;

import java.util.Date;

public class Comment {
    Long id;
    String text;
    int grade;
    Date date;
    int autor;
    int medicine;
    boolean anonimus;

    public Comment() {
    }

    public Comment(String text, int grade, Date date, int autor, int medicine, boolean anonimus) {
        this.text = text;
        this.grade = grade;
        this.date = date;
        this.autor = autor;
        this.medicine = medicine;
        this.anonimus = anonimus;
    }

    public Comment(Long id, String text, int grade, Date date, int autor, int medicine, boolean anonimus) {
        this.id = id;
        this.text = text;
        this.grade = grade;
        this.date = date;
        this.autor = autor;
        this.medicine = medicine;
        this.anonimus = anonimus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAutor() {
        return autor;
    }

    public void setAutor(int autor) {
        this.autor = autor;
    }

    public int getMedicine() {
        return medicine;
    }

    public void setMedicine(int medicine) {
        this.medicine = medicine;
    }

    public boolean isAnonimus() {
        return anonimus;
    }

    public void setAnonimus(boolean anonimus) {
        this.anonimus = anonimus;
    }


}
