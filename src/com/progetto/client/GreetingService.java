package com.progetto.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealDipartimento;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.concrete.RealCorso;
import com.progetto.shared.concrete.RealEsame;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Utente authenticate(String emial, String password) throws IllegalArgumentException;
	Boolean addUtente(Utente u) throws Exception;
	Boolean editUtente(Utente u, String oldEmail) throws Exception;
	Boolean removeUtente(Utente u) throws Exception;
	ArrayList<Utente> getUtenti() throws Exception;
	ArrayList<Utente> getUtenti(ArrayList<AccountType> types) throws Exception;
	ArrayList<RealDipartimento> getDipartimenti() throws Exception;	
	boolean addCorso(RealCorso c) throws Exception;
	boolean addCorso(RealCorso c, RealEsame e) throws Exception;
	boolean editCorso(RealCorso c) throws Exception;
	boolean editCorso(RealCorso c, int idEsame) throws Exception;
	boolean removeCorso(RealCorso c) throws Exception;
	ArrayList<RealCorso> getCorsi() throws Exception;
	ArrayList<RealCorso> getCorsi(ArrayList<Integer> idCorsi) throws Exception;
	boolean iscrizioneCorso(RealStudente s, int idCorso) throws Exception;
	boolean disiscrizioneCorso(RealStudente s, int idCorso) throws Exception;	
	boolean addEsame(RealEsame e) throws Exception;
	boolean editEsame(RealEsame e) throws Exception;
	ArrayList<RealEsame> getEsami(RealStudente s) throws Exception;
	boolean iscrizioneEsame(RealStudente s, int idEsame) throws Exception;
	boolean disiscrizioneEsame(RealStudente s, int idEsame) throws Exception;
	Utente getUtente(String email) throws Exception;
	ArrayList<RealEsame> getEsami(ArrayList<Integer> idEsami) throws Exception;
	ArrayList<RealEsame> getEsami() throws Exception ;
	RealEsame getEsame(int idEsame) throws Exception;
	boolean checkDocente(String email) throws Exception ;
	int addCorsoReturnId(RealCorso c) throws Exception;
	int addEsameReturnId(RealEsame e) throws Exception;
	int addCorsoNew(RealCorso c, RealDocente d) throws Exception;
	int addCorsoConEsame(RealCorso c, RealDocente d, RealEsame e) throws Exception;
	ArrayList<RealStudente> getStudentiIscrizioneEsame(int idEsame) throws Exception;
	boolean setCodocente(int idCorso,String email) throws Exception;
	boolean removeCodocenza(int idCorso, String email) throws Exception;
	boolean caricaDatiDebug() throws Exception;
	boolean inserimentoVoto(int idEsame, int voto, String email)throws Exception;
	int addEsameACorso(RealCorso c, RealEsame e) throws Exception;
	boolean removeEsame(RealEsame e) throws Exception;

	
	
}
