package model;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import controller.LignePanelListener;
import controller.ModeBoutonListener;
import panel.AbstractInfoPanel;
import panel.InfoArcPanel;
import panel.InfoSommetPanel;
import panel.LignePanel;
import view.AddSommetDialog;

public class Graphe implements LignePanelListener {

	private JPanel p;
	private ArrayList<Sommet> graphe;
	private LignePanel lignePanel;
	private static int mode = 1; // pour savoir le mode séléctionné (ajout sommet, arêtes ...)
	private AbstractInfoPanel infos;
	private Sommet debut, fin;

	public Graphe(JPanel p) {
		this.p = p;
		graphe = new ArrayList<Sommet>();
		lignePanel = new LignePanel();
		lignePanel.setListener(this);
		lignePanel.setBounds(0, 0,
				Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		p.add(lignePanel);

		debut = new Sommet(
				new Point(Toolkit.getDefaultToolkit().getScreenSize().width * 10 / 100,
						(int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2.5)),
				this);
		graphe.add(debut);
		debut.setName("Début");
		p.add(debut.getButton());
		addListenersTo(debut.getButton());

		fin = new Sommet(
				new Point(
						Toolkit.getDefaultToolkit().getScreenSize().width * 80 / 100,
						(int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2.5)),
				this);
		graphe.add(fin);
		fin.setName("Fin");
		p.add(fin.getButton());
		addListenersTo(fin.getButton());

		p.repaint();
		p.setComponentZOrder(lignePanel, p.getComponentCount() - 1);
		Sommet.setLignePanel(lignePanel);

	}

	public JButton addItem(Point pos) {
		Sommet s = new Sommet(pos, this);
		new AddSommetDialog(s, this);
		p.setComponentZOrder(lignePanel, p.getComponentCount() - 1);
		p.revalidate();
		p.repaint();
		return s.getButton();
	}

	public void addChild(JButton parent, JButton child) {
		Sommet par = null, enfant = null;
		for (Sommet s : graphe) {
			if (s.getName() == parent.getText()) {
				par = s;
			}
			if (s.getName() == child.getText()) {
				enfant = s;
			}
		}
		if (par != enfant) {
			par.addSucc(enfant);
			lignePanel.addHitBox(par, enfant, this);
		}

		for (Component comp : p.getComponents()) {
			if (comp instanceof JButton) {
				p.setComponentZOrder(comp, 0);
			}
			if (comp instanceof LignePanel) {
				comp.repaint();
			}
		}
	}

	public void onClickAddSommet(Point p) {
		addListenersTo(addItem(p));
	}

	public void onClickDelArc(Arc a) {
		a.getS1().delSucc(a.getS2());
		lignePanel.delHitBox(a);
		updateDates();
		p.repaint();

	}

	public void onClickShowArcInfo(Arc a) {
		if (!a.getS1().equals(getDebut())) { // on ne peut pas modifier le poids des arcs ayant origine le début
			clearInfoPanel();
			infos = new InfoArcPanel(a, this);
			afficherInfos(infos, p);
		}
	}

	public void onClickShowSommetInfo(Sommet s) {
		if (mode == 0) {
			if (!s.equals(getDebut()) && !s.equals(getFin())) { // on ne peut pas modifier début et fin
				clearInfoPanel();
				infos = new InfoSommetPanel(s, this);
				afficherInfos(infos, p);
			}
		}
	}

	private void afficherInfos(AbstractInfoPanel infos, JPanel p) {
		p.add(infos);
		infos.setLocation((int) (p.getSize().getWidth() - 250), (int) 10);
		infos.getTextField().requestFocus();
		p.repaint();
		p.revalidate();
		p.setComponentZOrder(infos, 0);
	}

	private void clearInfoPanel() {
		if (infos != null) {
			p.remove(infos);
			p.revalidate();
			p.repaint();
		}
	}

	public void updateDates() {
		updateDatePlusTot();
		updateDatePlusTard();
		// if (!fin.getPredecesseurs(this).isEmpty()) { // on colore pas tant qu'il y a
		// aucun arc vers fin
		System.out.println(getArcs());
		for (Sommet s : graphe) {
			System.out.println("Nom: " + s.getName() + ", datePlusTot: " + s.getDatePlusTot() + ", datePlusTard: "
					+ s.getDatePlusTard());
			boolean isole = s.getPredecesseurs(this).isEmpty() && s.getSuccesseurs().isEmpty();
			if (s.getDatePlusTot() == s.getDatePlusTard() & !isole) // chemin critique
				s.getButton().setBackground(Color.RED);
			else
				s.getButton().setBackground(Color.lightGray);
		}
		// }
	}

	private void updateDatePlusTot() {
		ArrayList<Sommet> file = new ArrayList<Sommet>();
		file.add(debut);
		while (!file.isEmpty()) {
			Sommet s = file.get(0);
			s.updateDatePlusTot(this);
			for (Sommet succ : s.getSuccesseurs()) {
				file.add(succ);
			}
			file.remove(s);
		}
	}

	private void updateDatePlusTard() { // doit etre appelé apres updateDatePlusTot
		ArrayList<Sommet> file = new ArrayList<Sommet>();
		file.add(fin);
		while (!file.isEmpty()) {
			Sommet s = file.get(0);
			s.updateDatePlusTard(this);
			ArrayList<Sommet> preds = s.getPredecesseurs(this);
			for (Sommet pred : preds) {
				file.add(pred);
			}
			file.remove(s);
		}

	}

	public void setMode(int i) {
		mode = i;
		lignePanel.setMode(i);
	}

	public static int getMode() {
		return mode;
	}

	public JPanel getP() {
		return p;
	}

	public Sommet getDebut() {
		return debut;
	}

	public Sommet getFin() {
		return fin;
	}

	public void setDebut(Sommet s) {
		this.debut = s;
	}

	public void setFin(Sommet s) {
		this.fin = s;
	}

	private void addListenersTo(JButton b) {
		b.addActionListener(new ModeBoutonListener(this));
	}

	public static boolean nomDisponible(String name, Sommet s, Graphe g) {
		for (Sommet t : g.getGraphe()) {
			if (t.getName().equalsIgnoreCase(name) && !s.equals(t))
				return false;
		}
		return true;
	}

	public ArrayList<Sommet> getGraphe() {
		return graphe;
	}

	public void setGraphe(ArrayList<Sommet> graphe) {
		this.graphe = graphe;
	}

	public void setArcs(ArrayList<Arc> arcs) {
		lignePanel.setArcs(arcs);
	}

	public ArrayList<Arc> getArcs() {
		return lignePanel.getArcs();
	}

	public Arc getArc(Sommet s1, Sommet s2) {
		return lignePanel.getArc(s1, s2);
	}

	public LignePanel getLignePanel() {
		return lignePanel;
	}
}
