package com.progetto.shared.concrete;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;
import com.progetto.client.gui.SchermataDocente;
import com.progetto.shared.interfaces.Docente;

public class RealDocente extends Utente implements Docente, Serializable{
	private ArrayList<Integer> corsiInsegnati;
	private ArrayList<Integer> corsiCoInsegnati;

	public RealDocente(){};
	public RealDocente(String name, String surname, String email, String password) {
		super(name, surname, email, password);
		corsiInsegnati = new ArrayList<Integer>();
		corsiCoInsegnati = new ArrayList<Integer>();
		this.type = "Docente";
	}

	public ArrayList<Integer> getCorsiInsegnati() {
		return corsiInsegnati;
	}

	
	public void addCorsoInsegnato(int c) {
		if(!this.corsiInsegnati.contains(c)){
			this.corsiInsegnati.add(c);
		}	
	}

	public void removeCorsoInsegnato(int c) {
		if(this.corsiInsegnati.contains(c)){
			this.corsiInsegnati.remove(Integer.valueOf(c));
		}	
	}
	public ArrayList<Integer> getCorsiCoInsegnati() {
		return corsiCoInsegnati;
	}

	
	public void addCorsoCoInsegnato(int c) {
		if(!this.corsiCoInsegnati.contains(c)){
			this.corsiCoInsegnati.add(c);
		}	
	}

	public void removeCorsoCoInsegnato(int c) {
		if(this.corsiCoInsegnati.contains(c)){
			
			//this.corsiCoInsegnati.remove(c);
			 this.corsiCoInsegnati.remove(Integer.valueOf(c));
		}	
	}
	
	@Override
	public Composite getWebpage(Prove main) {
		return new SchermataDocente(main, this);
	}	
}
