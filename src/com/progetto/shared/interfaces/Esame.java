package com.progetto.shared.interfaces;

import java.util.Date;

public interface Esame {
	public int getId();
	public int getCorso();
	public String getAula();
	public Date getData();
	public int getDurata();
	public int getCFU();
	public boolean isPubblicato();
	//public boolean isVotiInseriti();
	
	public String getMessage();
	public void setMessage(String m);
	
	public void setId(int id);
	public void setCorso(int c);
	public void setAula(String a);
	public void setData(Date d);
	public void setDurata(int durata);
	public void setCFU(int cf);
	public void setPubblicato(boolean pubblicato);
	public String getOra();
	public void setOra(String m);
}
