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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.DateBox;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Docente;


public class SchermataAggiungiCorso extends Composite {
	private HorizontalPanel principalPanel = new HorizontalPanel();
	private VerticalPanel sxPanel = new VerticalPanel();
	private VerticalPanel dxPanel = new VerticalPanel();
	private HTML obligatory;
	private HTML optional;
	private HTML name;
	private HTML cfu;
	private HTML startDate;
	private HTML finishDate;
	private HTML description;
	private HTML dip;
	private HTML codocente;
	private HTML examDate;
	private HTML examDurate;
	private HTML classRoom;
	private HTML examTime;
	private HTML exam;
	
	private TextBox nameBox = new TextBox();
	private IntegerBox cfuBox = new IntegerBox();
	private DateBox startDateBox = new DateBox();
	private DateBox finishDateBox = new DateBox();
	private TextBox descriptionBox = new TextBox();
	private TextBox dipBox = new TextBox();
	private TextBox coBox = new TextBox();
	private DateBox examDateBox = new DateBox();
	private IntegerBox examDurateBox = new IntegerBox();
	private TextBox classRoomBox = new TextBox();
	private TextBox examTimeBox = new TextBox();
	private Button saveCourseBtn;
	private RealDocente user;
	private int newCorsoId;
	private Prove main;
	
	private Logger l = Logger.getLogger("Debug");
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public SchermataAggiungiCorso(Docente d,Prove main) {
		this.user = (RealDocente)d;
		this.main=main;
		newCorsoId = -1;
		initWidget(principalPanel);
		
		obligatory = new HTML("<b>Campi obbligatori</b>");
		optional = new HTML("<b>Campi facoltativi</b>");
		name = new HTML("Nome");
		cfu = new HTML("Cfu");
		startDate = new HTML("Data inizio");
		finishDate = new HTML("Data fine (sucessiva a data inizio");
		description = new HTML("Descrizione");
		dip = new HTML("Dipartimento");
		codocente = new HTML("Codocente");
		exam = new HTML("<b>Esame</b>");
		examDate = new HTML("Data");
		examDurate = new HTML("Durata (min)");
		classRoom = new HTML("Aula");
		examTime = new HTML("Orario (stringa)");
		
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
		
		//Gestione pannello di destra
		dxPanel.add(optional);
		dxPanel.add(codocente);
		dxPanel.add(coBox);
		dxPanel.add(exam);
		dxPanel.add(examDate);
		dxPanel.add(examDateBox);
		dxPanel.add(examDurate);
		dxPanel.add(examDurateBox);
		dxPanel.add(classRoom);
		dxPanel.add(classRoomBox);
		dxPanel.add(examTime);
		dxPanel.add(examTimeBox);
		dxPanel.add(cfu);
		dxPanel.add(cfuBox);
		
		//Gestione pannello principale
		principalPanel.add(sxPanel);
		principalPanel.add(dxPanel);
		principalPanel.setSpacing(10);
		principalPanel.add(saveCourseBtn);
		
		saveCourseBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				l.log(Level.INFO, "ALL'INIZIO: User.getCorsiInsegnati()"+user.getCorsiInsegnati());
				l.log(Level.INFO, "ALL'INIZIO: checkTextBox()="+checkTextBox());
				
				//si entra in questo if se i dati obbligatori inseriti sono corretti e NON c'è un esame da aggiungere  
				if(checkTextBox()==0){
				
					greetingService.addCorsoNew(getCorsofromData(),user, new AsyncCallback<Integer>() {
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "ErroreAddCorso");
							l.log(Level.WARNING, caught.getMessage());
						}
						
						public void onSuccess(Integer result) {
							
							if(result == -1){
								l.log(Level.WARNING, "Errore imprevisto add corso new");}
							
							 else{
								 newCorsoId = result;
								 l.log(Level.INFO, "newCorsoId="+newCorsoId);
								update(user.getEmail());
								//checkCod(coBox.getText());
								verificaEsami();
								verificaCorsi();
								
							 }
							}
						});
					}
				
				
				//si entra in questo if se c'è anche un esame da aggiungere
				if(checkTextBox()==1){
					greetingService.addCorsoConEsame(getCorsofromData(), user, getEsamefromData(), new AsyncCallback<Integer>() {
						public void onFailure(Throwable caught) {
							l.log(Level.WARNING, "Errore");
							l.log(Level.WARNING, caught.getMessage());
						}
						
						public void onSuccess(Integer result) {

							if(result == -1){
								l.log(Level.WARNING, "Errore imprevisto add corso con esame");}
							
							 else{
							 newCorsoId = result;
							 l.log(Level.INFO, "newCorsoId="+newCorsoId);
							 update(user.getEmail());
							 verificaCorsi();
							 verificaEsami();
							}
						}
					});
				}
				
				if((checkTextBox()!=0)&&(checkTextBox()!=1)){
					l.log(Level.WARNING,"Le iformazioni inserite non sono corrette! nomeCorso: " +nameBox.getText()+"|descriptionBox: " +
				descriptionBox.getText()+"|cfuBox: " +cfuBox.getText());
				}
				
				
				
			}
		});
	}
	
	
	
	//ritorna -1 se i campi NON sono stati inseriti correttamente , 0 se non è stato inserito l' esame, 1 se è stato iserito l' esame 
	public int checkTextBox()
	{
		int result = -1;
		
		//se i dati del corso sono corretti il result vale 0
				if(!nameBox.getText().equals("") &&
				(startDateBox.getValue().before(finishDateBox.getValue())) &&
				!descriptionBox.getText().equals("") &&
				!dipBox.getText().equals(""))
				
				{result ++;}
		
		
		
		//se i dati dell' esame sono corretti il result vale 1
				if(( result ==0 && examDurateBox!=null && 
					examDateBox!=null && !classRoomBox.getText().equals("") && cfuBox.getValue()>0 &&
					!examTimeBox.getText().equals(""))
					&&(examDurateBox.getValue()>0 && (examDateBox.getValue().after(finishDateBox.getValue()))))
				
				{result ++;}			
		
		return result;
	}
	
	public RealCorso getCorsofromData()
	{
		RealCorso corso = new RealCorso(
		nameBox.getText(),
		startDateBox.getValue(), 
		finishDateBox.getValue(),
		user.getEmail(),
		coBox.getText(),
		descriptionBox.getText(),
		dipBox.getText(),
		-1);
		
		return corso;
	}

	public RealEsame getEsamefromData(){
		
		
		RealEsame esame = new RealEsame(
		classRoomBox.getText(),
		examDateBox.getValue(), 
		examDurateBox.getValue(),
		-1, cfuBox.getValue(), examTimeBox.getText());
		
		return esame;
	}
	
	
	/*
	 * prima viene modificato il corso, senza tanenere conto del contenuto di coBox ( il box dentro il quale deve essere indicata la mail del codcoente),
	 * e poi, solo dopo aver modificato il corso, viene impostata la Codocenza tramite il metodo setCod().
	 */
	public void setCod(){		

		if(coBox.getText()!= user.getEmail() && !coBox.getText().equalsIgnoreCase("")){
			greetingService.setCodocente(newCorsoId, coBox.getText(), new AsyncCallback<Boolean>() {

				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
					
				}
				
				@Override
				public void onSuccess(Boolean result) {
					if (!result){
						l.log(Level.WARNING, "Errore Imprevisto set codocente");			
					}
					else{
						l.log(Level.INFO, "è stato settato il codocente in modo corretto");
						verificaCodocente();
					}
					
				}
				
			});
		}
		Docente doc = this.user;
		main.setPage(new GestioneCorsiDocente(main,doc));
	}
	
	/*il metodo setUser() e update() servono per far si che il corso appena aggiunto compaia immediatamente
	 *  NON SOLO nel DB , ma anche come corso insegnato nella variabile di istanza "user" di tipo RealDocente
	 *  inoltre dopo l'update della variabile "user" viene anche impostato l' eventuale codocente
	 */
	public void setUser(Utente d){
		this.user=(RealDocente)d;
		l.log(Level.INFO, "abbiamo eseguito l'update di "+ user.getEmail()+"ora user.getCorsiInsegnati()="+this.user.getCorsiInsegnati());
		setCod();
	}
	
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
						
			}}
			
		});
		return true;
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