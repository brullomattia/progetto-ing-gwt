 package com.progetto.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.progetto.shared.AccountType;
import com.progetto.shared.concrete.RealDipartimento;
import com.progetto.shared.concrete.RealDocente;
import com.progetto.shared.concrete.RealEsame;
import com.progetto.shared.concrete.RealStudente;
import com.progetto.shared.concrete.Utente;
import com.progetto.shared.concrete.RealCorso;

/**
 * The async counterpart of <code>GreetingService</code>.
*/
public interface GreetingServiceAsync {
	void authenticate(String emial, String password,
			AsyncCallback<Utente> callback);

	void addUtente(Utente u, AsyncCallback<Boolean> callback);

	void getDipartimenti(AsyncCallback<ArrayList<RealDipartimento>> callback);

	void getUtenti(AsyncCallback<ArrayList<Utente>> callback);

	void getUtenti(ArrayList<AccountType> types,
			AsyncCallback<ArrayList<Utente>> callback);

	void removeUtente(Utente u, AsyncCallback<Boolean> callback);

	void editUtente(Utente u, String oldEmail, AsyncCallback<Boolean> callback);

	void addCorso(RealCorso c, AsyncCallback<Boolean> callback);

	void getCorsi(ArrayList<Integer> idCorsi,
			AsyncCallback<ArrayList<RealCorso>> callback);
	
	void getStudentiIscrizioneEsame(int idEsame, AsyncCallback<ArrayList<RealStudente>> callback);

	void addEsame(RealEsame e, AsyncCallback<Boolean> callback);

	void getCorsi(AsyncCallback<ArrayList<RealCorso>> callback);

	void removeCorso(RealCorso c, AsyncCallback<Boolean> callback);

	void iscrizioneCorso(RealStudente s, int idCorso,
			AsyncCallback<Boolean> callback);

	void disiscrizioneCorso(RealStudente s, int idCorso,
			AsyncCallback<Boolean> callback);

	void iscrizioneEsame(RealStudente s, int idEsame,
			AsyncCallback<Boolean> callback);

	void disiscrizioneEsame(RealStudente s, int idEsame,
			AsyncCallback<Boolean> callback);

	void getEsami(RealStudente s, AsyncCallback<ArrayList<RealEsame>> callback);

	void addCorso(RealCorso c, RealEsame e, AsyncCallback<Boolean> callback);

	void editCorso(RealCorso c, AsyncCallback<Boolean> callback);

	void editCorso(RealCorso c, int idEsame, AsyncCallback<Boolean> callback);

	void editEsame(RealEsame e, AsyncCallback<Boolean> callback);

	void getUtente(String email, AsyncCallback<Utente> callback);

	void getEsami(ArrayList<Integer> idEsami,AsyncCallback<ArrayList<RealEsame>> callback);

	void getEsami(AsyncCallback<ArrayList<RealEsame>> callback);

	void getEsame(int idEsame, AsyncCallback<RealEsame> callback);

	void checkDocente(String email, AsyncCallback<Boolean> callback);

	void addCorsoReturnId(RealCorso c, AsyncCallback<Integer> callback);

	void addEsameReturnId(RealEsame e, AsyncCallback<Integer> callback);

	void addCorsoNew(RealCorso c, RealDocente d, AsyncCallback<Integer> callback);

	void addCorsoConEsame(RealCorso c, RealDocente d, RealEsame e,
			AsyncCallback<Integer> callback);

	void setCodocente(int idCorso,String email, AsyncCallback<Boolean> callback);

	void removeCodocenza(int idCorso, String email,
			AsyncCallback<Boolean> callback);

	void caricaDatiDebug(AsyncCallback<Boolean> callback);

	void inserimentoVoto(int idEsame, int voto, String email,
			AsyncCallback<Boolean> callback);

	void addEsameACorso(RealCorso c, RealEsame e,
			AsyncCallback<Integer> callback);

	void removeEsame(RealEsame e, AsyncCallback<Boolean> callback);

	
}
