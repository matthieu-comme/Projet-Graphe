package model;

import java.awt.Point;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class Arc implements Serializable {
    private Sommet s1, s2;
    private int poids;

    public Arc(Sommet s1, Sommet s2, Graphe g) {
        try {
            // verifDebutFin(s1, s2, g);
            this.s1 = s1;
            this.s2 = s2;
            if (s1.equals(g.getDebut()))
                this.poids = 0;
            else
                this.poids = 1;
            g.updateDates();
        } catch (ArcException e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e.getMessage(), "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Arc(Sommet s1, Sommet s2, int poids) {
        this.s1 = s1;
        this.s2 = s2;
        if (poids < 0) {
            throw new ArcException("Un arc ne peut pas avoir un poids négatif");
        }
        this.poids = poids;
    }

    private void verifDebutFin(Sommet s1, Sommet s2, Graphe g) {
        if (s2.equals(g.getDebut()))
            throw new ArcException("Un arc ne peut pointer vers le début");
        else if (s1.equals(g.getFin()))
            throw new ArcException("Un arc ne peut partir de la fin");
    }

    public Sommet getS1() {
        return s1;
    }

    public Sommet getS2() {
        return s2;
    }

    public Point getP1() {
        return s1.getPos();
    }

    public Point getP2() {
        return s2.getPos();
    }

    public int getPoids() {
        return this.poids;
    }

    public void setPoids(int poids) {
        if (poids < 0) {
            throw new ArcException("Un arc ne peut pas avoir un poids négatif");
        }
        this.poids = poids;
    }

    @Override
    public String toString() {
        return s1.getName() + " -> " + s2.getName();
    }
}
