package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;

public class SchermataStudente extends Composite{
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private HorizontalPanel secondPanel= new HorizontalPanel();
	private Image uniImg;
	private HTML userName;
	private Anchor logout;
	private Image userImg;
	private HTML accessPage;
	private HTML personalDate;
	private Button examButton;
	private Button coursesButton;
	private Prove main;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	private RealStudente user;
		
	public SchermataStudente(Prove mainn,Utente u) {
		this.main = mainn;
		user= (RealStudente) u;
		initWidget(this.principalPanel);
		verificaStudente();
		
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
			
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + user.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
			
		//HTML  student page
		accessPage= new HTML("<b>Accesso Studente Dati Personali: </b>");
			
		//HTML personalDatee
		personalDate= new HTML("<br>Email:" + user.getEmail() + "<br>Matricola:"+ user.getMatricola() 
				+ "<br>Nome:"+ user.getName() + "<br>Cognome:" + user.getSurname());
			
		//ExamButton
		examButton = new Button("GESTIONE ESAMI");
		examButton.setPixelSize(100,80);
		examButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneEsamiStudente(main,user));
				
			}
			
		});
		
			
		//CoursesButton
		coursesButton = new Button("GESTIONE CORSI A CUI SEI ISCRITTO");
		coursesButton.setPixelSize(100,80);
		coursesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneCorsiStudente(main,user));
				
			}
			
		});
				
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);	
			
		// second HPanel
		this.secondPanel.setSpacing(30);
		secondPanel.add(personalDate);
		secondPanel.add(coursesButton);
		secondPanel.add(examButton);	

		//Principal VPanel
		principalPanel.add(firstPanel);
		principalPanel.add(accessPage);
		principalPanel.add(secondPanel);
	}
	
	
	
	
	
	/*
	 * metodo di verifica che non ha funzioni operative, ma che serve solo per visualizzare in console 
	 * i dati relativi allo studente che ha appena effettuato il login.
	 */
	public void verificaStudente(){
				greetingService.getCorsi(user.getIscrizioniCorsi(), new AsyncCallback<ArrayList<RealCorso>>() {

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore");
						l.log(Level.WARNING, caught.getMessage());
						
					}

					@Override
					public void onSuccess(ArrayList<RealCorso> result) {
						for(RealCorso c: result) {
							
							l.log(Level.INFO, "Lo studente corrente, è iscritto a: "+c.getNome()+ "con id="+ c.getId());
						}
						
					}
					
				});
	}
}