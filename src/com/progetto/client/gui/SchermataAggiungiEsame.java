package com.progetto.client.gui;

import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.interfaces.Corso;
import com.progetto.shared.interfaces.Docente;

public class SchermataAggiungiEsame extends Composite {
	HorizontalPanel principalPanel = new HorizontalPanel();
	VerticalPanel sxPanel = new VerticalPanel();
	VerticalPanel dxPanel = new VerticalPanel();
	HTML examDate;
	HTML examDurate;
	HTML examTime;
	HTML classRoom;
	HTML examCFU;
	HTML exam;
	HTML empty;
	DateBox examDateBox= new DateBox();
	IntegerBox examDurateBox= new IntegerBox();
	TextBox classRoomBox= new TextBox();
	TextBox examTimeBox= new TextBox();
	IntegerBox examCFUBox= new IntegerBox();
	Button saveExamBtn;
	private Prove main;
	int idCorso, idEsame;
	private Docente user;
	private RealCorso c;
	
	private Logger l = Logger.getLogger("Debug");
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public SchermataAggiungiEsame(Prove main, Corso cc, Docente d) {
		//Data attuale
		long millis = System.currentTimeMillis();
		Date today = new Date(millis); 
		this.c = (RealCorso) cc;
		
		this.user =  d;
		this.main=main;
		initWidget(principalPanel);
		empty= new HTML("             ");
		exam= new HTML("<b>Esame"+ c.getNome()+"</b>");
		examDate= new HTML("Data: (deve essere una data futura)");
		examDurate= new HTML("Durata (in minuti) ");
		examTime= new HTML("Orario (stringa) ");
		classRoom= new HTML("Aula");
		examCFU= new HTML("CFU");
		this.idCorso = c.getId();
		
		
		//BUTTON save and exit 
		saveExamBtn= new Button("Aggiungi Esame per il corso "+c.getNome());
		
		//Gestione pannello di sinistra
		principalPanel.setSpacing(10);
		sxPanel.add(exam);
		sxPanel.add(examCFU);
		sxPanel.add(examCFUBox);
		sxPanel.add(classRoom);
		sxPanel.add(classRoomBox);
	
		//Gestione pannello di destra
		dxPanel.add(empty);
		dxPanel.add(examDurate);
		dxPanel.add(examDurateBox);
		dxPanel.add(examDate);
		dxPanel.add(examDateBox);
		dxPanel.add(examTime);
		dxPanel.add(examTimeBox);
		
		//Gestione pannello principale
		principalPanel.add(sxPanel);
		principalPanel.add(dxPanel);
		principalPanel.setSpacing(10);
		principalPanel.add(saveExamBtn);
	
		saveExamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				l.log(Level.INFO, "checkTextBox(today)="+checkTextBox(today));
				
				
				/*
				 * l' esame viene creato e aggiunto al DB, solo se sono stati inseriti tutti 
				 * i dati necessari, in modo corretto
				 */
				if(checkTextBox(today)){
					
					greetingService.addEsameACorso(c, getEsamefromData(), new AsyncCallback<Integer>() {
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "ErroreAddEsame");
							l.log(Level.WARNING, caught.getMessage());
						}
						
						public void onSuccess(Integer result) {
							
							if(result == -1){
								l.log(Level.WARNING, "Errore imprevisto");}
							
							 else{
								 /*
								  * il seguente metodo stampa in console la lista degli esami sul db
								  * in modo da verificare l' inserimento del nuovo esame. inoltre questo metodo fa tornare alla pagina precedente
								  */
								verificaEsami();

							 }
							}
						});
					}
				
			}
		});
	}
	
	
	
	/*
	 * il seguente metodo controlla che i dati inseriti all' interno dei box 
	 * non siano vuoti , e che siano sensati ( la data dell' esame deve essere futura).
	 */
	public Boolean checkTextBox(Date oggi){
	
		return( examCFUBox.getValue()>0&& examDurateBox.getValue()>0
				&& examDateBox.getValue().after(oggi)
				&& !classRoomBox.getText().equalsIgnoreCase("")&& !examTimeBox.getText().equalsIgnoreCase("") );
		}
	
	
	
	public RealEsame getEsamefromData(){	
		
		RealEsame esame = new RealEsame(
		classRoomBox.getText(),
		examDateBox.getValue(), 
		examDurateBox.getValue(),
		idCorso,
		examCFUBox.getValue(),examTimeBox.getText());
		
		return esame;
	}
	
	public void verificaEsami(){
		greetingService.getEsami( new AsyncCallback<ArrayList<RealEsame>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(ArrayList<RealEsame> result) {
				
				
				for(RealEsame e: result){
				
						l.log(Level.INFO," |idEsame: " +e.getId()+" |idDelCorso: " +e.getCorso()+" |CFUCorso: " +e.getCFU()+" |Data: " +e.getData());
					
				}
				main.setPage(new GestioneEsamiDocente(main,user));
				
			}
			});
	}
}
