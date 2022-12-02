package com.progetto.shared.concrete;

import java.io.Serializable;
import java.util.ArrayList;

import com.progetto.shared.interfaces.Dipartimento;

public class RealDipartimento implements Dipartimento, Serializable{
	private String nome;
	private String coordinatore;
	private String sede;
	private ArrayList<Integer> corsi;
	
	public RealDipartimento(){};
	public RealDipartimento(String nome, ArrayList<Integer> corsi, String coordinatore, String sede)
	{
		this.nome = nome;
		this.corsi = corsi;
		this.coordinatore = coordinatore;
		this.sede = sede;
	}
	
	public String getNome() {
		return nome;
	}
	
	public ArrayList<Integer> getCorsi() {
		return corsi;
	}
	
	@Override
	public String getCoordinatore() {
		return coordinatore;
	}
	
	@Override
	public String getSede() {
		return sede;
	}

	public void setNome(String s) {
		this.nome=s;
	}
	
	public void addCorso(int c) {
		if(!this.corsi.contains(c)){
			this.corsi.add(c);
		}
	}
	
	public void removeCorso(int c) {
		if(this.corsi.contains(c)){
			this.corsi.remove(c);
		}
	}	
	
	@Override
	public void setCoordinatore(String emailCoordinatore) {
		this.coordinatore = emailCoordinatore;
	}
	
	@Override
	public void setSede(String indirizzoSede) {
		this.sede = indirizzoSede;
	}

}
