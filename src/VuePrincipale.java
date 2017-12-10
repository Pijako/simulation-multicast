/** Classe representant la Vue Principale
 * 
 * @author Thibault Pichel & Yassine Kossir
 *
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class VuePrincipale extends JFrame
{
	private GestionnaireMessage gestionnaire;
	
	private JFrame fenetre;
	
	private final JButton traiter = new JButton("Traiter");
	private final JButton traiterPlusieurs = new JButton("Traiter plusieurs messages");
	private final JButton afficherChemin = new JButton("Afficher le chemin");
	private final JButton afficherArbreDiffusion = new JButton("Afficher l'arbre de diffusion");
	
	private JList<Message> listeMessages;
	private JTextArea chemin = new JTextArea(12,20);
	private JTextArea arbre = new JTextArea(15,20);
	
	private JTextField canalArbre = new JTextField(5);
	private JTextField canalStation = new JTextField(5);
	private JTextField station = new JTextField(5);
	
	private JLabel liste = new JLabel("Liste des messages du reseau");
	
	public VuePrincipale(GestionnaireMessage gestionnaire)
	{
		this.gestionnaire = gestionnaire;
		
		listeMessages = new JList<Message>(this.gestionnaire.getListeMessages());
		
		this.fenetre = new JFrame("Simulation de protocole de diffusion multipoint");
		this.fenetre.setLocation(100, 200);
		
		//Construction de la partie centrale de l'interface
		//Construction du haut de la partie centrale
		JPanel centreNord = new JPanel();
		centreNord.add(liste);
		
		//Assemblage de la partie centrale
		JPanel centre = new JPanel(new BorderLayout());
		centre.add(new JScrollPane(listeMessages), BorderLayout.CENTER);
		JPanel centreSud = new JPanel();
		centreSud.add(traiter);
		centreSud.add(traiterPlusieurs);
		centre.add(centreSud, BorderLayout.SOUTH);
		centre.add(centreNord, BorderLayout.NORTH);
		
		//Construction de la partie droite de l'interface
		//Construction du haut de cette partie
		JPanel estNordCentre = new JPanel();
		estNordCentre.add(new JLabel("Station :"));
		estNordCentre.add(station);
		estNordCentre.add(new JLabel("Canal :"));
		estNordCentre.add(canalStation);
		JPanel estNord = new JPanel(new BorderLayout());
		estNord.add(estNordCentre, BorderLayout.CENTER);
		estNord.add(afficherChemin, BorderLayout.SOUTH);
		
		//Assemblage de la partie droite
		JPanel est = new JPanel(new BorderLayout());
		est.add(estNord, BorderLayout.NORTH);
		est.add(new JScrollPane(chemin), BorderLayout.CENTER);
		
		//Construction de la partie gauche de l'interface
		//Construction du haut de cette partie
		JPanel ouestNord = new JPanel(new BorderLayout());
		JPanel ouestNordNord = new JPanel();
		ouestNordNord.add(new JLabel("Canal :"));
		ouestNordNord.add(canalArbre);
		ouestNord.add(ouestNordNord, BorderLayout.NORTH);
		ouestNord.add(afficherArbreDiffusion, BorderLayout.SOUTH);
		
		
		//Assemblage de la partie gauche
		JPanel ouest = new JPanel(new BorderLayout());
		ouest.add(ouestNord, BorderLayout.NORTH);
		ouest.add(new JScrollPane(arbre), BorderLayout.CENTER);
		
		//Assemblage de toutes les parties
		this.fenetre.getContentPane().add(ouest, BorderLayout.WEST);
		this.fenetre.getContentPane().add(centre, BorderLayout.CENTER);
		this.fenetre.getContentPane().add(est, BorderLayout.EAST);
		
		//Construire gestionionnaire des evenements
		this.fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//afficher la fenetre
		this.fenetre.pack(); //redimmensionner la fenetre
		this.fenetre.setVisible(true);
		
		this.traiter.addActionListener(new ActionTraiter(false));
		this.traiterPlusieurs.addActionListener(new ActionTraiter(true));
		this.afficherChemin.addActionListener(new ActionAfficherChemin());
		this.afficherArbreDiffusion.addActionListener(new ActionAfficherArbre());
	}


	
	public class ActionTraiter implements ActionListener
	{
		Boolean plusieurs;
		public ActionTraiter(Boolean plusieurs)
		{
			this.plusieurs=plusieurs;
		}
		public void actionPerformed(ActionEvent arg0)
		{
			int indexMessage = 0;
			if(this.plusieurs)
			{
				
				int boucle;
				try
				{
					boucle = Integer.parseInt(JOptionPane.showInputDialog(fenetre, "Entrez un nombre", "Liste vide", JOptionPane.WARNING_MESSAGE));
					for (int i=0; i<boucle; i++)
					{
						try
						{
							indexMessage = listeMessages.getLastVisibleIndex();
							gestionnaire.traiterVue(indexMessage);
							gestionnaire.supprimerMessage(indexMessage);
						}
						catch(IndexOutOfBoundsException e)
						{
							JOptionPane.showMessageDialog(fenetre, "La liste de message du reseau est vide", "Liste vide", JOptionPane.WARNING_MESSAGE);
							break;
						}
					}
				}
				catch (NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(fenetre, e1, "Erreur saisi", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				indexMessage = listeMessages.getSelectedIndex();
				try
				{
					gestionnaire.traiterVue(indexMessage);
					gestionnaire.supprimerMessage(indexMessage);
				}
				catch(IndexOutOfBoundsException e)
				{
					int tailleList = listeMessages.getLastVisibleIndex();
					if (tailleList == -1)
					{
						JOptionPane.showMessageDialog(fenetre, "La liste de message du reseau est vide", "Liste vide", JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(fenetre, "Veuillez selectioner un message a traiter.", "Erreur de selection", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		}
	}
	
	
	public class ActionAfficherChemin implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			AfficherReseau donnerChemin = new AfficherReseau(gestionnaire.reseau);
			String cheminStation;
			try
			{
				cheminStation = donnerChemin.afficherChemin(station.getText(), canalStation.getText());
				chemin.setText(cheminStation);
			}
			catch (StationNomInvalideException e)
			{
				JOptionPane.showMessageDialog(fenetre, e, "Nom de station invalide", JOptionPane.WARNING_MESSAGE);
			}
			catch (NullPointerException e)
			{
				JOptionPane.showMessageDialog(fenetre, "Veuillez indiquer un nom de canal existant", "Cle de canal invalide", JOptionPane.WARNING_MESSAGE);
			}
			catch (PasDeRouteurLocalException e)
			{
				JOptionPane.showMessageDialog(fenetre, e, "Cle de canal invalide", JOptionPane.WARNING_MESSAGE);
			}
			
		}
	}
	
	public class ActionAfficherArbre implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			AfficherReseau donnerArbre = new AfficherReseau(gestionnaire.reseau);
			String arbreDiffusion;
			try {
				arbreDiffusion = donnerArbre.afficherArbre(canalArbre.getText());
				arbre.setText(arbreDiffusion);
			} catch (PasDeRouteurLocalException e) {
				e.printStackTrace();
			} 
			
		}
		
	}
}
