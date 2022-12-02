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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;



public class VisualizzaDatiStudente extends Composite{
	
	private VerticalPanel principalPanel= new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private Grid studList = new Grid(1,5);
	private Button coursesList;
	private Prove main;
	private HTML courses;
	
	private Logger l = Logger.getLogger("Debug");
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	public VisualizzaDatiStudente(Prove main) {
		initWidget(this.principalPanel);
		this.main = main;
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
				
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + "Segreteria" + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
	
		
		//ourCoursesList for student 
		studList.setWidget(0, 0, new HTML("<b>Matricola</b>"));
		studList.setWidget(0, 1, new HTML("<b>Nome</b>"));
		studList.setWidget(0, 2, new HTML("<b>Cognome</b>"));
		studList.setWidget(0, 3, new HTML("<b>Email</b>"));
		studList.setWidget(0, 4, new HTML("<b>Esami/Corsi</b>"));
		studList.setCellSpacing(10);
		studList.setStyleName("padding-table");
		
		//gestione ourCourses
		//addStudent(studList,"000000484949","Mattia","Brullo","prova@mail.it");
		/* MANCA IMPLEMENTAZIONE PER POPOLARE LE GRIGLIE */
		greetingService.getUtenti(new AsyncCallback<ArrayList<Utente>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<Utente> result) {
				// TODO Auto-generated method stub
				ArrayList<RealStudente> allStudenti = new ArrayList<RealStudente>();
				ArrayList<RealStudente> studentiIscrittiEsame = new ArrayList<RealStudente>(); 
				for(Utente u : result) {
					if(u.getType().equalsIgnoreCase("Studente")) {
						allStudenti.add((RealStudente) u);
						//addStudent(studList, allStudenti.);
					}
				}
				for(RealStudente s : allStudenti) {
					/*if(s.getRegistrazioniEsami().contains(c.getEsame())) {
						studentiIscrittiEsame.add(s);
						ourStudents(ourStudents, s.getEmail(), s.getMatricola());
					}*/
					addStudent(studList, s.getMatricola(), s.getName(), s.getSurname(), s.getEmail(), s);
				}
				
				
				l.log(Level.INFO, "Successo allStudent 1");
				l.log(Level.INFO, "dimensione studenti "+allStudenti.size());
				l.log(Level.INFO, "studenti iscritti a questo esame "+studentiIscrittiEsame.size());
			}
			
		});
			
		
		
		//HTML courses
		courses= new HTML("<b>Dati studenti: </b>");
		
		// first HPanel
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(courses);
		principalPanel.add(studList);
	
		
		
		
		
		
		
		
	}
	
	public void addStudent(Grid studList,String matricola,String name,String prename,String email, RealStudente s) {
		coursesList= new Button("Visualizza corsi");
		int rowCount= studList.getRowCount();
		studList.insertRow(rowCount);
		studList.setText(rowCount, 0, matricola);
		studList.setText(rowCount, 1, name);
		studList.setText(rowCount, 2, prename);
		studList.setText(rowCount, 3, email);
		studList.setWidget(rowCount, 4, coursesList);
		coursesList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new SchermataCorsiEsamiStudenti(main, s));
			}
			
		});
			
	}
	

	
	public void clearRow(Grid studList,int rowCount) {
		
		studList.setText(rowCount, 0, "");
		studList.setText(rowCount, 1, "");
		studList.setText(rowCount, 2, "");
		studList.setText(rowCount, 3, "");
		studList.setWidget(rowCount, 4, null);
		
	}

}
