package com.progetto.shared.interfaces;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;

public interface Docente{
	public Composite getWebpage(Prove main);
	
	public ArrayList<Integer> getCorsiInsegnati();
	public ArrayList<Integer> getCorsiCoInsegnati();
	public String getName();
	public String getSurname();
	public String getEmail();
	public String getPassword();
	public String getType();
	
	public void addCorsoInsegnato(int c);
	public void removeCorsoInsegnato(int c);
	public void addCorsoCoInsegnato(int c);
	public void removeCorsoCoInsegnato(int c);
	public void setName(String name);
	public void setSurname(String surname);
	public void setEmail(String email);
	public void setPassword(String password);
}
