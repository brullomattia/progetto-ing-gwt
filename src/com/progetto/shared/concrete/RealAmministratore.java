package com.progetto.shared.concrete;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;
import com.progetto.client.gui.SchermataAmministratore;
import com.progetto.shared.interfaces.Amministratore;

public class RealAmministratore extends Utente implements Amministratore {
	public RealAmministratore(){};
	public RealAmministratore(String name, String surname, String email, String password) {
		super(name, surname, email, password);
		this.type = "Amministratore";
	}
	
	@Override
	public Composite getWebpage(Prove main) {
		return new SchermataAmministratore(main, this);
	}
}
