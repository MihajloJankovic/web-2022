package com.ftn.PrviMavenVebProjekat.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private Long id;
    private String FirstName;
    private String LastName;
    private String email;
    private String Password;
    private String UserName;
    private String Street;
    private String StreetNum;

    private LocalDateTime AccountCreationTime;
    private UserType UserType;

    public User(Long id, String firstName, String lastName, String email, String password, String userName, LocalDateTime accountCreationTime, UserType userType, LocalDate dateOfBirth,String ulica,String broj) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
        Password = password;
        UserName = userName;
        AccountCreationTime = accountCreationTime;
        UserType = userType;
        DateOfBirth = dateOfBirth;
        Street = ulica;
        StreetNum = broj;
    }
    public User(String firstName, String lastName, String email, String password, String userName, LocalDateTime accountCreationTime, UserType userType, LocalDate dateOfBirth,String ulica,String broj) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
        Password = password;
        UserName = userName;
        AccountCreationTime = accountCreationTime;
        UserType = userType;
        DateOfBirth = dateOfBirth;
        Street = ulica;
        StreetNum = broj;
    }

    private LocalDate DateOfBirth;



    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(AccountCreationTime);
        return "User{" +
                "id=" + id +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", email='" + email + '\'' +
                ", Password='" + Password + '\'' +
                ", UserName='" + UserName + '\'' +
                ", AccountCreationTime=" + strDate +
                ", UserType=" + UserType +
                ", DateOfBirth=" + DateOfBirth +
                ", Street='" + FirstName + '\'' +
                ", StreetNum='" + FirstName + '\'' +
                '}';
    }


    public Long getId() {
        return id;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getStreetNum() {
        return StreetNum;
    }

    public void setStreetNum(String streetNum) {
        StreetNum = streetNum;
    }

    public String toFileString() {
        return this.getId() + ";" + this.getFirstName() + ";" + this.getLastName() + ";" +
                this.getEmail() + ";" + this.getPassword()+ ";" +this.getUserName()+ ";" +
                this.getAccountCreationTime()+ ";" +this.getUserType()+ ";" +this.DateOfBirth+ ";" +this.Street+ ";" +this.StreetNum;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public LocalDateTime getAccountCreationTime() {
        return AccountCreationTime;
    }

    public void setAccountCreationTime(LocalDateTime accountCreationTime) {
        AccountCreationTime = accountCreationTime;
    }

    public com.ftn.PrviMavenVebProjekat.model.UserType getUserType() {
        return UserType;
    }

    public void setUserType(com.ftn.PrviMavenVebProjekat.model.UserType userType) {
        UserType = userType;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public User() {
    }
}
