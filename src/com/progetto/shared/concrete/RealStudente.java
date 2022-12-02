package com.progetto.shared.concrete;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;
import com.progetto.client.gui.SchermataStudente;
import com.progetto.shared.interfaces.Studente;

public class RealStudente extends Utente implements Studente, Serializable{
	
	private ArrayList<Integer> registrazioniEsame; 
	
	private ArrayList<Integer> iscrizioniCorso;    
	private String matricola;
	private int[][] libretto;
	
	
	
	public RealStudente(){};
	
	public RealStudente(String name, String surname, String email, String matricola, String password) {
		super(name, surname, email, password);
		registrazioniEsame = new ArrayList<Integer>();
		iscrizioniCorso = new ArrayList<Integer>();
		this.libretto = new int[50][2];		
		this.matricola = matricola;	
		this.type = "Studente";
	}

	public ArrayList<Integer> getIscrizioniCorsi() {
		return iscrizioniCorso;
	}

	public int[][] getLibretto() {	
	return libretto;
	}
	
	public boolean insertVoto(int idEsame,int voto){
		boolean result = false;
		
		if(this.getRegistrazioniEsami().contains(idEsame)){
			for(int i = 0; i< 50;i++){
				if((!result) && ((libretto[i][0]==0)||(libretto[i][0]==idEsame))){
					libretto[i][0]=idEsame;
					libretto[i][1]=voto;
					result = true;
					
				}
			}
		}
		
		return result;
	}
	
	public void addCorso(int c) {
		if(!this.iscrizioniCorso.contains(c)){
			this.iscrizioniCorso.add(c);
		}	
	}
	
	//dato l' id di un esame, questo metodo ritorna il voto corrispondente
	//se l' esame non esiste o non ha un voto,il metodo ritorna -1
	public int getVoto(int e) {
		int result = -1;
		for(int i = 0; i< 50;i++){
			if(libretto[i][0]==e){				
				result = libretto[i][1];								
			}
		}
		return result;
	}	
	
	
	public void removeCorso(int c) {
		if(this.iscrizioniCorso.contains(c)){
			this.iscrizioniCorso.remove(Integer.valueOf(c));
		}	
	}
	
	public ArrayList<Integer> getRegistrazioniEsami() {
		return registrazioniEsame;
	}
	public void addRegistrazioneEsame(int r) {
		if(!this.registrazioniEsame.contains(r)){
			this.registrazioniEsame.add(r);
		}	
	}	
	public void removeRegistrazioneEsame(int r) {
		if(this.registrazioniEsame.contains(r)){
			this.registrazioniEsame.remove(Integer.valueOf(r));
		}	
	}


	
	@Override
	public Composite getWebpage(Prove main) {
		return new SchermataStudente(main,this);
	}
	@Override
	public String getMatricola() {
		return matricola;
	}
	@Override
	public String setMatricola(String matricola) {
		return this.matricola = matricola;
	}

}
