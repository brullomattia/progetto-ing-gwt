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

public class SchermataSegreteria extends Composite{
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private HorizontalPanel secondPanel= new HorizontalPanel();
	private Image uniImg;
	private HTML userName;
	private Anchor logout;
	private Image userImg;
	private HTML accessPage;
	private Button studentButton;
	private Button examButton;
	private Prove main;
	
	public SchermataSegreteria(Prove mainn) {
		this.main = mainn;
		initWidget(this.principalPanel);
		
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
		
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + "Segreteria" + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		//HTML  accessPage
		accessPage= new HTML("<b>Menu' segreteria: </b>");
		
	
		//button for student date
		studentButton = new Button("VISUALIZZA DATI STUDENTI");
		studentButton.setPixelSize(100,80);
		
		studentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new VisualizzaDatiStudente(main));
			}
		});
		
		//button for insert exam value
		examButton = new Button("INSERIMENTO VOTI STUDENTI");
		examButton.setPixelSize(100,80);
		
		examButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new SchermataInserisciVotiSegreteria(main));
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
		secondPanel.add(studentButton);
		secondPanel.add(examButton);
		

		//Principal VPanel
		principalPanel.add(firstPanel);
		principalPanel.add(accessPage);
		principalPanel.add(secondPanel);
		
	}



}

