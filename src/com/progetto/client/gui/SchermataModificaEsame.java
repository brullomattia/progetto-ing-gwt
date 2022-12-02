package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.DateBox;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.interfaces.Docente;
import com.progetto.shared.interfaces.Esame;


public class SchermataModificaEsame extends Composite {
	private HorizontalPanel principalPanel = new HorizontalPanel();
	private VerticalPanel sxPanel = new VerticalPanel();
	
	//Indicazioni del contenuto di Box
	private HTML obligatory;
	private HTML aulaEx;
	private HTML startDate;
	private HTML duration;
	private HTML CFU;
	private HTML time;
	
	private TextBox aulaBox = new TextBox();
	private TextBox timeBox = new TextBox();
	private DateBox startDateBox = new DateBox();
	private IntegerBox examDurateBox= new IntegerBox();
	private IntegerBox CFUBox= new IntegerBox();
	private Button saveExamBtn;
	private RealDocente user;
	private RealEsame esame;
	private Prove main;
	
	private Logger l = Logger.getLogger("Debug");
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public SchermataModificaEsame(Docente d,Prove mainn, Esame c) {
		this.user = (RealDocente)d;
		this.esame = (RealEsame)c;
		this.main=mainn;
		initWidget(principalPanel);
		DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
		String dataAttuale = fm.format(c.getData());
		
		
		//Indicazioni del contenuto di Box
		obligatory = new HTML("<b>Campi obbligatori Modifica Corso</b>");
		aulaEx = new HTML("Aula:       "+"  (Attuale aula-> "+ esame.getAula()+")" );
		startDate = new HTML("Data di Svolgimento:       "+ "(Attuale data di svolgimento-> "+ dataAttuale+")" );
		duration = new HTML("Durata:       "+ "(Attuale durata-> "+ esame.getDurata() +")");
		CFU = new HTML("CFU:       "+ "(Attuali CFU-> "+ esame.getCFU()+")");
		time = new HTML("Orario:       "+ "(Attuale orario-> "+ esame.getOra()+")");
		
		//BUTTON save and exit 
		saveExamBtn = new Button("Salva & Torna indietro");
		
		//Gestione pannello di sinistra
		sxPanel.add(obligatory);
		sxPanel.add(aulaEx);
		sxPanel.add(aulaBox);
		sxPanel.add(startDate);
		sxPanel.add(startDateBox);
		sxPanel.add(duration);
		sxPanel.add(examDurateBox);
		sxPanel.add(CFU);
		sxPanel.add(CFUBox);
		sxPanel.add(time);
		sxPanel.add(timeBox);
		
		//Gestione pannello principale
		principalPanel.add(sxPanel);
		principalPanel.setSpacing(10);
		principalPanel.add(saveExamBtn);
		
		saveExamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				/*
				 * viene eseguito un controllo dei valori inseriti nei Box: se tali valori sono stati modificati
				 * viene modificata la variabile di istanza RealEsame  con i nuovi valori inseriti.
				 */
				
				if((!aulaBox.getText().equals(""))&&(!aulaBox.getText().equals(esame.getAula()))){
				 esame.setAula(aulaBox.getText());
				}		
				if((!timeBox.getText().equals(""))&&(!timeBox.getText().equals(esame.getOra()))){
					 esame.setOra(timeBox.getText());
					}
				if((startDateBox.getValue()!=null)){
					esame.setData(startDateBox.getValue());
					
					}
				if((!examDurateBox.getText().equalsIgnoreCase(""))&&(examDurateBox.getValue()>0)){
					esame.setDurata(examDurateBox.getValue());
				}
				if((!CFUBox.getText().equalsIgnoreCase(""))&&(CFUBox.getValue()>0)){
					esame.setCFU(CFUBox.getValue());
				}
				
			
			
			
				/*
				 * le modfiche effettuate sulla variabile di istanza RealEsame, 
				 * vengono effettivamente riportate nel BD, tramite il metodo editEsame
				 */
					greetingService.editEsame(esame, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "ErroreEditEsame");
							l.log(Level.WARNING, caught.getMessage());
						}
						
						public void onSuccess(Boolean result) {
							if(!result ){
								l.log(Level.WARNING, "Errore imprevisto sull' edit dell'esame");}
							
							 else{
							
								 main.setPage(new GestioneEsamiDocente(main,user));	
							 }
							}
						});	
				
			}
		});
	}
	
	
	
	/*
	 * metodo di verifica, non necessari per l' esecuzione del programma:
	 * serve solo per verificare che l'Id dell' esame sia coerente con i dati inseriti
	 */
	
	public void verificaEsami(){
		greetingService.getEsami( new AsyncCallback<ArrayList<RealEsame>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(ArrayList<RealEsame> result) {	
				for(RealEsame c: result){
				
						l.log(Level.INFO," |idEsame: " +c.getId()+" |idDelCorso: " +c.getCorso()+" |CFUCorso: " +c.getCFU());	
				}
				
			}
			});
	}
	
	
}