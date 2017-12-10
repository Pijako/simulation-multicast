/** Classe principale avec fonction main
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProtocoleDiffusionMultipoint
{
	/** Initialise un réseau : construit tous les équipements, les connexions inter-équipements, initialise le gestionnaire de message qui prend la main
	 * pour traiter les messages issu du fonctionnement du réseau
	 * @param args
	 */
	public static void main(String[] args)
	{
		gestionOption(args);
		
		GestionnaireMessage m = new GestionnaireMessage();		
		//Création des machines du réseau en lisant la configuration réseau
		ArrayList<Equipement> machines = lectureTopologie(args[0]);
		m.setReseau(machines);

		
		VuePrincipale vu = new VuePrincipale(m);

		for(Equipement machine : machines)
		{
			if (machine instanceof Station)
			{
				new VueStation((Station) machine);
			}
		}
		
		
	}
	
	private static Boolean gestionOption(String[] args)
	{
		if (args.length > 2)
		{
			return false;
		}
		else
		{
			return true;
		}
		
		
	}

	/** Renvoi l'index d'une machine de la liste des machines du reseau en fonction de son nom
	 * @param nom nom de la machine a trouver
	 * @param reseau liste des machinesdu reseau
	 * @return index de la machine trouve ou -1 si elle n'y est pas
	 */
	private static int indexEquipement(String nom, ArrayList<Equipement> reseau)
	{
		int index = -1;
		for (Equipement lien : reseau)
		{
			if (lien.getNom().contentEquals(nom))
			{
				index = reseau.indexOf(lien);
			}
		}
		return index;
	}
	
	private static ArrayList<Equipement> lectureTopologie(String fichierReseau)
	{	
		Scanner scanner;
		ArrayList<Equipement> machines = new ArrayList<Equipement>();
		try
		{
			scanner = new Scanner(new File(fichierReseau));

			// On boucle sur chaque champ detecte
			while (scanner.hasNextLine())
			{
			    String ligne = scanner.nextLine();
			    System.out.println(ligne);
			 
			    traitementLigne(ligne, machines);
			}
			
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return machines;
	}
	
	private static void traitementLigne(String ligne, ArrayList<Equipement> machines)
	{
		String[] donnees = ligne.split(" ");
		
		switch(donnees[0])
		{
		case "routeur":
			machines.add(new Routeur(donnees[1]));
			break;
		case "station":
			machines.add(new Station(donnees[1]));
			break;
		case "lien":
			try
			{
				Equipement machine1 = machines.get(indexEquipement(donnees[1], machines));
				Equipement machine2 = machines.get(indexEquipement(donnees[2], machines));
				
				if (machine1 instanceof Routeur && machine2 instanceof Routeur)
				{
					((Routeur) machine1).setVoisin((Routeur) machine2);
					((Routeur) machine2).setVoisin((Routeur) machine1);
				}
				else if (machine1 instanceof Routeur && machine2 instanceof Station)
				{
					((Station) machine2).setRouteurLocal((Routeur) machine1);
				}
				else if (machine1 instanceof Station && machine2 instanceof Routeur)
				{
					((Station) machine1).setRouteurLocal((Routeur) machine2);
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				if (indexEquipement(donnees[1], machines) == -1)
				{
					System.out.println("Erreur, la machine " + donnees[1] + " n'existe pas");
				}
				else
				{
					System.out.println("Erreur, la machine " + donnees[2] + " n'existe pas");
				}
			}
			break;
		default:
			System.out.println("erreur de reconnaissance de l'intitule");
		}
	}

}
