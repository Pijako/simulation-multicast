/** Classe abstraite Equipement
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public abstract class Equipement extends Observable
{
	String nom;
	GestionnaireMessage gestionnaire;
	
	public Equipement(String nom)
	{
		this.nom = nom;
		this.gestionnaire = null;
		this.reinitialisationLog();
	}

	public String getNom()
	{
		return this.nom;
	}
	
	public String toString()
	{
		return this.getClass().getSimpleName() + " " + this.getNom();
	}
	
	public void setGestionnaire(GestionnaireMessage m)
	{
		this.gestionnaire = m;
	}
	
	public void reinitialisationLog()
	{
		File log = new File(this.getNom() + ".Log.txt");
		log.delete();
	}
	
	public static void ajoutLog(Message ordre)
	{
		//BufferedWriter a besoin d un FileWriter, 
		//les 2 vont ensemble, on donne comme argument le nom du fichier
		//true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 
		FileWriter fw1;
		FileWriter fw2;
		try
		{
			fw1 = new FileWriter(ordre.getEmetteur().getNom()+".Log.txt", true);
			fw2 = new FileWriter(ordre.getDestinataire().getNom()+".Log.txt", true);
		
			// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
			BufferedWriter output1 = new BufferedWriter(fw1);
			BufferedWriter output2 = new BufferedWriter(fw2);
			
			//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
			output1.write("Recus : " + ordre.toString() + "\r\n");
			output2.write("Envoye : " + ordre.toString() + "\r\n");
			
			//on peut utiliser plusieurs fois methode write
			
			output1.flush();
			output2.flush();
			//ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
			
			output1.close();
			output2.close();
			//et on le ferme
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	abstract public void traiteMessage(Message message);

}
