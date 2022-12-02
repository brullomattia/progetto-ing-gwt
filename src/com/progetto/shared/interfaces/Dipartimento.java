package com.progetto.shared.interfaces;

import java.util.ArrayList;

public interface Dipartimento {
	public String getNome();
	public String getCoordinatore();
	public String getSede();
	public ArrayList<Integer> getCorsi();
	
	public void setNome(String nome);
	public void setCoordinatore(String emailCoordinatore);
	public void setSede(String indirizzoSede);
	public void addCorso(int idCorso);
	public void removeCorso(int idCorso);
}
