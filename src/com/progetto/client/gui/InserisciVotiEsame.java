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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Esame;

public class InserisciVotiEsame extends Composite {
	
	private Prove main;
	int idCorso, idEsame;
	private RealEsame e;
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private TextBox emailStudent = new TextBox();
	private ListBox voti = new ListBox();
	private HTML insertVoti;
	private HTML emailStudente;
	private HTML pubblicaEsame;
	private HTML messageFromDocente;
	private Button insertVoteBtn;
	private Button publishExamBtn;
	private Anchor logout;
	
	
	private Logger l = Logger.getLogger("Debug");
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	public InserisciVotiEsame(Prove mainn, Esame ee) {
		
		
		this.main=mainn;
		this.e = (RealEsame) ee;
		
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		initWidget(this.principalPanel);
		//Button
		insertVoteBtn = new Button("Inserisci Voto"); 
		publishExamBtn = new Button("Pubblica Voti"); 
		
		voti.addItem("Insufficiente");
		voti.addItem("18");
		voti.addItem("19");
		voti.addItem("20");
		voti.addItem("21");
		voti.addItem("22");
		voti.addItem("23");
		voti.addItem("24");
		voti.addItem("25");
		voti.addItem("26");
		voti.addItem("27");
		voti.addItem("28");
		voti.addItem("29");
		voti.addItem("30");
		voti.addItem("31");
		voti.setVisibleItemCount(1);
		
		
		
		insertVoti = new HTML("<b>Voti da inserire:"+"<b>");
		
		//viene visualizzato il testo in cui il docente ha inserito i voti degli esami
		if(e.getMessage().equalsIgnoreCase("")){
			messageFromDocente = new HTML("Ancora nessun messaggio ricevuto dal Docente");
		}
		else{
			messageFromDocente = new HTML(""+e.getMessage());}

		emailStudente = new HTML("<b>Inserire l'email di uno Studente:"+"<b>");
		pubblicaEsame = new HTML("<b>Premere il bottone quando si desidera rendere visibili i voti agli studenti:"+"<b>");		
		
		
		this.firstPanel.setSpacing(10);
		principalPanel.add(logout);
		principalPanel.add(firstPanel);
		principalPanel.add(insertVoti);
		principalPanel.add(messageFromDocente);
		principalPanel.add(emailStudente);
		principalPanel.add(emailStudent);
		principalPanel.add(voti);
		principalPanel.setSpacing(10);
		principalPanel.add(insertVoteBtn);
		principalPanel.add(pubblicaEsame);
		principalPanel.add(publishExamBtn);
		
		insertVoteBtn.addClickHandler(new ClickHandler() {
			
			//nel caso in cui un esame non venga passato, tale esame verrà salvato sul DataBase con voto=1
			@Override
			public void onClick(ClickEvent event) {
				int intVote;
				if(voti.getValue(voti.getSelectedIndex()).equalsIgnoreCase("Insufficiente")){
					intVote = 1;
				}
				else{
					intVote = Integer.valueOf(voti.getValue(voti.getSelectedIndex()));
				}
				
				l.log(Level.INFO, "il voto settato = "+intVote );
				l.log(Level.INFO, "la mail = "+emailStudent.getValue());
				greetingService.inserimentoVoto(e.getId(), intVote,emailStudent.getValue(), new AsyncCallback<Boolean>(){

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Probelma con inserimento voto");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result){
							l.log(Level.WARNING, "Probelma con inserimento voto");
					
						}
						else{
							l.log(Level.INFO, "Voto settato correttamente");
							//controllaVotiStudenti();
							main.setPage(new InserisciVotiEsame( main,e));
						}
					}
				});		
				
			}
			
		});
		publishExamBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				e.setPubblicato(true);
				greetingService.editEsame(e, new AsyncCallback<Boolean>(){

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Probelma con pubblicazione Esame");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result){
							l.log(Level.WARNING, "Probelma con pubblicazione Esame");
					
						}
						else{
							l.log(Level.INFO, "Esame pubblicato correttamente");
							
							main.setPage(new SchermataInserisciVotiSegreteria(main));
						}
					}
				});
				
			}
			
		});
	}
	
	
	
	/*
	 * il seguente medoto è un metodo di verifica, che non ha una funzione operativa nell'
	 * esecuzione del programma
	 */
	public void controllaVotiStudenti() {
		
		ArrayList<AccountType> types = new ArrayList<AccountType>();
		types.add(AccountType.Studente);
		
		greetingService.getUtenti(types, new AsyncCallback< ArrayList<Utente>>(){

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Probelma con controllo dei voti");
				
			}

			@Override
			public void onSuccess( ArrayList<Utente> result) {
				for(Utente u: result){
					RealStudente tmps = (RealStudente) u;
					l.log(Level.INFO, "Email studente= "+ tmps.getEmail()+"Libretto studente= "+ tmps.getLibretto());
				}
			}
		});
		
	}
	
}
