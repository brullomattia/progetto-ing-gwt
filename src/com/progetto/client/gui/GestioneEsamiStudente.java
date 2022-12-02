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
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;

public class GestioneEsamiStudente extends Composite{
	
	private VerticalPanel principalPanel= new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private Grid ourExams = new Grid(1,6);
	private Grid allExams = new Grid(1,6);
	private Grid recordExams = new Grid(1,4);
	private Button unButton;
	private Button addButton;
	private HTML userExams;
	private HTML exams;
	private HTML examsRecord;
	private Prove main;
	private RealStudente user;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private Logger l = Logger.getLogger("Debug");	
	
	public GestioneEsamiStudente(Prove mainn,Utente u) {
		initWidget(this.principalPanel);
		this.main = mainn;
		this.user = (RealStudente) u;
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
				
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + u.getName()+ "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		//ourExamsList for student 
		ourExams.setWidget(0, 0, new HTML("<b>Nome</b>"));
		ourExams.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		ourExams.setWidget(0, 2, new HTML("<b>CFU</b>"));
		ourExams.setWidget(0, 3, new HTML("<b>Data</b>"));
		ourExams.setWidget(0, 4, new HTML("<b>Ora</b>"));
		ourExams.setWidget(0, 5, null);
		ourExams.setCellSpacing(10);
		ourExams.setStyleName("padding-table");
		
		
		
		/*
		 * gestione di due tabelle:
		 * 	1-)ourExams: esami a cui lo studente è iscritto, ma con voto non ancora pubblicato
		 * 	2-) recordExams: esami con esito pubblicato.
		 */
		
		greetingService.getEsami(user.getRegistrazioniEsami(), new AsyncCallback<ArrayList<RealEsame>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "ErroreGetEsamiDelloUser");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealEsame> listaEsami) {
				
				ArrayList<Integer> idCorsi = new ArrayList<Integer>();
				for(RealEsame e: listaEsami){
					idCorsi.add(e.getCorso());		
				}
				
				greetingService.getCorsi(idCorsi, new AsyncCallback<ArrayList<RealCorso>>() {

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "ErroreGetCorsi");
						l.log(Level.WARNING, caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<RealCorso> listaCorsi) {
						
						DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
						for(RealCorso c: listaCorsi) {
							
							//troviamo il cfu corrispondente
							int cfu=0;
							boolean pubblicato=false;
							int idEx=-1;
							String dataEsame = "";
							String oraEsame = "";
								for(RealEsame e: listaEsami){
									if(e.getId()==c.getEsame()){
										cfu=e.getCFU();
										pubblicato=e.isPubblicato();
										idEx=e.getId();
										dataEsame = fm.format(e.getData());
										oraEsame = e.getOra();
									}	
								}
								
								
								
								
							
							if(!pubblicato){
								addToOurExams(ourExams, c.getNome(), c.getDipartimento(),""+cfu,user,c.getEsame(),dataEsame,oraEsame);
								l.log(Level.INFO, "aggiungiamo agli esami a cui lo studente e' iscritto");
							}
							if(pubblicato){
								addToRecordExams(recordExams, c.getNome(), c.getDipartimento(),""+cfu,idEx);
								l.log(Level.INFO, "abbiamo aggiunto al libretto");
							}
						}
						
					}
					
				});
			}		
			});	
					
	
		
		/*
		 * gestione allExams: ovvero la tabella in cui compaiono gli esami a cui lo studente NON e' ancora iscritto
		 */
		greetingService.getCorsi(user.getIscrizioniCorsi(), new AsyncCallback<ArrayList<RealCorso>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "ErroreGetCorsi");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealCorso> corsiIscritto) {
	
				ArrayList<Integer> tuttiEsami = new ArrayList<Integer>();
				
				for(RealCorso c: corsiIscritto) {
							l.log(Level.INFO, "Corso a cui sei iscritto : "+c.getNome()+"   id corso:"+c.getId()+"  idEsame:"+c.getEsame());
							if(c.getEsame()!=-1){
								tuttiEsami.add(c.getEsame());}
						}
				
				
				greetingService.getEsami(tuttiEsami, new AsyncCallback<ArrayList<RealEsame>>() {

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "ErroreGetEsamiConId");
						l.log(Level.WARNING, caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<RealEsame> listaEsami) {
						l.log(Level.INFO, "l'array di esami totali  ha dimensione: "+tuttiEsami.size());
						DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
						
						for(RealCorso c: corsiIscritto){
						
							//controlliamo che l' esame abbia effettivamente un corso.
							if(c.getEsame()!=-1){
							
								//troviamo il cfu corrispondente e la data
								int cfu=0;
								String dataEsame = "";
								String oraEsame = "";	
								for(RealEsame e: listaEsami){
										if(e.getId()==c.getEsame()){
											cfu=e.getCFU();
											dataEsame = fm.format(e.getData());
											oraEsame = e.getOra();
										}
									}
								
								//controlliamo che lo studente NON sia già registrato all' Esame
									if(!user.getRegistrazioniEsami().contains(c.getEsame())){
										addToAllExams(allExams,c.getNome(),c.getDipartimento(),""+cfu,user,c.getEsame(),dataEsame,oraEsame);
										l.log(Level.INFO, "Siamo entrati nel primo for ");
									}
							}
						}
					}
				});
			}	
			});								
		
		
		//allExamsList for student 
		allExams.setWidget(0, 0, new HTML("<b>Nome</b>"));
		allExams.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		allExams.setWidget(0, 2, new HTML("<b>CFU</b>"));
		allExams.setWidget(0, 3, new HTML("<b>Data</b>"));
		allExams.setWidget(0, 4, new HTML("<b>Ora</b>"));
		allExams.setWidget(0, 5, null);
		allExams.setCellSpacing(10);
		allExams.setStyleName("padding-table");
		
		
		//recordExams for student: lista di esami effettuati e con voto
		recordExams.setWidget(0, 0, new HTML("<b>Nome</b>"));
		recordExams.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		recordExams.setWidget(0, 2, new HTML("<b>CFU</b>"));
		recordExams.setWidget(0, 3, new HTML("<b>Voto</b>"));
		recordExams.setCellSpacing(10);
		recordExams.setStyleName("padding-table");
	
		
		//HTML userCourses
		userExams= new HTML("<b>Esami a cui sei iscritto: </b>");
		
		//HTML courses
		exams= new HTML("<b>Esami disponibili: </b>");
		
		//HTML examsRecord
		examsRecord= new HTML("<b>Libretto universitario</b>");
		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(userExams);
		principalPanel.add(ourExams);
		principalPanel.add(exams);
		principalPanel.add(allExams);
		principalPanel.add(examsRecord);
		principalPanel.add(recordExams);
	}
	
	
	public void addToOurExams(Grid ourCourses,String name, String dip, String cfu, RealStudente user,int idEsame,String dataEs, String oraEs) {
		int rowCount = ourCourses.getRowCount();
		unButton = new Button("Disiscrivimi");
		ourCourses.insertRow(rowCount);
		ourCourses.setText(rowCount, 0, name);
		ourCourses.setText(rowCount, 1, dip);
		ourCourses.setText(rowCount, 2, cfu);
		ourCourses.setText(rowCount, 3, dataEs);
		ourCourses.setText(rowCount, 4, oraEs);
		ourCourses.setWidget(rowCount, 5, unButton);
		unButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addToAllExams(allExams,name,dip,cfu,user,idEsame, dataEs,oraEs);
				clearRow(ourCourses,rowCount);
				
				//implementazione della disiscrizione all' esame all' esame.
				greetingService.disiscrizioneEsame(user, idEsame, new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore");
						l.log(Level.WARNING, caught.getMessage());
						
					}
					
					@Override
					public void onSuccess(Boolean result) {						
						if (result ==true){
						
							l.log(Level.INFO, "ti sei  disiscritto all'esame del corso: "+ name);
							update(user.getEmail());
						}
						else{
							l.log(Level.WARNING, "Errore nella disiscrizione");
						}
					
					}
					
				});
				
			}});
	}
	
	public void addToAllExams(Grid allCourses,String name,String dip, String cfu,RealStudente user,int idEsame , String dataEs, String oraEs) {
		int rowCount=allCourses.getRowCount();
		addButton = new Button("Iscrivimi");
		allCourses.insertRow(rowCount);
		allCourses.setText(rowCount, 0, name);
		allCourses.setText(rowCount, 1, dip);
		allCourses.setText(rowCount, 2, cfu);
		allCourses.setText(rowCount, 3, dataEs);
		allCourses.setText(rowCount, 4, oraEs);
		allCourses.setWidget(rowCount, 5, addButton);
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addToOurExams(ourExams,name,dip,cfu,user,idEsame,dataEs,oraEs);
				clearRow(allCourses,rowCount);
				
				//implementazione dell' iscirizone all' esame.
				greetingService.iscrizioneEsame(user, idEsame, new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore");
						l.log(Level.WARNING, caught.getMessage());
						
					}
					
					@Override
					public void onSuccess(Boolean result) {
											
						if (result ==true){
						
						l.log(Level.INFO, "ti sei  iscritto all'esame del corso: "+ name);
						update(user.getEmail());
						
						}
						else{
							l.log(Level.WARNING, "Errore");
						}
						
					}
					
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
	
	public void addToRecordExams(Grid recordExams,String name,String dip, String cfu,int idEx) {
		int rowCount=recordExams.getRowCount();
		String strVoto="";
		
		int voto = this.user.getVoto(idEx);
		
		//di solito non dovrebbe accadere che un esame sia pubblicato  senza che tutti i voti vengano inseriti 
		if((voto==-1)||(voto==0)){
			strVoto="il voto deve ancora essere inserito";
		}
		if(voto==1){
			strVoto="non ammesso";
		}
		else{
			strVoto= ""+voto;
		}
		
		recordExams.insertRow(rowCount);
		recordExams.setText(rowCount, 0, name);
		recordExams.setText(rowCount, 1, dip);
		recordExams.setText(rowCount, 2, cfu);
		recordExams.setText(rowCount, 3, strVoto);
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
				l.log(Level.INFO, "E' stato eseguito l'update e ora user.getRegistrazioniEsami()="+user.getRegistrazioniEsami());
				
			}}
			
		});
		return true;
	}
	
	public void setUser(Utente s){
		this.user=(RealStudente)s;
		
	}
}
