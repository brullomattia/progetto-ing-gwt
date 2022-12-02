package com.progetto;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.progetto.server.DataManager;
import com.progetto.server.GreetingServiceImpl;
import com.progetto.shared.concrete.RealEsame;

public class CreaEsameTest {

	@Test
	public void test() {
		
		RealEsame esameBasi = new RealEsame("Aula1", new Date(1990-12-24),45,-1, 10,"sette e mezza");
		
		int i=0;
		GreetingServiceImpl gree = new GreetingServiceImpl();
		try{gree.addEsame(esameBasi);} catch(Exception e){e.getMessage();}		if (i ==-1){
			fail("Not yet implemented");
		}
		
		
		
	}

}
