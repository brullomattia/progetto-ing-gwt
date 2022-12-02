package com.progetto.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.progetto.client.gui.SchermataIniziale;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Prove implements EntryPoint {
	
	 //Create a remote service proxy to talk to the server-side Greeting service.
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");	
	public ClickHandler logoutHandler;	
	
	//This is the entry point method. 
	public void onModuleLoad() {
		Prove main = this;
		caricaDatiPerProva();
		setPage(new SchermataIniziale(main));
		
		logoutHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setPage(new SchermataIniziale(main));
			}
		};
	}
	
	public void setPage(Composite webpage){
		RootPanel.get("Body").clear();
		RootPanel.get("Body").add(webpage);
	}

	
	// Questo metodo serve per caricare dei dati predefiniti sul DB prima di iniziare ad usare il programma:
	public void caricaDatiPerProva(){	
		try{
			
		greetingService.caricaDatiDebug( new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage()+ "problema con carica dati debug, c'è una eccezione");
			}

			@Override
			public void onSuccess(Boolean result) {
				l.log(Level.INFO, "carica dati debug è avvenuto con successo");
			}
		});
		}
		catch(Exception e){l.log(Level.WARNING, e.getMessage()+"problema con carica dati debug");}
		
	}
	
	
}
