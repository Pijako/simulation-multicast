/** Exception de nom de station invalide
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

public class StationNomInvalideException extends Exception
{
	public StationNomInvalideException(String nom)
	{
		super("\nNom de station invalide : " + nom + "\nIl n'existe pas de station avec ce nom");
	}
}
