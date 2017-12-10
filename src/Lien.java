/** Classe représentant les liens entre equipement
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

public class Lien
{
	private Equipement machine;//le routeur machine d'un lien ou bien les stations/routeurs fils pour la diffusion
	private String clef;//la clé du canal auquel appartient le lien
	
	/** Constructeur d'un lien
	 * @param machine
	 * @param clef
	 * @param etat
	 */
	public Lien(Equipement machine, String clef)
	{
		this.machine = machine;
		this.clef = clef;
	}
	
	/** Renvoi le routeur père de la liaison
	 * @return routeur machine
	 */
	public Equipement getMachine()
	{
		return this.machine;
	}
	
	/** Renvoi la clef du canal auquel appartient la liaison
	 * @return String clef
	 */
	public String getClef()
	{
		return this.clef;
	}
}
