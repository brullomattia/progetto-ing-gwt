package com.progetto.shared.interfaces;

import java.util.Date;

public interface Corso {
	public int getId();
	public String getNome();		
	public Date getDataInizio();
	public Date getDataFine();
	public String getDocente();
	public String getCoDocente();
	public String getDescrizione();
	public String getDipartimento();
	public int getEsame();
	
	public void setId(int id);
	public void setNome(String nome);
	public void setDataInizio(Date dataInizio);
	public void setDataFine(Date dataFine);
	public void setDocente(String docente);
	public void setCoDocente(String coDocente);
	public void setDescrizione(String descrizione);
	public void setDipartimento(String dipartimento);
	public void setEsame(int esame);
}
