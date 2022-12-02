package com.progetto.client.gui;

import java.sql.Date;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.interfaces.Docente;


public class GestioneEsamiDocente extends Composite{
	private VerticalPanel principalPanel= new VerticalPanel();
	private HorizontalPanel firstPanel= new HorizontalPanel();
	private Image uniImg;
	private Image userImg;
	private HTML userName;
	private Anchor logout;
	private Grid addExams = new Grid(1,2);
	private Grid ourExams = new Grid(1,4);
	private Grid comExams = new Grid(1,3);
	private Grid votExams = new Grid(1,3);
	
	private Button addExamBtn, deleteExamBtn, editVoti,editExamBtn;
	private HTML examList, corsList, examComList, examVotList;
	private Prove main;
	private Docente d;
	private Logger l = Logger.getLogger("Debug");
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	
	public GestioneEsamiDocente(Prove main, Docente d) {
		this.main = main;
		this.d = d;
		
		// imposto la data attuale
		long millis = System.currentTimeMillis();
		Date date = new Date(millis); 
		
		initWidget(this.principalPanel);
		
		//UNIBO image
		uniImg = new Image("images/unibo.png");
				
		//UserImg, userName and logout link 
		userImg= new Image("images/userIcon.png");
		userImg.setSize("20px", "20px");
		userName=new HTML("<b>" + d.getName() + "</b>");
		logout = new Anchor("Logout");
		logout.addClickHandler(main.logoutHandler);
		
		//HTML userCourses: gliglia con i corsi che non hanno un esame
		corsList= new HTML("<b>Lista Corsi che non hanno un esame </b>");
		addExams.setWidget(0, 0, new HTML("<b>Nome Corso<b>"));
		addExams.setWidget(0, 1, null);
		addExams.setCellSpacing(10);
		
		//Esami da effettuare
		examList = new HTML("<b>Lista Esami da svolgere </b>");
		ourExams.setWidget(0, 0, new HTML("<b>Nome Esame<b>"));
		ourExams.setWidget(0, 1, new HTML("<b>Data Svolgimento<b>"));
		ourExams.setWidget(0, 2, null);
		ourExams.setWidget(0, 3, null);
		ourExams.setCellSpacing(10);
		
		//Esami effettuati ma con esito non pubblicato
		examComList = new HTML("<b>Lista Esami svolti non pubblicati</b>");
		comExams.setWidget(0, 0, new HTML("<b>Nome Esame<b>"));
		comExams.setWidget(0, 1, new HTML("<b>Data Svolgimento<b>"));
		comExams.setWidget(0, 2, null);
		addExams.setCellSpacing(10);
		
		//Esami effettuati con esito pubblicato
		examVotList = new HTML("<b>Lista Esami svolti pubblicati</b>");
		votExams.setWidget(0, 0, new HTML("<b>Nome Esame<b>"));
		votExams.setWidget(0, 1, null);
		votExams.setWidget(0, 2, null);
		addExams.setCellSpacing(10);
		
		//gestione addExam vogliamo vedere i corsi insegnati dal docente che non hanno l'esame
		greetingService.getCorsi(d.getCorsiInsegnati(), new AsyncCallback<ArrayList<RealCorso>>() {

			@Override
			public void onFailure(Throwable caught) {
				l.log(Level.WARNING, "ErroreGetCorsi");
				l.log(Level.WARNING, caught.getMessage());
				
			}

			@Override
			public void onSuccess(ArrayList<RealCorso> corsiInsegnati) {
				l.log(Level.INFO, "l'array di corsi insegnati ha dimensione: "+corsiInsegnati.size());
				
				//l' arraylist tuttiEsami contiene gli id degli esami dei corsi insegnati
				ArrayList<Integer> tuttiEsami = new ArrayList<Integer>();
				
				for(RealCorso c : corsiInsegnati) {
					
					// se un corso non ha ancora un esame viene aggiunto alla griglia addExams
					if(c.getEsame()==-1) {
						l.log(Level.INFO, "I Corsi senza esame sono: "+c.getNome()+"   id corso:"+c.getId()+"  idEsame:"+c.getEsame());
						addToExams(addExams, c, d);
					}else {
						l.log(Level.INFO, "I corsi con gli esami sono: "+c.getNome()+"   id corso:"+c.getId()+"  idEsame:"+c.getEsame());
						tuttiEsami.add(c.getEsame());
					}
				}
				
				//gestione esami degli esami (sia da svolgere che gia' svolti)
				greetingService.getEsami(tuttiEsami, new AsyncCallback<ArrayList<RealEsame>>() {
					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore in GetEsamiConId");
						l.log(Level.WARNING, caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<RealEsame> listaEsami) {
						
						l.log(Level.INFO, "l'array di esami totali  ha dimensione: "+tuttiEsami.size());
						
						for(RealCorso c : corsiInsegnati) {
							
							if(c.getEsame()!=-1) {
								for(RealEsame e : listaEsami) {
									
									//esami da svolgere ( con data posteriore alla data odierna (che si trova nella variabile chiamata "date")
									if(e.getId()==c.getEsame() && e.getData().after(date)){
										ourToExams(ourExams, c.getNome(),e);
									
										//gli esami svolti...
									}else if(e.getId()==c.getEsame() && e.getData().before(date)){
										
										//... con esito pubblicato, sono aggiunti alla griglia votToExams
										if(e.isPubblicato()==true) {
											votToExams(votExams,c.getNome());
										}
										
										//... con esito NON pubblicato, sono aggiunti alla griglia comToExams
										else{
											comToExams(comExams, c,e, d);
										}
										
									}
								}
							}
						}
						
					}
				});
			}	
			});
		

		
		// first HPanel 
		this.firstPanel.setSpacing(10);
		firstPanel.add(uniImg);
		firstPanel.add(userImg);
		firstPanel.add(userName);
		firstPanel.add(logout);
		
		//principalPanel
		principalPanel.add(firstPanel);
		//
		principalPanel.add(corsList);
		principalPanel.add(addExams);
		//
		principalPanel.add(examList);
		principalPanel.add(ourExams);
		//
		principalPanel.add(examVotList);
		principalPanel.add(votExams);
		//
		principalPanel.add(examComList);
		principalPanel.add(comExams);
	}

	
	
	/*
	 * metodi per aggiungere elementi alle 4 griglie:
	 */
	
	public void addToExams(Grid gridName, RealCorso c, Docente d) {
		int rowCount = gridName.getRowCount();
		addExamBtn= new Button("Aggiungi Esame");
		gridName.insertRow(rowCount);
		gridName.setText(rowCount, 0, c.getNome());
		gridName.setWidget(rowCount, 1, addExamBtn);
		addExamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new SchermataAggiungiEsame(main,c, d));
			}
		});
	}
	
	public void ourToExams(Grid ourExams, String nome,RealEsame e) {
		
		int rowCount = ourExams.getRowCount();
		deleteExamBtn = new Button("Elimina esame");
		editExamBtn = new Button("Modifica esame");
		DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
		String dataEsame = fm.format(e.getData());
		
		
		ourExams.insertRow(rowCount);
		ourExams.setText(rowCount, 0, nome);
		ourExams.setText(rowCount, 1, dataEsame);
		ourExams.setWidget(rowCount, 2, deleteExamBtn);
		ourExams.setWidget(rowCount, 3, editExamBtn);
		
		deleteExamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				greetingService.removeEsame(e, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "Errore nella rimozione dell' esame");
						l.log(Level.WARNING, caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						
						if(!result){
							l.log(Level.WARNING, "Errore nella rimozione dell' esame");
						}
						else{
							main.setPage(new GestioneEsamiDocente(main,d));
						}
					
						
					}
				});
			}
		});
		editExamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				main.setPage(new SchermataModificaEsame(d,main,e));
			}
		});
		
		
	}
	
	public void comToExams(Grid ourExams, RealCorso c, RealEsame e, Docente d) {
		int rowCount = ourExams.getRowCount();
		editVoti = new Button("Aggiungi voti");
		DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
		String dataEsame = fm.format(e.getData());
		
		ourExams.insertRow(rowCount);
		ourExams.setText(rowCount, 0, c.getNome());
		ourExams.setText(rowCount, 1, dataEsame);
		ourExams.setWidget(rowCount, 2, editVoti);
		editVoti.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				main.setPage(new SchermataInserisciVotiDocente(main, e,c,d));
			}
		});
		
	}
	
	public void votToExams(Grid votExams, String nome) {
		
		int rowCount = votExams.getRowCount();
		votExams.insertRow(rowCount);
		votExams.setText(rowCount, 0, nome);		
	}
	
	
	public void clearRow(Grid ourCourses,int rowCount) {
		ourCourses.setText(rowCount, 0, "");
		ourCourses.setWidget(rowCount, 1, null);
		ourCourses.setWidget(rowCount, 2, null);	
	}
}

