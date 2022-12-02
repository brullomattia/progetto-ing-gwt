package com.progetto;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;
import com.progetto.server.DataManager;
import com.progetto.server.GreetingServiceImpl;
import com.progetto.shared.concrete.RealCorso;

public class CreaCorsoTest {

	@Test
	public void test() {
		
		RealCorso corsoprova = new RealCorso("Basi di Dati", new Date(1990, 12, 24), new Date(2022, 12, 11),
				"email", "sfg", "Corso sui moderni sistemi di database",
				"dipartimanr", -1);
		int i=0;
		GreetingServiceImpl gree = new GreetingServiceImpl();
		try{i = gree.addCorsoReturnId(corsoprova);} catch(Exception e){ e.getLocalizedMessage();};
		if (i ==-1){
			fail("Not yet implemented");
		}
		
		
		
	}

}
