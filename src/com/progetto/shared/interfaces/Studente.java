package com.progetto.shared.interfaces;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;

public interface Studente {
	public Composite getWebpage(Prove main);
	
	public ArrayList<Integer> getRegistrazioniEsami();
	public ArrayList<Integer> getIscrizioniCorsi();
	public int[][] getLibretto();
	public String getName();
	public String getSurname();
	public String getMatricola();
	public String getEmail();
	public String getPassword();
	public String getType();

	public void addCorso(int r);
	public void removeCorso(int r);	
	public void addRegistrazioneEsame(int r);
	public void removeRegistrazioneEsame(int r);
	public void setName(String name);
	public void setSurname(String surname);
	public String setMatricola(String matricola);
	public void setEmail(String email);
	public void setPassword(String password);
}
