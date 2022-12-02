package com.progetto.client.gui;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.Prove;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.Utente;

public class SchermataAmministratore extends Composite{
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private HorizontalPanel secondPanel= new HorizontalPanel();
	private Image uniImg;
	private HTML userName;
	private Anchor logout;
	private Image userImg;
	private HTML accessPage;
	private HTML personalDate;
	private Button studentButton;
	private Button profButton;
	private Prove main;

	
	public SchermataAmministratore(Prove mainn, Utente u) {
		this.main = mainn;
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
		
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName = new HTML("<b>" + u.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
			
		
		//HTML  accessPage
		accessPage= new HTML("<b>Accesso Amministratore Dati Personali: </b>");
		
		//HTML personalDate
		personalDate= new HTML("<br>Email: " + u.getEmail()
				+ "<br>Nome: " + u.getName() + "<br>Cognome: " + u.getSurname());
		
		//student account Button
		studentButton = new Button("GESTIONE ACCOUNT STUDENTI");
		studentButton.setPixelSize(100,80);
		studentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneAccountAmministratore(AccountType.Studente, main));
			}
		});
		
		//prof and secretary account button
		profButton = new Button("GESTION ACCOUNT SEGRETERIA /DOCENTI");
		profButton.setPixelSize(100,80);
		profButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneAccountAmministratore(AccountType.Segreteria, main));
			}
		});
		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		// second HPanel
		this.secondPanel.setSpacing(30);
		secondPanel.add(personalDate);
		secondPanel.add(studentButton);
		secondPanel.add(profButton);
		
		//Principal VPanel
		principalPanel.add(firstPanel);
		principalPanel.add(accessPage);
		principalPanel.add(secondPanel);
	}
	
	
}
