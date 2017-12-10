/** Classe representant les liens entre equipement
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;

public class Station extends Equipement 
{
	private Routeur routeurLocal;
	private ArrayList<Onglet> abonnements; /**liste des canaux auquels la station est abonnee */
	private Liste<String> canauxDispo;//liste des canaux dispo
	private ArrayList<Onglet> canauxCrees;//liste des canaux crees par la station avec les messages envoyes sur ces canaux. 

	
	/** Constructeur de la classe
	 * @param  nom
	 * @param routeurlocal
	 * @param canaux 
	 * 
	 */
	public Station(String nom , Routeur routeur)
	{
		super(nom);
		this.routeurLocal=routeur ;
		this.abonnements= new ArrayList<Onglet> ();
		this.canauxDispo = new Liste<String>();
		this.canauxCrees = new ArrayList<Onglet>();
		
	}
	
	public Station(String nom)
	{
		this(nom, null);
	}
	
	/**renvoi la liste des canaux auquels la station peut s'abonner
	 * @param 
	 **/
	public void canauxDispo() throws PasDeRouteurLocalException
	{
		this.gestionnaire.addMessage(new Message( "listecanaux", this, this.getRouteurLocal(), ""));
	}
	
	public void abonnement(String clef) throws PasDeRouteurLocalException
	{
		if (this.abonnements.indexOf(clef) == -1)
		{
			this.gestionnaire.addMessage(new Message( "abonnement", this, this.getRouteurLocal(), clef));
			this.abonnements.add(new Onglet(clef));			
		}
	}
	
	public void desabonnement(String clef) throws PasDeRouteurLocalException
	{
		this.gestionnaire.addMessage( new Message("desabonnement", this, this.getRouteurLocal(), clef));
		this.abonnements.remove(clef);
		
	}
	
	public void creationCanal() throws PasDeRouteurLocalException
	{
		this.gestionnaire.addMessage(new Message("creation", this, this.getRouteurLocal(), ""));
		
	}
	
	public void recoitDonnees(Message donnee)
	{
		String[] message = donnee.getDonnees().split("@");
		
		for(Onglet onglet : this.abonnements)
		{
			if(onglet.getClef().equals(message[0]))
			{
				onglet.add(message[1]);
				this.setChanged();
				this.notifyObservers(donnee.getDonnees());
				break;
			}
		}
		
	}
	
	public void setRouteurLocal(Routeur machine)
	{
		this.routeurLocal = machine;
	}
	
	public Routeur getRouteurLocal() throws PasDeRouteurLocalException
	{
		if (this.routeurLocal == null)
		{
			System.out.println("null");
			throw new PasDeRouteurLocalException(this.getNom());
		}
		return this.routeurLocal;
	}

	public void traiteMessage(Message ordre)
	{
		switch (ordre.getType())
		{
		case "diffusion":
			this.recoitDonnees(ordre);
			break;
		case "canalcreation":
			Onglet cree = new Onglet(ordre.getDonnees());
			this.canauxCrees.add(cree);
			this.setChanged();
			this.notifyObservers(cree);
			break;
		case "listecanaux":
		    String liste= ordre.getDonnees(); /**on recupere la liste des canaux dispos */
		    String canauxdispos[]=liste.split("@"); /**on stocke les clefs des canaux dispos */
		    this.canauxDispo.deleteAll();
		    for (int i=0; i<canauxdispos.length; i++)
		    {
		    	canauxDispo.add(canauxdispos[i]);
		    }
		    break;
		}
	}
	
	public Liste<String> getCanauxDispo() {
		return canauxDispo;
	}	
	
	public void setCanauxDispo(Liste<String> canauxDispo) {
		this.canauxDispo = canauxDispo;
	}
	
	public void envoyerDonnees(String donnees, String clef) throws PasDeRouteurLocalException
	{
		for(Onglet onglet : this.canauxCrees)
		{
			if(onglet.getClef().equals(clef))
			{
				this.gestionnaire.addMessage(new Message("diffusion", this, this.getRouteurLocal(), clef+"@"+this.getNom() + ">> " + donnees));
				onglet.add(donnees);
				break;
			}
		}
	}

	public void fermerCanal(String clef)
	{
		System.out.println(clef);
		//incomplet
	}
	

}

