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
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.interfaces.Docente;

public class GestioneCorsiDocente extends Composite{
	private VerticalPanel principalPanel= new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private FlexTable ourCourses = new FlexTable();
	private Button deleteBtn;
	private Button editBtn;
	private Button addCoursesBtn;
	private HTML courses;
	private Prove main;
	private Docente d;
	private ArrayList<RealCorso> corsi = new ArrayList<RealCorso>();
	
	private Logger l = Logger.getLogger("Debug");
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	public GestioneCorsiDocente(Prove main, Docente d) {
		this.main = main;
		this.d = d;
		
		initWidget(this.principalPanel);
		
		
		
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
				
		//UserImg, userName and logout link 
		userImg = new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName = new HTML("<b>" + d.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);

		//ourCoursesList for docente 
		ourCourses.setWidget(0, 0, new HTML("<b>Nome</b>"));
		ourCourses.setWidget(0, 1, new HTML("<b>Dipartimento</b>"));
		ourCourses.setWidget(0, 2, new HTML("<b>Data inizio</b>"));
		ourCourses.setWidget(0, 3, new HTML("<b>Data Fine</b>"));
		ourCourses.setWidget(0, 4, null);
		ourCourses.setWidget(0, 5, null);
		ourCourses.setCellSpacing(10);
		ourCourses.setStyleName("padding-table");
		
		loadData();
		
		//Button add courses
		addCoursesBtn = new Button("Aggiungi corso");
		
		addCoursesBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new SchermataAggiungiCorso(d,main));
			}
		});
		
		//HTML courses
		courses= new HTML("<b>Elenco Corsi insegnati: </b>");
		
		// first HPanel
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		
		
		//principalPanel
		principalPanel.add(firstPanel);
		principalPanel.add(courses);
		principalPanel.add(ourCourses);
		principalPanel.add(addCoursesBtn);
		
		
	}
	
	
	
	public void addOurCourses(RealCorso c) {
		corsi.add(c);
		
		DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
		String StringInizio = fm.format(c.getDataInizio());
		String StringFine = fm.format(c.getDataFine());
		
		
		
		deleteBtn = new Button("Delete");
		editBtn = new Button("Edit");
		int currentRow = ourCourses.getRowCount();
		ourCourses.setText(currentRow, 0, c.getNome());
		ourCourses.setText(currentRow, 1, c.getDipartimento());
		ourCourses.setText(currentRow, 2, StringInizio);
		ourCourses.setText(currentRow, 3, StringFine);
		ourCourses.setWidget(currentRow, 4, deleteBtn);
		ourCourses.setWidget(currentRow, 5, editBtn);
		
		
		deleteBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int index = ourCourses.getCellForEvent(event).getRowIndex();
				
				removeCorso(corsi.get(index - 1));
				corsi.remove(index - 1);
				clearRow(index);
			}
		});	
		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
					
					main.setPage(new SchermataModificaCorso(d,main,c));
				
			}
		});	
	}
	
	public void clearRow(int rowCount) {
		ourCourses.setText(rowCount, 0, "");
		ourCourses.setWidget(rowCount, 1, null);
	}

	/*
	 * il metodo removeCorso elimina il corso dal Db
	 */
	public void removeCorso(RealCorso c)
	{
		greetingService.removeCorso(c, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(Boolean result) {
				if(result == false)
				{
					l.log(Level.WARNING, "Errore imprevisto in remove Corso");
				}
				else{
					main.setPage( new GestioneCorsiDocente( main,  d));
				}
			}
		});
	}

	/*
	 * il metodo loadData() carica i corsi insegnati dal docente.
	 * tramite il metodo addOurCourses()  vengono uno ad uno aggiunti alla tabella ourCourses
	 */
	public void loadData()
	{	
		ArrayList<Integer> tmpcorsi = d.getCorsiInsegnati();
		l.log(Level.INFO, "il Docente insegna i corsi con id= "+ tmpcorsi );
		
		
		greetingService.getCorsi(tmpcorsi, new AsyncCallback<ArrayList<RealCorso>>() {
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "Errore");
				l.log(Level.WARNING, caught.getMessage());
			}
			
			public void onSuccess(ArrayList<RealCorso> result) {
				if(result != null)
				{
					for(RealCorso c : result)
					{
						addOurCourses(c);
					}
				}
				else{
					l.log(Level.INFO, "getCorsi non ha ritornato alcun corso" );
				}
			}
		});
	}
}

