package com.progetto.shared.concrete;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;

//import com.progetto.server.DataManager;

public class Utente implements Serializable{
	String name;
	String surname;
	String email;
	String password;
	String type = "Utente";
	
	public Utente(){};
	public Utente(String name, String surname, String email, String password)
	{
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
	}
	
	public Composite getWebpage(Prove main)
	{
		return null;
	}
	
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void saveOnDB(){};
	public String getType()
	{
		return type;
	}
}
