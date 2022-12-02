package com.progetto.shared.concrete;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;
import com.progetto.client.gui.SchermataSegreteria;
import com.progetto.shared.interfaces.Segreteria;

public class RealSegreteria extends Utente implements Segreteria, Serializable{

	public RealSegreteria(){};
	public RealSegreteria(String name, String surname, String email, String password) {
		super(name, surname, email, password);
		this.type = "Segreteria";
	}
	
	@Override
	public Composite getWebpage(Prove main) {
		return new SchermataSegreteria(main);
	}

}
