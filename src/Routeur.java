/** Classe représentant les liens entre equipement
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;
import java.util.Random;

public class Routeur extends Equipement
{
	private ArrayList<Equipement> voisins;//liste des voisins du routeur
	private ArrayList<Lien> pere;//liste des canaux qui passent par ce routeur, avec les pères et l'état diffusant
	private ArrayList<Lien> fils;//liste des canaux qui diffusent aux travers de ce routeur ainsi que des fils concerné par la diffusion
	
	/** Constructeur de la classe en specifiant le nom
	 * @param nom
	 * @param voisins
	 */
	public Routeur(String nom)
	{
		super(nom);
		this.voisins = new ArrayList<Equipement>();
		this.pere = new ArrayList<Lien>();
		this.fils = new ArrayList<Lien>();
	}
	
	/** Traite les messages refus du gestionnaire de message
	 * @param ordre message du gestionnaire de message
	 */
	public void traiteMessage(Message ordre)
	{
		switch (ordre.getType())
		{
		case "recouvrement":
			this.recoitRecouvrement(ordre.getEmetteur(), ordre.getDonnees());
			break;
		case "abonnement":
			this.abonnement(ordre.getEmetteur(), ordre.getDonnees());
			break;
		case "diffusion":
			this.diffusion(ordre.getDonnees());
			break;
		case "creation":
			this.creerCanal(ordre.getEmetteur());
			break;
		case "listecanaux":
			this.getCanaux((Station) ordre.getEmetteur());
			break;
		case "desabonnement":
			this.desabonnement(ordre.getEmetteur(), ordre.getDonnees());
			break;
		}
	}
	
	/** Supprimer la station de la liste de diffusion pour un canal
	 * @param emetteur
	 * @param donnees
	 */
	private void desabonnement(Equipement emetteur, String donnees)
	{
		int index = -1;
		for (Lien fils : this.getFils())
		{
			if (fils.getMachine() == emetteur && fils.getClef() == donnees)
			{
				index = this.getFils().indexOf(fils);
			}
		}
		this.getFils().remove(index);
	}
	
	/** Fait remonter l'abonnement pour une diffusion vers son père
	 * @param fils équipement qui demande la diffusion
	 * @param clef clé du canal qui doit diffuser
	 */ 
	private void abonnement(Equipement fils, String clef)
	{
		if (!this.dejaFils(fils, clef))
		{
			this.getFils().add(new Lien(fils, clef));//on ajoute le fils à la liste des équipements qui recoivent la diffusion
			
			this.gestionnaire.addMessage(new Message("abonnement", this, this.getPere(clef), clef));
		}
	}

	/** Propage le recouvrement d'un canal à ses voisins
	 * @param clef : clef du canal à recouvrer
	 */
	private void propageRecouvrement(String clef)
	{
		for (Equipement voisin : this.voisins)
		{
			this.gestionnaire.addMessage(new Message("recouvrement", this, voisin, clef));
		}
	}
	
	
	/** Creer un canal
	 * @param equipement station initiatrice du canal
	 */
	private void creerCanal(Equipement equipement)
	{
		String clef = this.genererClef();
		this.pere.add(new Lien(equipement, clef));
		this.propageRecouvrement(clef);
		this.gestionnaire.addMessage(new Message("canalcreation", this, equipement, clef));
	}
	
	/** Generer une clef aleatoire de caractere lors de la creation d'un canal
	 * @return clef
	 */
	private String genererClef()
	{
		String clef = "";
		for (int i = 0; i<4; i++)
		{
			Random generateurCarac = new Random();
			char val =(char) ('a' + generateurCarac.nextInt(26));
			clef += val;
		}
		return clef;
	}

	/** Recoit une demande de propagation d'un recouvrement
	 * @param pere la station qui demande le recouvrement
	 * @param clef la clé du canal à recouvrer
	 */
	private void recoitRecouvrement(Equipement pere, String clef)
	{
		Lien aAjouter = new Lien(pere, clef);
		if (this.getPere(clef) == null)//si cet équipement pour ce canal n'est pas enregistré, on l'enregistre comme pere pour ce canal
		{
			this.pere.add(aAjouter);
			this.propageRecouvrement(clef);
		}
	}
	
	/** Transmet les donnees sur l'arbre de diffusion
	 * @param clef clé du canal qui doit diffuser
	 */ 
	private void diffusion(String donnees)
	{
		String[] traitement = donnees.split("@");
		for (Lien fils : this.fils)
		{
			if(fils.getClef().equals(traitement[0]))
			{
				this.gestionnaire.addMessage(new Message("diffusion", this, fils.getMachine(), donnees));
			}
		}
	}
	
	/** Renvoi la liste des canaux qui passent par le routeur
	 * @return canaux
	 */
	private void getCanaux(Station destinataire)
	{
		String canaux = "";
		for(Lien canal : this.pere)
		{
			if (canaux.indexOf(canal.getClef()) == -1)
			{
				canaux += canal.getClef();
				canaux += '@';
			}
		}
		this.gestionnaire.addMessage(new Message("listecanaux", this, destinataire, canaux));
	}
	
	/** Renvoi le père pour un canal donné
	 * @param clef clé du canal considéré
	 * @return le père de l'équipement pour ce canal
	 */
	public Equipement getPere(String clef)
	{
		Equipement pere = null;
		for (Lien p : this.pere)
		{
			if (p.getClef().equals(clef))
			{
				pere = p.getMachine();
				break;
			}
		}
		return pere;
	}
	
	/** Mettre à jour les voisins
	 * @param voisins
	 */
	public void setVoisin(Routeur voisins)
	{
		this.voisins.add(voisins);
	}

	/** Definit le gestionnaire de message avec lequel va travailler la machine
	 */
	public void setGestionnaire(GestionnaireMessage m)
	{
		this.gestionnaire = m;
	}


	public ArrayList<Lien> getFils()
	{
		return fils;
	}
	
	/** Test si un equipement pour un canal donne est deje fils de l'equipement actuel
	 * @param fils
	 * @param clef
	 * @return
	 */
	public Boolean dejaFils(Equipement fils, String clef)
	{
		Boolean dejaFils = false;
		for (Lien test : this.fils)
		{
			if (test.getMachine() == fils && test.getClef().equals(clef))
			{
				dejaFils = true;
				break;
			}
		}
		return dejaFils;
	}
}
