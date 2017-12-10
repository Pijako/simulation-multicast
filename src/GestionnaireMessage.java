/** Classe traitant les messages
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class GestionnaireMessage extends Observable
{
	private Liste<Message> listeMessages;
	public ArrayList<Equipement> reseau;
	
	/** Construit un gestionnaire de message pour un reseau avec une liste de message
	 * @param machines les machines du reseau
	 */
	public GestionnaireMessage()
	{
		this.reseau = null;
		listeMessages = new Liste<Message>();
	}
	
	/** Indique au gestionnaire de message avec quel reseau il travaille et indique aux machines avec quel gestionnaire elles travaillent
	 * @param reseau
	 */
	public void setReseau(ArrayList<Equipement> reseau)
	{
		this.reseau = reseau;
		for (Equipement machine : this.reseau)
		{
			machine.setGestionnaire(this);
		}
	}
	
	public void addMessage(Message nouveau)
	{
		this.getListeMessages().add(nouveau);
		Equipement.ajoutLog(nouveau);
		this.setChanged();
		this.notifyObservers(nouveau);
	}
	
	/** Choisi aleatoirement un message dans la liste des messages
	 */
	public void traiterListeAlea()
	{
		Random numeroLigne = new Random();
		this.repartirMessage(numeroLigne.nextInt()%getListeMessages().size());
	}
	
	/** Traite la liste des messages comme une FIFO 
	 */
	public void traiterListeFIFO()
	{
		this.repartirMessage(0);
	}
	
	/** Traite le message selectionne de la liste
	 * @param indexMessage
	 */
	public void traiterVue(int indexMessage)
	{
		this.repartirMessage(indexMessage);
	}
	
	/** Envoi le message choisi au destinataire concerne
	 * @param index position ud message choisi dans la liste des messages
	 */
	public void repartirMessage(int index)
	{
		Equipement machine = this.getListeMessages().get(index).getDestinataire();
		machine.traiteMessage(this.getListeMessages().get(index));
	}

	/** Retire le message de la liste des messages du reseau
	 * @param indexMessage index du message a retirer de la liste
	 */
	public void supprimerMessage(int indexMessage)
	{
		this.getListeMessages().delete(indexMessage);		
	}

	/** Renvoi la liste des messages du gestionnaire en transit dans le reseau
	 * @return
	 */
	public Liste<Message> getListeMessages()
	{
		return listeMessages;
	}
}
