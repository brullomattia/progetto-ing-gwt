package com.progetto.shared.concrete;

import java.io.Serializable;
import java.util.Date;

import com.progetto.shared.interfaces.Esame;

public class RealEsame implements Esame, Serializable{
	private int id;
	private String aula;
	private String ora;
	private Date data;
	private int durata;
	private int corso;
	private int CFU ;
	private boolean pubblicato;
	private String message;

	public RealEsame(){};
	public RealEsame(String a, Date d, int du, int c, int cf, String o){
		this.id = -1;
		this.aula = a;
		this.data = d;
		this.durata = du;
		this.corso = c;
		this.CFU = cf;
		this.pubblicato = false;
		this.message = "";
		this.ora = o;

	}

	public String getAula() {
		return aula;
	}
	public String getOra() {
		return ora;
	}
	public void setOra(String m) {
		this.ora = m;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String m) {
		this.message = m;
	}
	public void setAula(String aula) {
		this.aula = aula;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}
	public int getCorso() {
		return corso;
	}

	public void setCorso(int corso) {
		this.corso = corso;
	}
	public int getCFU() {
		return CFU;
	}

	public void setCFU(int cFU) {
		CFU = cFU;
	}
	
    public boolean isPubblicato(){
    	return pubblicato;	
    }
    
	public void setPubblicato(boolean pubblicato) {
		this.pubblicato = pubblicato;
	}

	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
}
	
