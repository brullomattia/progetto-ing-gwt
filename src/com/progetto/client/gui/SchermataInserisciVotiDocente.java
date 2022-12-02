package com.progetto.client.gui;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.client.Prove;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.interfaces.Corso;
import com.progetto.shared.interfaces.Docente;
import com.progetto.shared.interfaces.Esame;

public class SchermataInserisciVotiDocente extends Composite {
	
	private Prove main;
	int idCorso, idEsame;
	private RealDocente user;
	private RealCorso c;
	private RealEsame e;
	private VerticalPanel verticalPanel = new VerticalPanel();
	private TextArea testoVoti = new TextArea();
	private HTML insertVoti;
	private HTML nota;
	private Button saveVotiBtn;
	
	
	private Logger l = Logger.getLogger("Debug");
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	/*
	 * in questa pagina il docente puo modificare il campo "message" di un esame, inserendo
	 * un testo scritto in cui sono scritti tutti i voti degli studenti dell' esame corrispondente.
	 * tale testo sarà poi visualizzato dalla segreteria
	 */
	public SchermataInserisciVotiDocente(Prove mainn, Esame ee, Corso cc, Docente d) {
		this.user = (RealDocente) d;
		this.e =  (RealEsame) ee;
		this.idEsame = e.getId();
		this.main=mainn;
		this.c = (RealCorso) cc;
		initWidget(verticalPanel);
		
		testoVoti.setCharacterWidth(75);
	    testoVoti.setVisibleLines(30);
	    testoVoti.setText(e.getMessage());
	    
	    //Button
		saveVotiBtn = new Button("Salva & Torna indietro"); 
		
		insertVoti = new HTML("<b>Inserisci i voti agli studenti per l'esame di:  "	+c.getNome()+"</b>");
		nota = new HTML( "<p> Nota: indicare la mail di ogni studente di cui si desidera inserire il voto </p>");

		
		l.log(Level.INFO, "L'id dell'essame =>¨: "+ c.getEsame());
		

		verticalPanel.setSpacing(10);
		verticalPanel.add(insertVoti);
		verticalPanel.add(nota);
		verticalPanel.add(testoVoti);
		verticalPanel.add(saveVotiBtn);	
		saveVotiBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				e.setMessage(testoVoti.getText());

				greetingService.editEsame(e, new AsyncCallback<Boolean>(){

					@Override
					public void onFailure(Throwable caught) {
						l.log(Level.WARNING, "problemi con editEsame ");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result){
							l.log(Level.WARNING, "problemi con editEsame ");
						}
						else{
							main.setPage( new GestioneEsamiDocente(main,user)) ;
						}
					}
				});
			}
			
		});
	}
	


}
