/** Classe représentant les liens entre equipement
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class Liste<E> extends DefaultListModel<E>
{
	ArrayList<E> list;
	
	public Liste()
	{
		super();
		list = new ArrayList<E>();
	}
	
	public E getElementAt(int arg0)
	{
		return list.get(arg0);
	}

	public int getSize()
	{
		return list.size();
	}


	public void add(E string)
	{
		list.add(string);
		this.addElement(string);
		
	}

	public void delete(int selectedIndex)
	{
		list.remove(selectedIndex);
		this.removeElementAt(selectedIndex);
		
	}
	
	public String toString()
	{
		String mm = "";
		for (E m : list)
		{
			mm += m;
			mm += "\n";
		}
		return mm;
	}

	public void deleteAll()
	{
		list.clear();
		this.clear();
	}

}
