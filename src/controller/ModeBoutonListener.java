package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Graphe;
import model.Sommet;

public class ModeBoutonListener implements ActionListener {
    private Graphe graphe;
    static JButton start = null; // mémorise le premier bouton cliqué quand on crée une arête

    public ModeBoutonListener(Graphe g) {
        this.graphe = g;
    }

    public void actionPerformed(ActionEvent e) {
        int mode = Graphe.getMode();
        JButton source = (JButton) e.getSource();
        String name = source.getText();

        // 2 pour ajouter des arêtes
        if (mode == 2) {
            if (start == null) {
                start = source;
            } else {
                graphe.addChild(start, source);
                start = null;
            }
            // 3 pour supprimer Sommet
        } else if (mode == 3 && !name.equals("Début") && !name.equals("Fin")) {
            ArrayList<Sommet> g = graphe.getGraphe();
            for (Sommet s : g) {
                s.getSuccesseurs().removeIf(fils -> fils.getName().equals(name));
            }
            g.removeIf(s -> s.getName().equals(name));
            graphe.getLignePanel().delHitBox(name);
            graphe.updateDates();
            JPanel p = graphe.getP();
            p.remove(source);
            p.repaint();
        }
    }
}
