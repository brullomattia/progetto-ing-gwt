package com.progetto.shared.concrete;

import java.io.Serializable;
import java.util.Date;

import com.progetto.shared.interfaces.Corso;


public class RealCorso implements Corso, Serializable{
	private int id;
	private String nome;
	private Date dataInizio;
	private Date dataFine;
	private String docente;
	private String coDocente;
	private String descrizione;
	private String dipartimento;
	private int esame;
	
	public RealCorso(){};
	public RealCorso(String n, Date i, Date f, String d, String cod, String de, String dip, int e){
		this.id = -1;
		nome=n;
		dataInizio=i;
		dataFine=f;
		docente=d;
		coDocente=cod;
		descrizione=de;
		dipartimento=dip;
		esame=e;
	}
	public RealCorso(int id, String n, Date i, Date f, String d, String cod, String de, String dip, int e){
		this.id = id;
		nome=n;
		dataInizio=i;
		dataFine=f;
		docente=d;
		coDocente=cod;
		descrizione=de;
		dipartimento=dip;
		esame=e;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	
	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	
	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}
	public String getCoDocente() {
		return coDocente;
	}

	public void setCoDocente(String coDocente) {
		this.coDocente = coDocente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(String dipartimento) {
		this.dipartimento = dipartimento;
	}
	
	
	public int getEsame() {
		return esame;
	}

	public void setEsame(int esame){
		this.esame = esame;
	}
}
