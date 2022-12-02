package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealDipartimento;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Studente;

public class SchermataIniziale extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private FlexTable flexTable = new FlexTable();
	private HorizontalPanel hPanel = new HorizontalPanel();
	private TextBox idTxt;
	private PasswordTextBox pwTxt;
	private VerticalPanel vPanelLogin = new VerticalPanel();
	private Image img;
	private HTML dipText;
	private HTML contactText;
	private Button loginButton;
	private HTML id;
	private HTML pw;
	private Prove main;
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	
	public SchermataIniziale(Prove main) {
		this.main = main;
		 
		
		
		/*
		 * scommentare la seguente riga di codice se si desidera eseguire il metodo 
		 * verificaAccounts(), descritto a fondo pagina.
		 */
		//verificaAccounts();
		
		initWidget(this.vPanel);
		this.hPanel.setSpacing(10);
		//UNIBO image
		try
		{img = new Image("images/unibo.png");}
		catch(Exception e){};
		
		//HTML text
		dipText = new HTML("<b>I NOSTRI DIPARTIMENTI: </b>");
		
		//FlexTable
		flexTable.setHTML(0, 0, "<b>Nome</b>");
		flexTable.setHTML(0, 1, "<b>Docente</b>");
		flexTable.setHTML(0, 2, "<b>Sede</b>");

		flexTable.setBorderWidth(1);
		flexTable.setCellSpacing(10);
		flexTable.setStyleName("padding-table");
		hPanel.add(flexTable);
		loadDipartimenti();
		
		//HTML TEXT
		contactText= new HTML("<b>Contatti:</b><br>Email: aiuto@unibo.it<br>Telefono: +39 051 2080310<br>Indirizzo: Via Zamboni 33");
		hPanel.add(contactText);
		
		hPanel.add(vPanelLogin);
		
		//ID Text Box
		idTxt = new TextBox();
		id = new HTML("<b>Id</b>");
		vPanelLogin.add(id);
		vPanelLogin.add(idTxt);
		
		//PW TextBox
		pwTxt= new PasswordTextBox();
		pw = new HTML("<b>Password</b>");
		
		vPanelLogin.add(pw);
		vPanelLogin.add(pwTxt);
		
		//LOGIN BUTTON
		loginButton = new Button("ACCESSO CON CREDEZIALI D'ATENEO");
		loginButton.setPixelSize(100,80);
		hPanel.add(loginButton);
		
		loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login();
			}
		});
		idTxt.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					login();
				}	
			}
		});
		pwTxt.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					login();
				}	
			}
		});
		
		vPanel.add(img);
		vPanel.add(dipText);
		vPanel.add(hPanel);
	}
	
	public void aggiungiRiga(FlexTable flexTable, String nome, String docente, String sede){
		int nextrow = flexTable.getRowCount();
		flexTable.setText(nextrow, 0, nome);
		flexTable.setText(nextrow, 1, docente);
		flexTable.setText(nextrow, 2, sede);
	}
	
	public void loadDipartimenti()
	{
		greetingService.getDipartimenti(new AsyncCallback<ArrayList<RealDipartimento>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(ArrayList<RealDipartimento> result) {
				if(result != null)
				{
					for(RealDipartimento d : result)
					{
						aggiungiRiga(flexTable, d.getNome(), d.getCoordinatore(), d.getSede());
					}
				}
			}
		});
	}
	
	public void login()
	{
		
		
		greetingService.authenticate(idTxt.getText(), pwTxt.getText(), new AsyncCallback<Utente>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
			}

			public void onSuccess(Utente result) {
				if(result != null)
				{
					l.log(Level.INFO, "Successo");
					
					if(result != null)
					{
						main.setPage(result.getWebpage(main));
					}
				}
				else
				{
					l.log(Level.WARNING, "Credenziali errate");
				}
			}
		});
	}

	
	
	
	
	
	
	
	/*
	 * il seguente metodo "verificaAccounts()" è un metodo di prova:
	 * è possibile scommentarlo  se si desidera  visualizzare in console
	 * un elenco di tutti gli utenti presenti sul database, ogni volta che si ritorna alla
	 * pagina iniziale.
	 */
	
	private void verificaAccounts(){
		ArrayList<AccountType> types = new ArrayList<AccountType>();
		types.add(AccountType.Studente);
		types.add(AccountType.Docente);
		types.add(AccountType.Segreteria);
		types.add(AccountType.Amministratore);
		
			greetingService.getUtenti(types, new AsyncCallback<ArrayList<Utente>>() {
				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
				}

				public void onSuccess(ArrayList<Utente> result) {
					if(result != null)
					{
						l.log(Level.INFO, "Verifica Accounts dalla pagina iniziale");
						for(Utente u : result)
						{
							l.log(Level.INFO, "|EMAIL: ->"+u.getEmail()+"|NAME: ->"+u.getName()+
									"|SURNAME: ->"+u.getSurname()+"|TYPE: ->"+u.getType());
							if (u instanceof Studente){
								RealStudente tmp = (RealStudente) u;
								l.log(Level.INFO, "tmp.getLibretto()"+tmp.getLibretto());
								l.log(Level.INFO, "tmp.getRegistrazioniEsami()"+tmp.getRegistrazioniEsami());
							}
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

	
