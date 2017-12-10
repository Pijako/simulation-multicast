/** Classe représentant un message
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

public class Message 
{
		 
	/* Attributs */
	private String type;//type du message : prolonge recouvrement, abonnement, envoi de données,...
	private Equipement destinataire;
	private Equipement emetteur;	
	private String donnees;

		
	public Message(String type, Equipement emetteur, Equipement destinataire, String donnees)
	{

		/* Constructeurs */
			
		this.type=type;
		this.destinataire=destinataire;
		this.emetteur=emetteur;
		this.donnees=donnees;
	}		


		/* Methodes */


		/** Obtenir le type du message 
		*
		*@return	
		*/
		
		public String getType()
		{
			return this.type;
		}

		/** Connaitre le destinataire du message 
		*
		*@return	
		*/
		
		public Equipement getDestinataire() {
			return this.destinataire;
		}

		/** Connaitre l'emetteur du message 
		*
		*@return	
		*/
		
		public Equipement getEmetteur() {
			return this.emetteur;
		}

		/** Connaitre donnees du message recu sur un canal 
		*
		*@return	
		*/
		
		public String getDonnees() {
			return this.donnees;
		}

		public String toString()
		{
			return getType() + " " + getEmetteur() + " " + getDestinataire() + " " + " " + getDonnees();
		}

}
