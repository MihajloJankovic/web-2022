package com.ftn.Pharmacy.Project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;



public class User {
	
	private Long userID;
	private String username;
	private String password;
	private String email;
	private String name;
	private String surname;
	private LocalDate birthDate;
	private String street;
	private String streetNumber;
	private String city;
	private String country;
	private String phoneNumber;
	private UserRole role;
	private boolean logged = false;

	private LocalDateTime registeredTime;
	private String birthDateStr;

	public User() {
		
		this.username = "";
		this.password = "";
		this.email = "";
		this.name = "";
		this.surname = "";
		this.birthDate = null;
		this.phoneNumber = "";
		this.role = null;
		this.street = "";
		this.streetNumber = "";
		this.city = "";
		this.country = "";
	}

	public User(Long userID, String username, String password, String email, String name, String surname, LocalDate birthDate, String street, String streetNumber, String city, String country, String phoneNumber, UserRole role, LocalDateTime registeredTime) {
		
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.birthDate = birthDate;
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.registeredTime = registeredTime;
		this.birthDateStr = String.valueOf(this.birthDate);
		}


	public User(String username, String password, String email, String name, String surname, LocalDate birthDate, String street, String streetNumber, String city, String country, String phoneNumber, UserRole role, LocalDateTime registeredTime) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.birthDate = birthDate;
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.registeredTime = registeredTime;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getBirthDateStr() {
		return birthDateStr;
	}

	public void setBirthDateStr(String birthDateStr) {
		this.birthDateStr = birthDateStr;
	}

	public LocalDateTime getRegisteredTime() {
		return registeredTime;
	}

	public void setRegisteredTime(LocalDateTime registeredTime) {
		this.registeredTime = registeredTime;
	}

	@Override
	public String toString() {
		return "User{" +
				"userID=" + userID +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", birthDate=" + birthDate +
				", street='" + street + '\'' +
				", streetNumber='" + streetNumber + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", role=" + role +
				", logged=" + logged +
				", registeredTime=" + registeredTime +
				", birthDateStr='" + birthDateStr + '\'' +
				'}';
	}

	public String toFileString() {
		return this.getUserID() + "|" + this.getUsername() + "|" + this.getPassword() + "|" +
				this.getEmail() + "|" + this.getName() + "|" + this.getSurname() + "|" + this.getBirthDate() + "|" +
				this.getStreet() + ";" + this.getStreetNumber() + ";" + this.getCity() + ";" + this.getCountry() + "|" +
				this.getPhoneNumber() + "|" + this.getRole() + "|" + this.getRegisteredTime();
	}
}

