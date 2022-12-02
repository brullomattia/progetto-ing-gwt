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
import com.progetto.shared.interfaces.Docente;

public class SchermataDocente extends Composite{
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private HorizontalPanel secondPanel= new HorizontalPanel();
	private Image uniImg;
	private HTML userName;
	private Anchor logout;
	private Image userImg;
	private HTML accessPage;
	private HTML personalDate;
	private Button examButton;
	private Button coursesButton;
	private Prove main;
	
	public SchermataDocente(Prove mainn, Docente d) {
		this.main = mainn;
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
		
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName =new HTML("<b>" + d.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		//HTML  student page
		accessPage= new HTML("<b>Accesso Docente Dati Personali: </b>");
		
		//HTML personalDatee
		personalDate= new HTML("<br>Email:" + d.getEmail()
				+ "<br>Nome:"+ d.getName() + "<br>Cognome:" + d.getSurname());
		
		//ExamButton
		examButton = new Button("GESTIONE ESAMI");
		examButton.setPixelSize(100,80);
		
		//CoursesButton
		coursesButton = new Button("GESTIONE CORSI");
		coursesButton.setPixelSize(100,80);
		
		coursesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneCorsiDocente(main, d));
			}
		});
		
		examButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new GestioneEsamiDocente(main, d));
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
		secondPanel.add(coursesButton);
		secondPanel.add(examButton);
		
		//Principal VPanel
		principalPanel.add(firstPanel);
		principalPanel.add(accessPage);
		principalPanel.add(secondPanel);
	}
}
