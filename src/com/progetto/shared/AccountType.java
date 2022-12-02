package com.progetto.shared;

public enum AccountType {
	Studente, Segreteria, Docente, Amministratore;
	
	public static AccountType getFromString(String type) {
		if(type.equals("Studente"))
		{
			return AccountType.Studente;
		}
		if(type.equals("Docente"))
		{
			return AccountType.Docente;
		}
		if(type.equals("Segreteria"))
		{
			return AccountType.Segreteria;
		}
		if(type.equals(Amministratore))
		{
			return AccountType.Amministratore;
		}
		return null;
	}
}
