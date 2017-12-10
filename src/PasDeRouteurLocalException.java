/** Exception de nom d'absence de routeur local
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

public class PasDeRouteurLocalException extends Exception
{
	public PasDeRouteurLocalException(String nom)
	{
		super("\nPas de routeur local : \n"+nom);
	}
}
