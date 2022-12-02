package com.progetto.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import com.progetto.shared.concrete.RealDipartimento;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Dipartimento;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.interfaces.Corso;
import com.progetto.shared.interfaces.Esame;

public class DataManager{
	static String DBName = "dati.db";
	static String UserDB = "User";
	public static String StudentiDB = "Studenti";
	public static String AdminDB = "Admin";
	public static String DipartimentiDB = "Dipartimenti";
	public static String CorsiDB = "Corsi";
	public static String EsamiDB = "Esami";
	public static String salt = "somesaltforoutunsecurelysavedpassword";
	static private DB db;
	
	public synchronized static Utente getUser(String email) {
		openDB();
		Utente tmp = null;
		
		try
		{
			ConcurrentMap map = getMap(UserDB);
			
			tmp = (Utente) map.get(email);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		
		return tmp;
	}
	
	public synchronized static boolean removeUser(String email)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(UserDB);
			map.remove(email);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			
			return false;
		}
		
		closeDB();
		return true;
	}
	
	public synchronized static ArrayList<Utente> getUtenti()
	{
		ArrayList<Utente> tmpArray = null;
		openDB();
		ConcurrentMap map = getMap(UserDB);
		try
		{
			tmpArray = new ArrayList<Utente>(map.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		closeDB();
		return tmpArray;
	}
	
	public static String gethash(String textinput)
	{
		//Da aggiustare
		//return Crypt.crypt(textinput, salt);
		return textinput;
	}
	
	public static void openDB()
	{
		db = DBMaker
		        .fileDB(DBName)
		        .fileMmapEnable()
		        .make();
	}
	public static void closeDB()
	{
		db.close();
	}
	
	public static ConcurrentMap getMap(String mapName)
	{
		
		ConcurrentMap map = db
		        .hashMap(mapName)
		        .createOrOpen();
		return map;
	}
	
	public synchronized static void addoreditUser(Utente u)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(UserDB);
			map.put(u.getEmail(), u);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDB();
	}
	
	public synchronized static void addDipartimento(Dipartimento d)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(DipartimentiDB);
			map.put(d.getNome(), d);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
	}
	
	public synchronized static ArrayList<RealDipartimento> getDipartimenti()
	{
		ArrayList<RealDipartimento> tmpArray = null;
		openDB();
		ConcurrentMap map = getMap(DipartimentiDB);
		try
		{
			tmpArray = new ArrayList<RealDipartimento>(map.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		
		for(RealDipartimento d : tmpArray)
		{
			Utente tmpUser = getUser(d.getCoordinatore());
			
			if(tmpUser != null)
			{
				d.setCoordinatore(tmpUser.getName());
			}
			else
			{
				d.setCoordinatore("Account non trovato");
			}
		}
		return tmpArray;
	}
	
	public static synchronized boolean editUser(Utente u, String oldEmail)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(UserDB);
			map.remove(oldEmail);
			map.put(u.getEmail(), u);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDB();
		
		return true;
	}
	
	public static synchronized int addCorso(Corso c)
	{
		int idBound = 100000;
		int somethingiswrong = 0;
		int thresholdNewId = 1000;
		openDB();
		
		try
		{
			if(c.getId() != -1)
			{
				throw new DatabaseException("Errore imprevisto :\nSto cercando di aggiungere un'esame che ha gi� un id preimpostato");
			}
			else
			{
				ConcurrentMap map = getMap(CorsiDB);
				
				Random r = new Random();
				int newId = r.nextInt(idBound);
				
				while(map.containsKey(newId))
				{
					newId = r.nextInt(idBound);
					somethingiswrong++;
					
					if(somethingiswrong > thresholdNewId)
					{
						throw new DatabaseException("Errore imprevisto :\nNon trovo un id valido da assegnare al nuovo Esame");
					}
				}
				
				c.setId(newId);
				map.put(c.getId(), c);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			
			return -1;
		}
		
		closeDB();
		return c.getId();
	}
	
	
	public static synchronized void clearCorsi()
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(CorsiDB);
			map.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDB();
	}

	public static synchronized void clearEsami()
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(EsamiDB);
			map.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDB();
	}
	
	public static synchronized ArrayList<RealStudente> getStudenti(){
		ArrayList<RealStudente> tmpArray = null;
		
		openDB();
		try {
			ConcurrentMap map = getMap(UserDB);
			tmpArray = new ArrayList<RealStudente>(map.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return tmpArray;
	}
	
	public static synchronized ArrayList<RealCorso> getCorsi()
	{
		ArrayList<RealCorso> tmpArray = null;
		
		openDB();
		ConcurrentMap map = getMap(CorsiDB);
		try
		{
			tmpArray = new ArrayList<RealCorso>(map.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return tmpArray;
	}

	public static synchronized boolean removeCorso(int id) {
		openDB();
		
		try
		{
			ConcurrentMap map = getMap(CorsiDB);
			map.remove(id);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			
			return false;
		}
		
		closeDB();
		return true;
	}
	
	public static synchronized Corso getCorso(int id)
	{
		Corso c = null;
		openDB();
		
		try
		{
			ConcurrentMap map = getMap(CorsiDB);
			c =  (Corso) map.get(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return c;
	}
	
	public static synchronized int addEsame(Esame exam)
	{
		int idBound = 100000;
		int somethingiswrong = 0;
		int thresholdNewId = 1000;
		openDB();
		
		try
		{
			if(exam.getId() != -1)
			{
				
				throw new DatabaseException("Errore imprevisto :\nSto cercando di aggiungere un'esame che ha gi� un id preimpostato");
			}
			else
			{
				ConcurrentMap map = getMap(EsamiDB);
				
				Random r = new Random();
				int newId = r.nextInt(idBound);
				
				while(map.containsKey(newId))
				{
					newId = r.nextInt(idBound);
					somethingiswrong++;
					
					if(somethingiswrong > thresholdNewId)
					{
						throw new DatabaseException("Errore imprevisto :\nNon trovo un id valido da assegnare al nuovo Esame");
					}
				}
				
				exam.setId(newId);
				map.put(exam.getId(), exam);
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			
			return -1;
		}
		
		closeDB();
		return exam.getId();
	}
	
	public static synchronized boolean editEsame(Esame exam)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(EsamiDB);
			map.put(exam.getId(), exam);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			return false;
		}
		closeDB();
		
		return true;
	}
	
	public static synchronized boolean editCorso(Corso c)
	{
		openDB();
		try
		{
			ConcurrentMap map = getMap(CorsiDB);
			map.put(c.getId(), c);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			return false;
		}
		closeDB();
		
		return true;
	}
	
	public static synchronized Esame getEsame(int id)
	{
		Esame exam = null;
		openDB();
		
		try
		{
			ConcurrentMap map = getMap(EsamiDB);
			exam = (Esame) map.get(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return exam;
	}
	/*
	public static synchronized Studente getStudente(int id)
	{
		Studente exam = null;
		openDB();
		
		try
		{
			ConcurrentMap map = getMap(StudentDB);
			exam = (Studente) map.get(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return exam;
	}
	*/
	public static synchronized boolean removeEsame(int id)
	{
		openDB();
		
		try
		{
			ConcurrentMap map = getMap(EsamiDB);
			map.remove(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			closeDB();
			
			return false;
		}
		
		closeDB();
		return true;
	}
	
	public static synchronized ArrayList<Esame> getEsami()
	{
		ArrayList<Esame> tmpArray = null;
		
		openDB();
		try
		{
			ConcurrentMap map = getMap(EsamiDB);
			tmpArray = new ArrayList<Esame>(map.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		return tmpArray;
	}
	
	
}
