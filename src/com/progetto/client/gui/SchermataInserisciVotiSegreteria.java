package com.progetto.client.gui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
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
import com.progetto.shared.concrete.RealEsame;

public class SchermataInserisciVotiSegreteria extends Composite{
	
	private VerticalPanel principalPanel = new VerticalPanel();
	private HorizontalPanel firstPanel = new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private FlexTable examsList = new FlexTable();	
	private Button editBtn;
	private HTML email;
	private Prove main;
	private ArrayList<RealEsame> exams = new ArrayList<RealEsame>();

	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	

	
	public SchermataInserisciVotiSegreteria( Prove main) {
		this.main = main;
		
		
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
		
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + "Segreteria" + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		
		
		ArrayList<AccountType> selectedtypes = new ArrayList<AccountType>();
		
			//ourCoursesList for student 
			examsList.setWidget(0, 0, new HTML("<b>ID Esame</b>"));
			examsList.setWidget(0, 1, new HTML("<b>CFU</b>"));
			examsList.setWidget(0, 2, new HTML("<b>Aula</b>"));
			examsList.setWidget(0, 3, new HTML("<b>Data</b>"));
			examsList.setWidget(0, 4, null);
			examsList.setWidget(0, 5, null);
			
			selectedtypes.add(AccountType.Studente);
			getUtenti(selectedtypes);
	
		
		examsList.setCellSpacing(10);
		examsList.setStyleName("padding-table");
		
		//HTML userCourses
		email = new HTML("<b>Lista Esami: </b>");
		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(email);
		principalPanel.add(examsList);
	}
	
	public void getUtenti(ArrayList<AccountType> types)
	{	
		greetingService.getEsami(new AsyncCallback<ArrayList<RealEsame>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}

			public void onSuccess(ArrayList<RealEsame> result) {
				if(result != null)
				{
					for(RealEsame e : result)
					{	
						if(e.isPubblicato()==false){
							exams.add(e);}
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
	

	
	public void updateTable() {
		DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
		String dataEsame = "";
		
		for(RealEsame u : exams)
		{	
			dataEsame =  fm.format(u.getData());
			int rowCount = examsList.getRowCount();
			editBtn = new Button("Inserisci Voti");
			examsList.insertRow(rowCount);
			examsList.setText(rowCount, 0, Integer.toString(u.getId()));
			examsList.setText(rowCount, 1, Integer.toString(u.getCFU()));			
			examsList.setText(rowCount, 2, u.getAula());
			examsList.setText(rowCount, 3, dataEsame);
			examsList.setWidget(rowCount, 4, editBtn);
			
			editBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					RealEsame tmp = exams.get(examsList.getCellForEvent(event).getRowIndex() - 1);
						main.setPage(new InserisciVotiEsame( main,tmp));					
					
				}});
				
		}	
	}
	
	
	/*public void clearRow(FlexTable emailList,int rowCount) {
		emailList.setText(rowCount, 0, "");
		emailList.setText(rowCount, 1, "");
		emailList.setText(rowCount, 2, "");
		emailList.setText(rowCount, 3, "");
		emailList.setWidget(rowCount, 4, null);
		emailList.setWidget(rowCount, 5, null);
	}*/
}