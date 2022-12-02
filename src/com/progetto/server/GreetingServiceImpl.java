package com.progetto.server;


import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.progetto.client.GreetingService;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealAmministratore;
import com.progetto.shared.concrete.RealDipartimento;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealSegreteria;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.interfaces.Amministratore;
import com.progetto.shared.interfaces.Docente;
import com.progetto.shared.interfaces.Esame;
import com.progetto.shared.interfaces.Segreteria;
import com.progetto.shared.interfaces.Studente;
import com.progetto.shared.concrete.RealCorso;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.progetto.shared.interfaces.Corso;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	
	private Logger l = Logger.getLogger("Debug");
	


	@Override
	public Utente authenticate(String email, String password)
			throws IllegalArgumentException {
		//caricaDatiDebug();
		
		password = DataManager.gethash(password);
		Utente u = DataManager.getUser(email);
		
		if(u == null)
		{
			return null;
		}
		if(u.getPassword().equals(password))
		{
			checkUtente(u);
			Utente nu = DataManager.getUser(u.getEmail());
			return nu;
		}
		
		return null;
	}
	
	/*
	 * Metodo per caricare dati di prova su DB 
	 * 
	 */
	@Override
	public boolean caricaDatiDebug()  throws Exception
	{	
		DataManager.removeUser("prova@mail.it");
		int durata = 90;
		String emailProva = "admin";
		String sedeProva = "Via Zamboni 45";
		RealDocente docenteprova = new RealDocente("Giuseppe", "Pagliari", "docente1@mail.it", "password");
		RealDocente docenteprova1 = new RealDocente("Nicola", "Lini", "docente@mail.it", "password");
		DataManager.addoreditUser(docenteprova1);
		RealDipartimento dipinformaticaprova = new RealDipartimento("Informatica", null, emailProva, sedeProva);
		
		//crea corsi prova ( hanno id -1) 
		
		RealCorso corsoprova = new RealCorso("Basi di Dati", new Date(1990, 12, 24), new Date(2022, 12, 11),
				docenteprova.getEmail(), "", "Corso sui moderni sistemi di database",
				dipinformaticaprova.getNome(), -1);
		
		RealCorso corsoprova4 = new RealCorso("Tec Web", new Date(1990, 12, 24), new Date(2022, 12, 11),
				docenteprova.getEmail(), "", "Corso sui moderni sistemi di database",
				dipinformaticaprova.getNome(), -1);
		RealCorso corsoprova5 = new RealCorso("Algoritmi", new Date(1990, 12, 24), new Date(2022, 12, 11),
				docenteprova.getEmail(), "", "Corso sui moderni sistemi di database",
				dipinformaticaprova.getNome(), -1);
		RealEsame esameBasi = new RealEsame("Aula1", new Date(1990-12-24),durata,
			corsoprova.getId(), 10,"sette e mezza");
		DataManager.addoreditUser(new RealAmministratore("Enrico", "Rossi", emailProva, "admin"));	
		RealStudente studente1 = new RealStudente("Marco", "Verdi", "studente3@mail.it", "00002331", "password");
		RealStudente studente2 = new RealStudente("Pino", "Rossi", "studente2@mail.it", "00002431", "password");
		DataManager.addoreditUser(studente1);
		DataManager.addoreditUser(studente2);	
		DataManager.addoreditUser(new RealSegreteria("Piero", "Gialli", "segreteria1@mail.it", "password"));
		DataManager.addoreditUser(new RealSegreteria("Piero", "Verdi", "segreteria2@mail.it", "password"));
		DataManager.addoreditUser(new RealDocente("Cristina", "Camurri", "docente2@mail.it", "password"));
		DataManager.addoreditUser(new RealDocente("Roberto", "De Niro", "docente3@mail.it", "password"));
		
		
		//metodi per eliminare tutti i corsi e gli esami nel DB
		DataManager.clearCorsi();
		DataManager.clearEsami();
	
		
		
		DataManager.addoreditUser(docenteprova);
		//Aggiunta dipartimenti per le prove
		DataManager.addDipartimento(dipinformaticaprova);
		DataManager.addDipartimento(new RealDipartimento("Fisica", null, emailProva, sedeProva));
		DataManager.addDipartimento(new RealDipartimento("Chimica", null, emailProva, sedeProva));
		
		//aggiungi esami (hanno id diverso da -1, ma idCorso -1) 
		try{addEsame(esameBasi);} catch(Exception e){e.getMessage();}
		
		corsoprova.setEsame(esameBasi.getId());
		DataManager.addCorso(corsoprova);DataManager.addCorso(corsoprova4);DataManager.addCorso(corsoprova5);
		
		//aggiungo corso al docente
		docenteprova.addCorsoInsegnato(corsoprova.getId());
		docenteprova.addCorsoInsegnato(corsoprova4.getId());
		docenteprova.addCorsoInsegnato(corsoprova5.getId());
		
		DataManager.addoreditUser(docenteprova);
		esameBasi.setCorso(corsoprova.getId()); 		
		
		try{editEsame(esameBasi);} catch(Exception e){e.getMessage();}
		
		try{iscrizioneCorso(studente1,corsoprova.getId());} catch(Exception e){e.getMessage();}
		
		try{boolean sign = iscrizioneEsame(studente1,esameBasi.getId());
		l.log(Level.INFO, "iscritto all' esame="+ sign);
		} catch(Exception e){e.getMessage();}
		return true;
	}

	/*
	 * metodo che serve per verificare che nei dati di un utente 
	 * non vi sia nessun riferimento ad un esame o corso che sono stati eliminati	
	 */
	public void checkUtente(Utente u)  {
		Utente tmpu = DataManager.getUser(u.getEmail());
		try{
		if(tmpu!= null){
				
				if(tmpu instanceof Docente){
					RealDocente tmp = (RealDocente) tmpu ;
					
					for(int i: tmp.getCorsiInsegnati()){
						Corso tmpc = DataManager.getCorso(i);
						if (tmpc==null){
							tmp.removeCorsoInsegnato(i);
						}
					}
					for(int i: tmp.getCorsiCoInsegnati()){
						Corso tmpc = DataManager.getCorso(i);
						if (tmpc==null){
							tmp.removeCorsoCoInsegnato(i);				
						}
					}
					DataManager.addoreditUser(tmp);
				}
				
				
				if(tmpu instanceof Studente){
					RealStudente tmp = (RealStudente) tmpu ;			
					for(int i: tmp.getIscrizioniCorsi()){
						Corso tmpc = DataManager.getCorso(i);
						if (tmpc==null){
							tmp.removeCorso(i);
						}
					}
					for(int i: tmp.getRegistrazioniEsami()){
						Esame tmpe = DataManager.getEsame(i);
						if (tmpe==null){
							tmp.removeRegistrazioneEsame(i);					
						}
					}
					DataManager.addoreditUser(tmp);
					
				}
		}
		}
		catch(Exception e){	e.getMessage();}
		
	}
	
	
	@Override
	public Boolean addUtente(Utente u) throws Exception {
		try {
			String hashedPassword = DataManager.gethash(u.getPassword());
			u.setPassword(hashedPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DataManager.addoreditUser(u);
		return true;
	}
	
	//metodo che serve per verificare che la email in input appartenga ad un docente
	public boolean checkDocente(String email)
			throws Exception {
		ArrayList<Utente> allUser = DataManager.getUtenti();
		boolean result=false;
		
		for(Utente u : allUser){	
			if(u instanceof Docente && u.getEmail().equals(email))
			{
				result=true;
			}
			
		}
		return result;
	}
	

	
	@Override
	public ArrayList<RealDipartimento> getDipartimenti() throws Exception {
		return DataManager.getDipartimenti();
	}

	@Override
	public ArrayList<Utente> getUtenti() throws Exception {
		return DataManager.getUtenti();
	}
	
	public ArrayList<RealEsame> getEsami() throws Exception {
		ArrayList<Esame> abs = new ArrayList<Esame>();
		abs=DataManager.getEsami();
		ArrayList<RealEsame> result = new ArrayList<RealEsame>();
		for(Esame e:abs){
			RealEsame re = (RealEsame) e;
			result.add(re);
		}
		
		return result;
	}
	@Override
	public ArrayList<Utente> getUtenti(ArrayList<AccountType> types)
			throws Exception {
		ArrayList<Utente> allUser = DataManager.getUtenti();
		ArrayList<Utente> filteredArray = new ArrayList<Utente>();
		
		for(Utente u : allUser)
		{
			if(u instanceof Studente && types.contains(AccountType.Studente))
			{
				filteredArray.add(u);
			}
			else if(u instanceof Docente && types.contains(AccountType.Docente))
			{
				filteredArray.add(u);
			}
			else if(u instanceof Segreteria && types.contains(AccountType.Segreteria))
			{
				filteredArray.add(u);
			}
			else if(u instanceof Amministratore && types.contains(AccountType.Amministratore))
			{
				filteredArray.add(u);
			}
		}
		return filteredArray;
	}
	

	public Utente getUtente(String email)
			throws Exception {
		ArrayList<Utente> allUser = DataManager.getUtenti();
		Utente result = new Utente();
		
		for(Utente u : allUser)
		{
			if(u.getEmail().equals(email))
			{result =u;
				
		}}
		return result;
	}
	
	
	@Override
	public Boolean removeUtente(Utente u) throws Exception {
		return DataManager.removeUser(u.getEmail());
	}

	@Override
	public Boolean editUtente(Utente u, String oldEmail) throws Exception {
		return DataManager.editUser(u, oldEmail);
	}
	
	@Override
	public ArrayList<RealCorso> getCorsi(ArrayList<Integer> idCorsi)
			throws Exception {
		ArrayList<RealCorso> allcorsi = DataManager.getCorsi();
		ArrayList<RealCorso> filteredcorsi = new ArrayList<RealCorso>();
		
		for(RealCorso c : allcorsi)
		{
			if(idCorsi.contains(c.getId()))
			{
				filteredcorsi.add(c);
			}
		}
		
		return filteredcorsi;
	}
	
	@Override
	public ArrayList<RealCorso> getCorsi() throws Exception {
		ArrayList<RealCorso> allcorsi = DataManager.getCorsi();
		return allcorsi;
	}

	
	@Override
	public boolean addCorso(RealCorso c) throws Exception {
		if(c.getId() == -1 && c != null)
		{
			int newCorsoId = DataManager.addCorso(c);
			
			if(newCorsoId != -1)
			{
				return true;
			}
		}
		
		return false;
	}

	/*
	 * Metodo che serve per aggiungere un corso al DB e per Assegnarlo ad un Docente
	 */
	
	@Override
	public int addCorsoNew(RealCorso c,RealDocente d) throws Exception {
		
		Utente tmpu = DataManager.getUser(d.getEmail());
		RealDocente tmpd;		
		
		if(tmpu != null && tmpu instanceof Docente){
			tmpd = (RealDocente) tmpu;
				
					if(c.getId() == -1 && c != null){
					
					int newCorsoId = DataManager.addCorso(c);
				
					if(newCorsoId != -1){
						tmpd.addCorsoInsegnato(newCorsoId);
						c.setDocente(d.getEmail());
						DataManager.addoreditUser(tmpd);
						DataManager.editCorso(c);
						return newCorsoId;
					}
			}
		}
				
		return -1;
	}
	
	@Override
	public boolean setCodocente(int idCorso,String email) throws Exception {
		
		String oldCoDocente = "";
		Utente tmpu = DataManager.getUser(email);
		RealDocente tmpd;		
		
		Corso tmpc = DataManager.getCorso(idCorso);
		RealCorso tmprc;
		
		if(tmpu != null && tmpu instanceof Docente){
			tmpd = (RealDocente) tmpu;
				
					if(tmpc != null && tmpc instanceof Corso){
					
					tmprc  = (RealCorso) tmpc;
					oldCoDocente = tmprc.getCoDocente();
				
					if(idCorso != -1){
						if(!oldCoDocente.equals("")){
							removeCodocenza(idCorso,oldCoDocente);};
						
					}
						
						tmpd.addCorsoCoInsegnato(idCorso);
						tmprc.setCoDocente(email);
						
						DataManager.addoreditUser(tmpd);
						DataManager.editCorso(tmprc);
						return true;
						
			}
		}		
		return false;
	}	
	
	
	@Override
	public boolean removeCodocenza(int idCorsoo,String emaill) throws Exception {
		
		Utente tmpuu = DataManager.getUser(emaill);
		RealDocente tmpdd;		
		Corso tmpcc = DataManager.getCorso(idCorsoo);
		RealCorso tmprcc;
		if(tmpuu != null && tmpuu instanceof Docente){
			tmpdd = (RealDocente) tmpuu;			
					if(tmpcc != null && tmpcc instanceof Corso){	
						tmprcc = (RealCorso)tmpcc;
						if(idCorsoo != -1){
							tmprcc.setCoDocente("");
							tmpdd.removeCorsoCoInsegnato(idCorsoo);  
							DataManager.addoreditUser(tmpdd);
							DataManager.editCorso(tmprcc);
							
							return true;
						}
			}
		}
		return false;
	}
	
	
	@Override
	public int addCorsoConEsame(RealCorso c,RealDocente d,RealEsame e) throws Exception {
		
		Utente tmpu = DataManager.getUser(d.getEmail());
		
		
		if(tmpu != null && tmpu instanceof Docente){
			d = (RealDocente) tmpu;
				
					if(c.getId() == -1 && c != null){
					
						int newCorsoId = DataManager.addCorso(c);
						
						if(e.getId() == -1 && e != null){
							int newExamId = DataManager.addEsame(e);
					
							if((newCorsoId != -1)&&(newExamId != -1)){
								
								d.addCorsoInsegnato(newCorsoId);
								c.setDocente(d.getEmail());
								c.setEsame(newExamId);
								e.setCorso(newCorsoId);
								
								DataManager.addoreditUser(d);
								DataManager.editCorso(c);
								DataManager.editEsame(e);
								return newCorsoId;
					}
			}
		}
		}

		return -1;
	}
	
	@Override
	public int addEsameACorso(RealCorso c,RealEsame e) throws Exception {
		
		Corso tmp = DataManager.getCorso(c.getId());
		int idEsame=e.getId();
		
		if(tmp != null && idEsame ==-1){
			
			e.setCorso(c.getId());
			idEsame = DataManager.addEsame(e);
			c.setEsame(idEsame);
			DataManager.editCorso(c);	
		}
			
			return idEsame;
	
	}
	
	
	public int addCorsoReturnId(RealCorso c) throws Exception {
		if(c.getId() == -1 && c != null)
		{
			int newCorsoId = DataManager.addCorso(c);
			
			if(newCorsoId != -1)
			{
				return newCorsoId;
			}
		}
		
		return -1;
	}
	
	@Override
	public boolean addEsame(RealEsame e) throws Exception {
		if(e.getId() == -1 && e != null)
		{
			int newExamId = DataManager.addEsame(e);
			
			if(newExamId != -1)
			{
				return true;
			}
		}
		
		return false;
	}

	
	@Override
	public int addEsameReturnId(RealEsame e) throws Exception {
		if(e.getId() == -1 && e != null)
		{
			int newExamId = DataManager.addEsame(e);
			
			if(newExamId != -1)
			{
				return newExamId;
			}
		}
		
		return -1;
	}
	@Override
	public boolean removeCorso(RealCorso c) throws Exception {
		
		if(c.getEsame()!=-1){
			return DataManager.removeCorso(c.getId()) && DataManager.removeEsame(c.getEsame()) ;
		}
		else{
			return DataManager.removeCorso(c.getId());
		}
		
		
	}
	@Override
	public boolean removeEsame(RealEsame e) throws Exception {
		if(e.getId()!=-1){
			
			Corso tmp = DataManager.getCorso(e.getCorso());
			if(tmp!= null && tmp.getEsame()==e.getId()){
				RealCorso tmpc = (RealCorso) tmp;
				tmpc.setEsame(-1);
				DataManager.editCorso(tmpc);
			}
			return DataManager.removeEsame(e.getId()) ;
		}
		else{
			return true;
		}
	}
	
	@Override
	public boolean iscrizioneCorso(RealStudente s, int idCorso)
			throws Exception {
		Utente tmpu = DataManager.getUser(s.getEmail());
		
		if(tmpu != null && tmpu instanceof Studente)
		{
			RealStudente tmps = (RealStudente) tmpu;
			
			if(DataManager.getCorso(idCorso) != null && !tmps.getIscrizioniCorsi().contains(idCorso))
			{
				tmps.addCorso(idCorso);
				DataManager.addoreditUser(tmps);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean disiscrizioneCorso(RealStudente s, int idCorso)
			throws Exception {
		Utente tmpu = DataManager.getUser(s.getEmail());
		Corso tmpc = DataManager.getCorso(idCorso);
		if(tmpu != null && tmpu instanceof Studente && tmpc !=null)
		{
			RealStudente tmps = (RealStudente) tmpu;
			RealCorso tmpcr = (RealCorso) tmpc;
			int idEsame = tmpcr.getEsame();
			if(tmps.getIscrizioniCorsi().contains(idCorso))
			{	
				tmps.removeCorso(idCorso);
				
				if(idEsame != -1 && tmps.getRegistrazioniEsami().contains(idEsame)){
					tmps.removeRegistrazioneEsame(idEsame);
				}
				
				DataManager.addoreditUser(tmps);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean iscrizioneEsame(RealStudente s, int idEsame)
			throws Exception {
		Utente tmpu = DataManager.getUser(s.getEmail());
		
		if(tmpu != null && tmpu instanceof Studente)
		{
			RealStudente tmps = (RealStudente) tmpu;
			Esame tmpe = DataManager.getEsame(idEsame);
			
			if(tmpe != null && DataManager.getCorso(tmpe.getCorso()) != null
					&& tmps.getIscrizioniCorsi().contains(tmpe.getCorso())
					&& !tmps.getRegistrazioniEsami().contains(idEsame))
			{
				tmps.addRegistrazioneEsame(idEsame);
				DataManager.addoreditUser(tmps);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean inserimentoVoto(int idEsame,int voto,String email)
			throws Exception {
		Utente tmpu = DataManager.getUser(email);
		Esame tmpe = DataManager.getEsame(idEsame);
		
		if(tmpu != null && tmpu instanceof Studente && tmpe != null)
		{
			RealStudente tmps = (RealStudente) tmpu;
			
				tmps.insertVoto(idEsame, voto);
				DataManager.addoreditUser(tmps);
				return true;
			
		}
		return false;
	}
	
	@Override
	public boolean disiscrizioneEsame(RealStudente s, int idEsame)
			throws Exception {
		Utente tmpu = DataManager.getUser(s.getEmail());
		
		if(tmpu != null && tmpu instanceof Studente)
		{
			RealStudente tmps = (RealStudente) tmpu;
			
			if(tmps.getRegistrazioniEsami().contains(idEsame))
			{
				int index = tmps.getRegistrazioniEsami().indexOf(idEsame);
				tmps.getRegistrazioniEsami().remove(index);
				
				DataManager.addoreditUser(tmps);
				return true;
				
				
				
				
			}
		}
		return false;
	}


	@Override
	public ArrayList<RealEsame> getEsami(RealStudente s) throws Exception {
		ArrayList<Esame> allEsami = DataManager.getEsami();
		ArrayList<RealEsame> filteredEsami = new ArrayList<RealEsame>();
		
		for(Esame e : allEsami){
			
			if(s.getRegistrazioniEsami().contains(e.getId()))
			{
				filteredEsami.add((RealEsame) e);
			}
		}
		
		return filteredEsami;
	}
	
	public ArrayList<RealEsame> getEsami(ArrayList<Integer> idEsami) throws Exception {
		ArrayList<Esame> allEsami = DataManager.getEsami();
		ArrayList<RealEsame> filteredEsami = new ArrayList<RealEsame>();
		
		for(Esame e : allEsami)
		{
			if(idEsami.contains(e.getId()))
			{
				filteredEsami.add((RealEsame) e);
			}
		}
		
		return filteredEsami;
	}
	
	public RealEsame getEsame(int idEsame) throws Exception {
		ArrayList<Esame> allEsami = DataManager.getEsami();
		RealEsame theExam = null;
		
		for(Esame e : allEsami)
		{	RealEsame re = (RealEsame)e;
			if(idEsame==re.getId())
			{
				theExam= re;
			}
		}
		
		return theExam;
	}
	
	
	@Override
	public boolean addCorso(RealCorso c, RealEsame e) throws Exception {
		if(e.getId() == -1 && c != null && e != null)
		{
			int newExamId = DataManager.addEsame(e);
			
			if(newExamId != -1)
			{
				c.setEsame(newExamId);
				
				int newCorsoId = DataManager.addCorso(c);
				if(newCorsoId != -1)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	

	@Override
	public boolean editCorso(RealCorso c) throws Exception {
		if(c.getId() != -1 && c != null)
		{
			RealCorso tmpcorso = (RealCorso) DataManager.getCorso(c.getId());
			if(tmpcorso == null)
			{
				
				return false;
			}
			if(DataManager.editCorso(c))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean editCorso(RealCorso c, int idEsame) throws Exception {
		if(c != null)
		{
			RealCorso Corso = (RealCorso) DataManager.getCorso(c.getId());
			if(Corso == null)
			{
				return false;
			}else {
				c.setEsame(idEsame);
				if(DataManager.editCorso(c)) {
					l.log(Level.INFO, "il corso è stato aggiornato con idEsame: "+idEsame);
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean editEsame(RealEsame e) throws Exception {
		if(e.getId() != -1 && e != null)
		{
			RealEsame tmpesame = (RealEsame) DataManager.getEsame(e.getId());
			
			if(tmpesame == null)
			{
				return false;
			}
			
			if(DataManager.editEsame(e))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ArrayList<RealStudente> getStudentiIscrizioneEsame(int idEsame) throws Exception {
		ArrayList<RealStudente> allStudenti = DataManager.getStudenti();
		l.log(Level.INFO, "gli studenti sono: "+allStudenti.size());
		
		return null;
	}

	
	

	


	
}
