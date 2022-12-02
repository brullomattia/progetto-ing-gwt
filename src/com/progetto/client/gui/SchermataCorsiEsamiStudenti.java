package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.interfaces.Studente;

public class SchermataCorsiEsamiStudenti extends Composite{
	private Prove main;
	int idCorso;
	private RealStudente user;
	
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Button backBtn;
	private Grid ourCourses = new Grid(1,3);
	private Grid ourExams = new Grid(1,4);
	private Grid recordExams = new Grid(1,4);
	private HTML stud,list, listex;
	
	private Logger l = Logger.getLogger("Debug");
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	/*
	 * 
	 * in questa schermata viene gestita la visualizzazione degli esami e 
	 * dei corsi a cui uno studente è iscritto (vi ci puo accedere la segreteria).
	 *
	 */
	public SchermataCorsiEsamiStudenti(Prove mainn, Studente s) {
		this.user = (RealStudente) s;
		this.main = mainn;
		
		initWidget(this.principalPanel);
		
		backBtn = new Button("Torna all'elenco studenti");
		stud= new HTML("<b>Studente:  |email:"+s.getEmail()+"matricola: "+s.getMatricola()+"|</b>");
		list = new HTML("<b>Elenco corsi: </b>");
		listex= new HTML("<b>Elenco esami: </b>");
				
		//ourCoursesList for student 
				ourCourses.setWidget(0, 0, new HTML("<b>Nome</b>"));
				ourCourses.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
				ourCourses.setWidget(0, 2, new HTML("<b>Docente</b>"));
				ourCourses.setCellSpacing(10);
				ourCourses.setStyleName("padding-table");
				
				ourExams.setWidget(0, 0, new HTML("<b>Nome</b>"));
				ourExams.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
				ourExams.setWidget(0, 2, new HTML("<b>CFU</b>"));
				ourExams.setCellSpacing(10);
				ourExams.setStyleName("padding-table");
				
				recordExams.setWidget(0, 0, new HTML("<b>Nome</b>"));
				recordExams.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
				recordExams.setWidget(0, 2, new HTML("<b>CFU</b>"));
				recordExams.setWidget(0, 3, new HTML("<b>Voto</b>"));
				recordExams.setCellSpacing(10);
				recordExams.setStyleName("padding-table");
		
		
		
				
		/*
		 * gestione della griglia ourCourses, ovvero l' elenco dei corsi a cui lo studente è iscritto
		 */
		greetingService.getCorsi(user.getIscrizioniCorsi(), new AsyncCallback<ArrayList<RealCorso>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealCorso> result) {
				for(RealCorso c: result) {
					addOurCourses(ourCourses, c.getNome(), c.getDipartimento(), c.getDocente(),user,c.getId());
				}
				
			}
			
		});
		
		
		
		
		/*
		 * gestione delle griglie 
		 * 1-)ourExams, ovvero l' elenco degli esami con esito non ancora pubblicato
		 * 2-)recordExams: ovvero l' elenco degli esami con esito pubblicato
		 */
		greetingService.getEsami(user, new AsyncCallback<ArrayList<RealEsame>>() {

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
						
						
						for(RealCorso c: listaCorsi) {
							
							//troviamo il cfu corrispondente e il voto
							int cfu=0;
							boolean pubblicato=false;
							int voto=0;
								for(RealEsame e: listaEsami){
									if(e.getId()==c.getEsame()){
										cfu=e.getCFU();
										pubblicato=e.isPubblicato();
										voto= user.getVoto(e.getId());
									}	
								}
							
							if(!pubblicato){
								addToOurExams(ourExams, c.getNome(), c.getDipartimento(),""+cfu);
							
							}
							if(pubblicato){
								addToRecordExams(recordExams, c.getNome(), c.getDipartimento(),""+cfu,""+voto);
						
							}
						}
						
					}
					
				});
			}		
			});	
		
		
		this.firstPanel.setSpacing(10);
		principalPanel.add(stud);
		firstPanel.setSpacing(10);
		principalPanel.add(list);
		principalPanel.add(ourCourses);
		
		principalPanel.add(listex);
		principalPanel.add(ourExams);
		principalPanel.add(recordExams);
		firstPanel.setSpacing(10);
		principalPanel.add(backBtn);
		
		backBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new VisualizzaDatiStudente(main));
			}
			
		});
	}
	
	public void addOurCourses(Grid ourCourses,String name, String dip, String doc,RealStudente user,int id) {
		int rowCount = ourCourses.getRowCount();
		ourCourses.insertRow(rowCount);
		ourCourses.setText(rowCount, 0, name);
		ourCourses.setText(rowCount, 1, dip);
		ourCourses.setText(rowCount, 2, doc);
	}
	
	public void addToOurExams(Grid ourCourses,String name, String dip, String cfu) {
		int rowCount = ourCourses.getRowCount();
		ourCourses.insertRow(rowCount);
		ourCourses.setText(rowCount, 0, name);
		ourCourses.setText(rowCount, 1, dip);
		ourCourses.setText(rowCount, 2, cfu);
	}
	
	public void addToRecordExams(Grid recordExams,String name,String dip, String cfu,String voto) {
		int rowCount=recordExams.getRowCount();
		recordExams.insertRow(rowCount);
		recordExams.setText(rowCount, 0, name);
		recordExams.setText(rowCount, 1, dip);
		recordExams.setText(rowCount, 2, cfu);
		recordExams.setText(rowCount, 3, voto);
	}
}
