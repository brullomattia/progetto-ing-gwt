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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealAmministratore;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealSegreteria;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;

public class SchermataCreaOModificaAccount extends Composite {
	HorizontalPanel principalPanel = new HorizontalPanel();
	VerticalPanel sxPanel = new VerticalPanel();
	VerticalPanel dxPanel = new VerticalPanel();
	HTML name;
	HTML prename;
	HTML email;
	HTML type;
	HTML id;
	HTML title;
	HTML password;
	HTML empty;
	
	TextBox nameBox = new TextBox();
	TextBox prenameBox = new TextBox();
	TextBox emailBox = new TextBox();
	ListBox typeBox = new ListBox();
	TextBox idBox = new TextBox();
	PasswordTextBox passwordBox = new PasswordTextBox();
	Button addAccountBtn;
	private Prove main;
	
	private String currentName;
	private String currentSurname;
	private String currentEmail;
	private AccountType currentAccountType;
	private String currentMatricola;
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	
	//costruttore in caso di CREAZIONE di un nuovo account (indipendentemente dal tipo)
	public SchermataCreaOModificaAccount(Prove main, AccountType ty, boolean isnew) {
		this.main = main;
		this.currentAccountType = ty;
		
		loadPagina(isnew);
	}
	
	//costruttore in caso di MODIFICA di account Segreteria o Docente
	public SchermataCreaOModificaAccount(Prove main, String Name, String Surname, String Email, AccountType TipologiaAcc) {
		this.main = main;
		
		this.currentName = Name;
		this.currentSurname = Surname;
		this.currentEmail = Email;
		this.currentAccountType = TipologiaAcc;
		
		loadPagina(false);
	}
	
	//costruttore in caso di MODIFICA di account Studente (si tiene conto del numero di matricola)
	public SchermataCreaOModificaAccount(Prove main, String Name, String Surname, String Email, String Matricola) {
		this.main = main;	
		
		this.currentName = Name;
		this.currentSurname = Surname;
		this.currentEmail = Email;
		this.currentMatricola = Matricola;
		this.currentAccountType = AccountType.Studente;
		
		loadPagina(false);
	}
	
	public void loadPagina(boolean isnew)
	{
		initWidget(principalPanel);
		
		name = new HTML("Nome");
		prename = new HTML("Cognome");
		email = new HTML("Email");
		type = new HTML("TipologiaAcc");
		id = new HTML("Matricola");
		password = new HTML("Password");
		title = new HTML("<b>Modifica Account</b>");
		empty = new HTML("_");
		
		typeBox.addItem("Docente");
		typeBox.addItem("Segreteria");
		
		
		//BUTTON addAccount è utilizzato sia in caso di Creazione che in caso di modifica di accunt già esistente
		if(isnew)
		{
			addAccountBtn = new Button("Aggiungi Account");
		}
		else
		{
			addAccountBtn = new Button("Modifica Account");
		}
		
		addAccountBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String newname = nameBox.getText();
				String newsurname = prenameBox.getText();
				String newemail = emailBox.getText();
				String newpassword = passwordBox.getText();
				
				if(!newname.equals("") & !newsurname.equals("") & !newemail.equals("") & !newpassword.equals(""))
				{
					if(currentAccountType == AccountType.Studente && !idBox.getText().equals(""))
					{
						String newmatricola = idBox.getText();
						sendUsertoServer(new RealStudente(newname, newsurname, newemail, newmatricola, newpassword), currentEmail, isnew,AccountType.Studente);
						
					}
					else
					{
						AccountType newType = AccountType.getFromString(typeBox.getSelectedItemText());
						sendUsertoServer(createUtente(newname, newsurname, newemail, newpassword, newType), currentEmail, isnew,AccountType.Segreteria);
						
					}
				}
			}
		});
		
		//Gestione pannello di sinistra
		sxPanel.add(title);
		sxPanel.add(name);
		sxPanel.add(nameBox);
		sxPanel.add(prename);
		sxPanel.add(prenameBox);
		sxPanel.add(email);
		sxPanel.add(emailBox);
		sxPanel.setSpacing(10);
	
		dxPanel.add(empty);
		
		if(currentAccountType == AccountType.Studente)
		{	
			//Gestione pannello di destra
			dxPanel.add(id);
			dxPanel.add(idBox);
		}
		else
		{
			//Gestione pannello di destra
			dxPanel.add(type);
			dxPanel.add(typeBox);
		}
		
		dxPanel.add(password);
		dxPanel.add(passwordBox);
		dxPanel.add(addAccountBtn);
		dxPanel.setSpacing(10);
	
		//Gestione pannello principale
		principalPanel.add(sxPanel);
		principalPanel.add(dxPanel);
		
		
		if(!isnew)
		{
			nameBox.setText(currentName);
			prenameBox.setText(currentSurname);
			emailBox.setText(currentEmail);
			
			if(currentAccountType == AccountType.Docente)
			{
				typeBox.setItemSelected(0, true);
			}
			if(currentAccountType == AccountType.Segreteria)
			{
				typeBox.setItemSelected(1, true);
			}
			if(currentAccountType == AccountType.Studente)
			{
				idBox.setText(currentMatricola);
			}
		}
	}
	
	/*
	 * la variabile booleana isnew fa si che il metodo sendUsertoServer() possa essere utiilizzato
	 * sia per la creazione che per la modifica di un Utente
	 */
	public void sendUsertoServer(Utente u, String oldEmail, boolean isnew, AccountType tyy)
	{
		if(isnew)
		{
			greetingService.addUtente(u, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
				}

				public void onSuccess(Boolean result) {
					if(result != true)
					{
						l.log(Level.WARNING, "Errore imprevisto");
					}
					else{
						l.log(Level.INFO, "Utente Creato Con Successo");
						main.setPage(new GestioneAccountAmministratore(tyy, main));
						
						
						/*
						 * verificaAccounts() è un metodo di controllo, 
						 * non necesssario ai fini dell' esecuzione
						 */
						
						verificaAccounts();
					}
				}
			});
		}
		else
		{
			greetingService.editUtente(u, oldEmail, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
				}

				public void onSuccess(Boolean result) {
					if(result != true)
					{
						l.log(Level.WARNING, "Errore imprevisto");
					}
					else{
						l.log(Level.INFO, "Utente modificato Con Successo");
						main.setPage(new GestioneAccountAmministratore(tyy, main));
						/*
						 * verificaAccounts() è un metodo di controllo, 
						 * non necesssario ai fini dell' esecuzione
						 */
						
						verificaAccounts();
					}
				}
			});
		}
		
	}
	
	public Utente createUtente(String name, String surname, String email, String password, AccountType type)
	{
		if(type == AccountType.Docente)
		{
			return new RealDocente(name, surname, email, password);
		}
		if(type == AccountType.Segreteria)
		{
			return new RealSegreteria(name, surname, email, password);
		}
		
		return null;
	}
	
	

	
	
	
	/*
	 * il metodo verificaAccounts() ha il solo scopo di visualizzare in console dei logs contenenti
	 *  un elenco degli account presenti sul DB: è un metodo di verifica che non ha responsabilità 
	 *  di funzionamento del programma 
	 */
	private void verificaAccounts(){
		ArrayList<AccountType> types = new ArrayList<AccountType>();
		types.add(AccountType.Studente);types.add(AccountType.Docente);
		types.add(AccountType.Segreteria);types.add(AccountType.Amministratore);
		
			greetingService.getUtenti(types, new AsyncCallback<ArrayList<Utente>>() {
				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
				}

				public void onSuccess(ArrayList<Utente> result) {
					if(result != null)
					{l.log(Level.INFO, "ELENCO AL TERMINE DELLA MODIFICA/CREAZIONE DI UN ACCOUNT");
						
					for(Utente u : result){					
							l.log(Level.INFO, "|EMAIL: ->"+u.getEmail()+"|NAME: ->"+u.getName()+
									"|SURNAME: ->"+u.getSurname()+"|TYPE: ->"+u.getType());
						}
						
					}
					else
					{
						l.log(Level.WARNING, "Nessun utente ricevuto");
					}
				}
			});
		
	}
}
	




