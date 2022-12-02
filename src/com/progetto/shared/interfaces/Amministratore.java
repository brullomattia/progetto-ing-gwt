package com.progetto.shared.interfaces;

import com.google.gwt.user.client.ui.Composite;
import com.progetto.client.Prove;

public interface Amministratore {
	public Composite getWebpage(Prove main);
	public String getName();
	public String getSurname();
	public String getEmail();
	public String getPassword();
	public String getType();
	
	public void setName(String name);
	public void setSurname(String surname);
	public void setEmail(String email);
	public void setPassword(String password);
}
