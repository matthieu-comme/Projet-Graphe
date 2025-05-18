package controller;

import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Graphe;
import model.Sommet;

public class ShowSommetInfoAdapter extends MouseAdapter {
    Graphe graphe;
    Sommet sommet;

    public ShowSommetInfoAdapter(Sommet s, Graphe g) {
        this.graphe = g;
        this.sommet = s;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
            graphe.onClickShowSommetInfo(sommet);

    }
}
