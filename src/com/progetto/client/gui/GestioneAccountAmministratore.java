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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Studente;

public class GestioneAccountAmministratore extends Composite{
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel = new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private FlexTable accountList = new FlexTable();
	
	private Button editBtn;
	private Button deleteBtn;
	private Button addAccountBtn;
	private HTML listAcc;
	private Prove main;
	private ArrayList<Utente> accounts = new ArrayList<Utente>();
	private AccountType type;
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	/*
	 * Questa pagina gestisce la visualizzazione degli 
	 * account registrati alla piattaforma.
	 * A seconda del parametro AccountType che viene passato al costruttore , si distinguono due casi:
	 * 1-) viene visualizzata la lista degli account "Studente"
	 * 2-) viene visualizzata una lista con tutti gli account "Docente" e "Segreteria".
	 * 
	 */

	
	public GestioneAccountAmministratore(AccountType type, Prove main) {
		this.main = main;
		this.type = type;
		
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
		
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + "Amministratore" + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		//BTN addAccount
		addAccountBtn = new Button("Aggiungi account");
		
		/*
		 * Utilizziamo un Arraylist<AccountType> perchè nel caso di Docente e Segreteria,
		 *  vogliamo visualizzare tutti gli account relativi a 2 diversi AccountType
		 */
		ArrayList<AccountType> selectedtypes = new ArrayList<AccountType>();
		
		
		if(type == AccountType.Studente)
		{

			accountList.setWidget(0, 0, new HTML("<b>Email</b>"));
			accountList.setWidget(0, 1, new HTML("<b>Matricola</b>"));
			accountList.setWidget(0, 2, new HTML("<b>Nome</b>"));
			accountList.setWidget(0, 3, new HTML("<b>Cognome</b>"));
			accountList.setWidget(0, 4, null);
			accountList.setWidget(0, 5, null);
			
			selectedtypes.add(AccountType.Studente);
			getUtenti(selectedtypes);
			
			addAccountBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					main.setPage(new SchermataCreaOModificaAccount(main, AccountType.Studente, true));
				}
			});
		}
		if(type == AccountType.Segreteria || type == AccountType.Docente)
		{
			accountList.setWidget(0, 0, new HTML("<b>Email</b>"));
			accountList.setWidget(0, 1, new HTML("<b>TipoAccount</b>"));
			accountList.setWidget(0, 2, new HTML("<b>Nome</b>"));
			accountList.setWidget(0, 3, new HTML("<b>Cognome</b>"));
			accountList.setWidget(0, 4, null);
			accountList.setWidget(0, 5, null);
			
			selectedtypes.add(AccountType.Segreteria);
			selectedtypes.add(AccountType.Docente);
			getUtenti(selectedtypes);
			
			addAccountBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					/*
					 * sia per aggiungere un account Docente, che per aggiungere un account Segreteria
					 * viene passato un AccountType.Segreteria, alla pagina successiva (sucessivamente
					 * si potrà specificare il tipo di account che si desidera creare) 
					 */
					main.setPage(new SchermataCreaOModificaAccount(main, AccountType.Segreteria, true));
				}
			});
		}
		
		accountList.setCellSpacing(10);
		accountList.setStyleName("padding-table");
		
		//HTML userCourses
		listAcc = new HTML("<b>Lista account: </b>");
		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(listAcc);
		principalPanel.add(accountList);
		principalPanel.add(addAccountBtn);	
	}
	
	//questo metodo carica dal Db tutti gli account dei tipi inseriti come input, 
	//e li aggiunge alla lista con il metodo updateTable()
	public void getUtenti(ArrayList<AccountType> types)
	{	
		greetingService.getUtenti(types, new AsyncCallback<ArrayList<Utente>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}

			public void onSuccess(ArrayList<Utente> result) {
				if(result != null)
				{
					for(Utente u : result)
					{
						accounts.add(u);
					}
					updateTable();
				}
				else
				{
					l.log(Level.WARNING, "Nessun utente ricevuto");
				}
			}
		});
	}

	public boolean onlyType(ArrayList<AccountType> types, AccountType check)
	{
		for(AccountType tmp : types)
		{
			if(tmp != check)
			{
				return false;
			}
		}
		return true;
	}	
	
	public void updateTable() {
		for(Utente u : accounts)
		{	
			int rowCount = accountList.getRowCount();
			editBtn = new Button("Modifica");
			deleteBtn= new Button("Elimina");
			accountList.insertRow(rowCount);
			accountList.setText(rowCount, 0, u.getEmail());
			
			//per gli studenti viene inserita la matricola, per Docenti e Segreteria viene inserito il tipo di account
			if(type == AccountType.Studente)
			{
				accountList.setText(rowCount, 1, ((Studente)u).getMatricola());
			}
			else
			{
				accountList.setText(rowCount, 1, u.getType());
			}
			
			accountList.setText(rowCount, 2, u.getName());
			accountList.setText(rowCount, 3, u.getSurname());
			accountList.setWidget(rowCount, 4, editBtn);
			accountList.setWidget(rowCount, 5, deleteBtn);
			
			editBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Utente tmp = accounts.get(accountList.getCellForEvent(event).getRowIndex() - 1);
					
					
					/*
					 * la pagina "SchermataCreaOModificaAccount()" ha costruttori diversi a seconda 
					 * del tipo di Account che si vuole modificare: uno per gli studenti (che tiene conto del 
					 * numero di matricola) e uno in comune per Docenti e Segreteria (che prende in input un valore AccountType)
					 */
					if(tmp instanceof Studente)
					{
						Studente s = ((Studente)tmp);
						main.setPage(new SchermataCreaOModificaAccount(main, s.getName(), s.getSurname(), s.getEmail(), s.getMatricola()));	
					}
					else
					{
						main.setPage(new SchermataCreaOModificaAccount(main, u.getName(), u.getSurname(), u.getEmail(), AccountType.getFromString(u.getType())));
					}	
				}});
			
			
			//il tasto delete esegue le stesse procedure a prescindere dal tipo di account
			deleteBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int rowNumber = accountList.getCellForEvent(event).getRowIndex();
					//La lista utenti è una posizione indietro dato che la tabella ha anche la prima
					//riga con i nomi dei campi
					Utente tmp = accounts.get(rowNumber - 1);			
					clearRow(accountList, rowNumber);
					removeAccount(tmp);
				}
			});	
		}	
	}
	
	
	public void removeAccount(Utente u)
	{
		greetingService.removeUtente(u, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				if(!result)
				{
					l.log(Level.WARNING, "Errore inaspettato sul metodo \"removeAccount()\"");
				}
				else{
					l.log(Level.INFO, "Utente rimosso con successo");
				}
			}
		});
	}
	
	public void clearRow(FlexTable emailList,int rowCount) {
		emailList.setText(rowCount, 0, "");
		emailList.setText(rowCount, 1, "");
		emailList.setText(rowCount, 2, "");
		emailList.setText(rowCount, 3, "");
		emailList.setWidget(rowCount, 4, null);
		emailList.setWidget(rowCount, 5, null);
	}
}