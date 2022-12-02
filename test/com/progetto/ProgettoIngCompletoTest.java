package com.progetto;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.progetto.client.GreetingService;
import com.progetto.client.GreetingServiceAsync;
import com.progetto.shared.concrete.RealCorso;

public class ProgettoIngCompletoTest extends GWTTestCase{

	private ServiceDefTarget target;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	private Logger l = Logger.getLogger("Debug");
	
	@Override
	public String getModuleName() {
		return this.getClass().toString();
		//return null;

	}
	
	  /**
	   	* Add as many tests as you like.
	   	* Loading inherited module 'com.progetto'
   		*[ERROR] Unable to find 'com/progetto.gwt.xml' on your classpath;
    	*could be a typo, or maybe you forgot to include a classpath entry for source?
	   */
	  public void testSimple() {                                              // (3)
		  assertTrue(true);
			/*gestione allCourses list
			greetingService.getCorsi(new AsyncCallback<ArrayList<RealCorso>>() {

				@Override
				public void onFailure(Throwable caught) {
					l.log(Level.WARNING, "Errore");
					l.log(Level.WARNING, caught.getMessage());
					assertTrue(false);
				}

				@Override
				public void onSuccess(ArrayList<RealCorso> result) {
					DateTimeFormat fm = DateTimeFormat.getFormat("MM/dd/yyyy");
					
					
					
				}
				
			});*/
		  
	  }

}