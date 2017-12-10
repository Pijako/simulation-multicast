/*
 * Classe qui représente un canal cree par une station.
 * A chaque fois qu'une station va faire un nouveau canal,
 * elle ajoute un nouvel objet de type onglet a sa liste des canaux cree
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;

public class Onglet extends ArrayList<String>
{
	private String clef;//Clef du canal cree

	public Onglet(String clef)
	{
		super();
		this.clef = clef;
	}
	
	public String getClef()
	{
		return this.clef;
	}
}
