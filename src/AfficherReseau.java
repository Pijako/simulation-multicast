import java.util.ArrayList;


public class AfficherReseau
{
	private ArrayList<Equipement> reseau;
	
	public AfficherReseau(ArrayList<Equipement> reseau)
	{
		this.reseau = reseau;
	}
	
	public String afficherChemin(String station, String clef) throws StationNomInvalideException, PasDeRouteurLocalException
	{
		Station stationDepart = null;
		
		for (Equipement equipement : reseau)//on récupere la référence sur la station dont on connait le nom
		{
			if (equipement.getNom().equals(station))
			{
				stationDepart = (Station) equipement;
			}
		}
		
		String chemin = "";
		
		if (stationDepart == null)//si on trouve la station, on edite le chemin
		{
			throw (new StationNomInvalideException(station));
		}
		else
		{
			chemin += stationDepart;
			chemin += " <-- ";
			Equipement machine = stationDepart.getRouteurLocal();
			do
			{
				chemin += machine;
				chemin += " <-- ";
				machine = ((Routeur) machine).getPere(clef);
			}while(!(machine instanceof Station));
		}
		String chemin2[] = chemin.split(" <-- ");//on va retourner le chemin pour que le point de depart soit le routeur local qui a cree le canal
		String chemin3 = "";
		for (int i = chemin2.length-1; i>=0; i--)
		{
			chemin3 += chemin2[i];
			if (i != 0)
			{
				chemin3 += " --> ";
			}
		}
		return chemin3;
	}
	
	public String afficherArbre(String clef) throws PasDeRouteurLocalException
	{
		Equipement stationDepart = null;
		
		for (Equipement equipement : reseau)
		{
			if (equipement instanceof Routeur)
			{
				stationDepart = ((Routeur) equipement).getPere(clef);
				if (stationDepart != null)
				{
					while (!(stationDepart instanceof Station))
					{
						stationDepart = ((Routeur) stationDepart).getPere(clef);
					}
				}
				break;
			}
		}
		
		String[][] arbre = new String[20][20];
		if (stationDepart != null)
		{
			int i = 0;
			int j = 0;
			
			arbre[i][j] = stationDepart.toString();
			
			Equipement machine = ((Station) stationDepart).getRouteurLocal();
			this.ecrirePosition(arbre, machine, clef, i+1, j);			
		}
		String arbreDiffusion = "";
		int longueurMot = this.plusLongNom(arbre);
		
		for (int b = 0; b < arbre.length; b++)
		{
			for (int a = 0; a< arbre[b].length; a++)
			{
				if (arbre[a][b] != null)
				{
					arbreDiffusion += " -> ";
					arbreDiffusion += arbre[a][b];
					for (int i=arbre[a][b].length(); i<longueurMot; i++)
					{
						arbreDiffusion += " ";
					}
					
				}
				else
				{
					for (int i=0; i<longueurMot; i++)
					{
						arbreDiffusion += " ";
					}
					arbreDiffusion += "    ";
				}
			}
			arbreDiffusion += '\n';
		}
		return arbreDiffusion;
	}
	
	public int plusLongNom(String[][] arbre)
	{
		int plusLong = 0;
		for (int i=0; i<arbre.length; i++)
		{
			for (int j=0; j<arbre[i].length; j++)
			{
				if (arbre [i][j] != null && arbre[i][j].length() > plusLong)
				{
					plusLong = arbre[i][j].length();
				}
			}
		}
		return plusLong;
	}
	
	
	public int ecrirePosition(String[][] arbre, Equipement machine, String clef, int i, int j)
	{
		arbre[i][j] = machine.toString();
		
		if (!(machine instanceof Station))
		{
			for (Lien fils : ((Routeur) machine).getFils())
			{
				if (fils.getClef().equals(clef))
				{
					j = this.ecrirePosition(arbre, fils.getMachine(), clef, i+1, j);
					j++;
				}
			}
		}
		return j;
	}
}
