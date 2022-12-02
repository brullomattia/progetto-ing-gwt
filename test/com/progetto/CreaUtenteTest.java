package com.progetto;

import static org.junit.Assert.*;

import org.junit.Test;
import com.progetto.server.DataManager;
import com.progetto.server.GreetingServiceImpl;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealSegreteria;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;

public class CreaUtenteTest {

	@Test
	public void test() {
		GreetingServiceImpl gree = new GreetingServiceImpl();
		
		RealDocente docenteprova1 = new RealDocente("Nicola", "Lini", "docente@mail.it", "password");
		RealStudente studente1 = new RealStudente("Marco", "Verdi", "studente3@mail.it", "00002331", "password");
		RealSegreteria segreteria1 = (new RealSegreteria("Piero", "Gialli", "segreteria1@mail.it", "password"));
		
		
		int i=0;
		
		
		try{
			if(gree.addUtente(docenteprova1)) i++; 
			if(gree.addUtente(studente1)) i++; 
			if(gree.addUtente(segreteria1)) i++; 
			}
		catch(Exception e){e.getMessage();}		
		
		if (i !=3){
			fail("Not yet implemented");
		}
		
	}

}
