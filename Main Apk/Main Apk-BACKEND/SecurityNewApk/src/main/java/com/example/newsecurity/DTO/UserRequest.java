package com.example.newsecurity.DTO;

import com.example.newsecurity.Model.Address;
import com.example.newsecurity.Model.GenderEnum;

// DTO koji preuzima podatke iz HTML forme za registraciju
public class UserRequest {

	private long id;

	private String name;

	private String surname;

	private String mail;

	private String username;

	private String password;

	private String phoneNumber;

	private String jmbg;

	private GenderEnum gender;

	private Address address;
	private String role;

	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	public String getTitle(){return title;}
	public void setTitle(String title){this.title = title;}
	public String getRole(){return role;}
	public void setRole(String role){this.role = role;}
}
