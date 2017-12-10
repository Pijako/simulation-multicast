/** Classe représentant un canal
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;


public class Canal
{
	private String clef;
	private Station mere;
	private ArrayList<Equipement> connecte;

	public Canal(String clef, Station mere)
	{
		this.connecte = new ArrayList();
		this.clef = clef;
		this.mere = mere;
	}
	

	/** Connaitre la liste des canaux connectés
	* @return la liste
	*/
	
	public ArrayList<Equipement> canauxconnectes() {
		return this.connecte;
	}
	
	/** Obtenir la clé du canal
	* @return la clef
	*/
	
	public String getClef() {
		return this.clef;
	}


	/** Obtenir le nom de la station mère
	* @return
	*/

	public Station getMere() {
		return this.mere;	
	}

}
