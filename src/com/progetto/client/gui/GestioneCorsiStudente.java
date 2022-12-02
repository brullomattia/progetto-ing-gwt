package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
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
import com.progetto.shared.interfaces.Studente;

public class GestioneCorsiStudente extends Composite{
	private VerticalPanel principalPanel= new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private Grid ourCourses = new Grid(1,6);
	private Grid allCourses = new Grid(1,6);
	private Button unButton;
	private Button addButton;
	private HTML userCourses;
	private HTML courses;
	private Prove main;
	private RealStudente user;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");

	
	public GestioneCorsiStudente(Prove mainn, Studente u) {
		this.user= (RealStudente) u;
		this.main=mainn;
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
				
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + user.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		//ourCoursesList: lista dei corsi a cui lo studente  e' GIA' iscritto
		ourCourses.setWidget(0, 0, new HTML("<b>Nome</b>"));
		ourCourses.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		ourCourses.setWidget(0, 2, new HTML("<b>Docente</b>"));
		ourCourses.setWidget(0, 3, new HTML("<b>Data Inizio</b>"));
		ourCourses.setWidget(0, 4, new HTML("<b>Data Fine</b>"));
		ourCourses.setWidget(0, 5, null);
		ourCourses.setCellSpacing(10);
		ourCourses.setStyleName("padding-table");
		
		l.log(Level.INFO, "MESSAGGIO INIZIALE:lo studente è già iscritto ai corsi con id: "+user.getIscrizioniCorsi());
		
		//aggiungere alla tabella ourCourses tutti i corsi a cui lo studente è gia iscritto
		greetingService.getCorsi(user.getIscrizioniCorsi(), new AsyncCallback<ArrayList<RealCorso>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealCorso> result) {
				DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");		
				for(RealCorso c: result) {
					String StringInizio = fm.format(c.getDataInizio());
					String StringFine = fm.format(c.getDataFine());
					
					addOurCourses(ourCourses, c.getNome(), c.getDipartimento(), c.getDocente(),user,c.getId(), StringInizio ,  StringFine);
					l.log(Level.INFO, "Durante get Iscrizioni: SEI GIA' iscritto al corso con nome: "+c.getNome());
				}
				
			}
			
		});
		
		
		
		//allCoursesList : lista dei corsi a cui lo studente NON e' iscritto
		allCourses.setWidget(0, 0, new HTML("<b>Nome</b>"));
		allCourses.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		allCourses.setWidget(0, 2, new HTML("<b>Docente</b>"));
		allCourses.setWidget(0, 3, new HTML("<b>Data Inizio</b>"));
		allCourses.setWidget(0, 4, new HTML("<b>Data Fine</b>"));
		allCourses.setWidget(0, 5, null);
		allCourses.setCellSpacing(10);
		allCourses.setStyleName("padding-table");
		
		//gestione allCourses list
		greetingService.getCorsi(new AsyncCallback<ArrayList<RealCorso>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealCorso> result) {
				DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
				
				for(RealCorso c: result) {
					String StringInizio = fm.format(c.getDataInizio());
					String StringFine = fm.format(c.getDataFine());
					if(!user.getIscrizioniCorsi().contains(c.getId())){
						addAllCourses(allCourses, c.getNome(), c.getDipartimento(), c.getDocente(),user,c.getId(),StringInizio,StringFine);
						l.log(Level.INFO, "Durante get all courses: non sei ancora iscritto al corso con nome: "+c.getNome()+" e id: "+c.getId());
					}
				}
				
			}
			
		});
		
	
		//HTML userCourses
		userCourses= new HTML("<b>Corsi a cui sei iscritto: </b>");
		
		//HTML courses
		courses= new HTML("<b>Corsi disponibili: </b>");
		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(userCourses);
		principalPanel.add(ourCourses);
		principalPanel.add(courses);
		principalPanel.add(allCourses);
	}
	
	public void addOurCourses(Grid ourCourses,String name, String dip, String doc,RealStudente user,int id, String inizio, String fine) {
		int rowCount = ourCourses.getRowCount();
		unButton = new Button("Disiscrivimi");
		ourCourses.insertRow(rowCount);
		ourCourses.setText(rowCount, 0, name);
		ourCourses.setText(rowCount, 1, dip);
		ourCourses.setText(rowCount, 2, doc);
		ourCourses.setText(rowCount, 3, inizio);
		ourCourses.setText(rowCount, 4, fine);
		ourCourses.setWidget(rowCount, 5, unButton);
		unButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAllCourses(allCourses,name,dip,doc,user ,id,inizio,fine);
				clearRow(ourCourses,rowCount);
				
				greetingService.disiscrizioneCorso(user, id, new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore");
						l.log(Level.WARNING, caught.getMessage());
						
					}
					
					@Override
					public void onSuccess(Boolean result) {

						l.log(Level.INFO, "Successo Disiscrizione");
						update(user.getEmail());
						

					}
					
				});
				
				
			}});
	}
	
	public void addAllCourses(Grid allCourses,String name,String dip, String doc,RealStudente user, int id, String inizio, String fine) {
		int rowCount = allCourses.getRowCount();
		addButton = new Button("Iscrivimi");
		allCourses.insertRow(rowCount);
		allCourses.setText(rowCount, 0, name);
		allCourses.setText(rowCount, 1, dip);
		allCourses.setText(rowCount, 2, doc);
		allCourses.setText(rowCount, 3, inizio);
		allCourses.setText(rowCount, 4, fine);
		allCourses.setWidget(rowCount, 5, addButton);
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addOurCourses(ourCourses,name,dip,doc,user,id,inizio,fine);
				clearRow(allCourses,rowCount);	
				
				//effettivamente iscrivere lo studente
				greetingService.iscrizioneCorso(user, id, new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore");
						l.log(Level.WARNING, caught.getMessage());
						
					}
					
					@Override
					public void onSuccess(Boolean result) {
						if (result ==true){
						l.log(Level.INFO, "Successo Iscrizione");
						l.log(Level.INFO, "ti sei  iscritto al corso con nome: "+ name);
						update(user.getEmail());
						
					}}
					
				});
				
				
				
					
				
			}
		});
	}
	
	public void clearRow(Grid ourCourses,int rowCount) {
		ourCourses.setText(rowCount, 0, "");
		ourCourses.setText(rowCount, 1, "");
		ourCourses.setText(rowCount, 2, "");
		ourCourses.setText(rowCount, 3, "");
		ourCourses.setText(rowCount, 4, "");
		ourCourses.setWidget(rowCount, 5, null);	
	}
	
	
	/*
	 * update e setUser aggiornano la variabile di istranza di tipo RealStudente in modo che 
	 * 	i cambiamenti avvenuti nel lato server a seguito delle iscrizioni siano visibili anche nel lato client
	 */
	public boolean update(String email){
		
		greetingService.getUtente(email, new AsyncCallback<Utente>() {

			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
				
			}
			
			@Override
			public void onSuccess(Utente result) {
				if (result !=null){
				
				setUser(result);
				l.log(Level.INFO, "Hai eseguito l'update e ora i corsi seguiti sono:"+user.getIscrizioniCorsi());
				
			}}
			
		});
		return true;
	}
	
	public void setUser(Utente s){
		
		this.user=(RealStudente)s;
		
	}
}
