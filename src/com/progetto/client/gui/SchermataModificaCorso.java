package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.DateBox;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Corso;
import com.progetto.shared.interfaces.Docente;


public class SchermataModificaCorso extends Composite {
	private HorizontalPanel principalPanel = new HorizontalPanel();
	private VerticalPanel sxPanel = new VerticalPanel();
	
	//Indicazioni del contenuto di Box
	private HTML obligatory;
	private HTML name;
	private HTML startDate;
	private HTML finishDate;
	private HTML description;
	private HTML dip;
	private HTML codocente;
	
	private TextBox nameBox = new TextBox();
	private DateBox startDateBox = new DateBox();
	private DateBox finishDateBox = new DateBox();
	private TextBox descriptionBox = new TextBox();
	private TextBox dipBox = new TextBox();
	private TextBox coBox = new TextBox();
	private Button saveCourseBtn;
	private RealDocente user;
	private RealCorso corso;
	private int corsoId;
	private Prove main;
	
	private Logger l = Logger.getLogger("Debug");
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public SchermataModificaCorso(Docente d,Prove main, Corso c) {
		this.user = (RealDocente)d;
		this.corso = (RealCorso) c;
		this.main=main;
		corsoId = c.getId();
		initWidget(principalPanel);
		
		
		//Indicazioni del contenuto di Box
		obligatory = new HTML("<b> Modifica Corso</b>");
		name = new HTML("Nome:       "+"  (Attuale nome-> "+ corso.getNome()+")" );
		startDate = new HTML("Data inizio:       "+ "(Attuale data di inizio-> "+ corso.getDataInizio()+")" );
		finishDate = new HTML("Data fine:       "+ "(Attuale data di fine-> "+ corso.getDataFine()+")" );
		description = new HTML("Descrizione:       "+ "(Attuale descizione-> "+ corso.getDescrizione() +")");
		dip = new HTML("Dipartimento:       "+ "(Attuale Dipartimento-> "+ corso.getDipartimento()+")");
		codocente = new HTML("Codocente:       "+ "(Attuale codocente-> "+ corso.getCoDocente()+")Inserire la mail del Codocente che si desidera impostare. Digitare 'Elimina' se si desidera eliminare l' attuale Codocente" );
		
		//BUTTON save and exit 
		saveCourseBtn = new Button("Salva & Torna indietro");
		
		//Gestione pannello di sinistra
		sxPanel.add(obligatory);
		sxPanel.add(name);
		sxPanel.add(nameBox);
		sxPanel.add(startDate);
		sxPanel.add(startDateBox);
		sxPanel.add(finishDate);
		sxPanel.add(finishDateBox);
		sxPanel.add(description);
		sxPanel.add(descriptionBox);
		sxPanel.add(dip);
		sxPanel.add(dipBox);
		sxPanel.add(codocente);
		sxPanel.add(coBox);
		
		//Gestione pannello principale
		principalPanel.add(sxPanel);
		principalPanel.setSpacing(10);
		principalPanel.add(saveCourseBtn);
		
		saveCourseBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				/*
				 * viene eseguito un controllo dei valori inseriti nei Box: se tali valori sono stati modificati
				 * e sono sensati (es: la data di fine corso deve essere sucessiva alla data di inzio)
				 * viene modificato la variabile di istanza RealCorso  con i nuovi valori inseriti.
				 */
				
				if((!nameBox.getText().equals(""))&&(!nameBox.getText().equals(corso.getNome()))){
				 corso.setNome(nameBox.getText());
				}			
				if((startDateBox.getValue()!=null && finishDateBox.getValue()!=null)&&
				(startDateBox.getValue().before(finishDateBox.getValue()))){
					corso.setDataInizio(startDateBox.getValue()); 
					corso.setDataFine(finishDateBox.getValue());
					}
				if((!descriptionBox.getText().equals(""))&&(!descriptionBox.getText().equals(corso.getDescrizione()))){
					 corso.setDescrizione(descriptionBox.getText());
					}
				if((!dipBox.getText().equals(""))&&(!dipBox.getText().equals(corso.getDipartimento()))){
					 corso.setDipartimento(dipBox.getText());
					}
				
			
			
			
				/*
				 * le modfiche effettuate sulla variabile di istanza RealCorso, 
				 * vengono effettivamente riportate nel BD, tramite il metodo editCorso
				 */
					greetingService.editCorso(corso, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "ErroreEditCorso");
							l.log(Level.WARNING, caught.getMessage());
						}
						
						public void onSuccess(Boolean result) {
							if(!result ){
								l.log(Level.WARNING, "Errore imprevisto sull' edit del corso");}
							
							 else{
								setCod();	
							 }
							}
						});	
				
			}
		});
	}
	
	
	
	
	
	
	
	/*
	 * prima viene modificato il corso, senza tanenere conto del contenuto di coBox ( il box dentro il quale deve essere indicata la mail del codcoente),
	 * e poi, solo dopo aver modificato il corso, viene impostata la Codocenza tramite il metodo setCod().
	 */
	public void setCod(){		
		
		if(!coBox.getText().equals(user.getEmail())&&!coBox.getText().equalsIgnoreCase("")){
			
			//se viene inserita la parola "elimina" , viene eliminata la codocenza...
				if((coBox.getText().equalsIgnoreCase("elimina"))&&(!corso.getCoDocente().equals(""))){
					greetingService.removeCodocenza(corsoId, corso.getCoDocente(), new AsyncCallback<Boolean>() {
		
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "Errore");
							l.log(Level.WARNING, caught.getMessage());
							
						}
						
						@Override
						public void onSuccess(Boolean result) {
							if (!result){
								l.log(Level.WARNING, "Problemi nell' eliminare la codcocenza");			
							}
							else{
								l.log(Level.INFO, "è stata eliminata la codocenza");
								//verificaEsami();
								//verificaCorsi();
								Docente doc = user;
								main.setPage(new GestioneCorsiDocente(main,doc));
							}
							
						}
						
					});
				}
				
				//altrimenti viene inserita la codocenza nel Db
				else{
					greetingService.setCodocente(corsoId, coBox.getText(), new AsyncCallback<Boolean>() {
		
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "Errore");
							l.log(Level.WARNING, caught.getMessage());
							
						}
						
						@Override
						public void onSuccess(Boolean result) {
							if (!result){
								l.log(Level.WARNING, "Il codocente inserito è nullo o inesistente");			
							}
							else{
								l.log(Level.INFO, "è stato settato il codocente in modo corretto");
								//verificaEsami();
								//verificaCorsi();
								//verificaCodocente();
								Docente doc = user;
								main.setPage(new GestioneCorsiDocente(main,doc));
							}
							
						}
						
					});
				}
		}
		else{
			Docente doc = this.user;
			main.setPage(new GestioneCorsiDocente(main,doc));
		}
	}
	

	
	
	
	/*
	 * metodi di verifica, non necessari per l' esecuzione del programma:
	 */
	
	
	
	//questo metodo serve solo per verificare che  sia stato aggiunto il corso nei coInsegnati del codocente
	public boolean verificaCodocente(){
		
		greetingService.getUtente(coBox.getText(), new AsyncCallback<Utente>() {

			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());		
			}
			
			@Override
			public void onSuccess(Utente result) {
				if (result !=null){
					RealDocente tmpcod = (RealDocente) result;
					l.log(Level.INFO, "codocente.corsiCoInsegnati = "+ tmpcod.getCorsiCoInsegnati()+ "codocente.getNome() = "+ tmpcod.getName());			
			}}
			
		});
		return true;
	}
	
	

	
	//questa parte di codice serve solo per verificare che effettivamente sia stato aggiunto il corso
	public void verificaCorsi(){
		
		greetingService.getCorsi( new AsyncCallback<ArrayList<RealCorso>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(ArrayList<RealCorso> result) {
				
				for(RealCorso c: result){
				
						l.log(Level.INFO,"NomeCorso: " +c.getNome()+" |idCorso: " +c.getId()+" |idEsameDelCorso: " +c.getEsame()+" |DocenteCorso: " 
						+c.getDocente()+" |Codocente: "+c.getCoDocente()+ " |Dipartimento Corso: " +c.getDipartimento());
					
				}
			}
			});
	}
	
	//questa parte di codice serve solo per verificare che l'Id degli esami sia coerente con i dati inseriti
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