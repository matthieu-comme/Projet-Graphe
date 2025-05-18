package model;

import java.io.Serializable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import components.RoundBtn;
import panel.LignePanel;

public class Sommet implements Serializable {
    private Point pos;
    private String name, desc;
    private transient RoundBtn b; // transient = ignoré pendant la serialization
    private ArrayList<Sommet> successeurs;
    private transient Point offset; // décalage entre le clic et la pos du bouton
    private static LignePanel lignePanel = null;
    private int datePlusTot, datePlusTard;

    public Sommet(Point pos, Graphe g) {
        this.name = "";
        this.pos = pos;
        successeurs = new ArrayList<Sommet>();
        this.b = new RoundBtn(this, g);
        addDragAndDrop(b);
        datePlusTot = 0;
        datePlusTard = 0;
    }

    public void addDragAndDrop(JButton bouton) {
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                offset = e.getPoint(); // point cliqué dans le bouton
            }
        });

        bouton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (Graphe.getMode() == 0) {
                    // a partir du point dans le bouton, on obtient (x,y) dans le panel
                    Component parent = bouton.getParent();
                    if (parent != null) {
                        Point point = SwingUtilities.convertPoint(bouton, e.getPoint(), parent); // change référentiel
                        int x = point.x - offset.x;
                        int y = point.y - offset.y;
                        ArrayList<Sommet> selectSommet = lignePanel.getSelectedSommet();
                        /**
                         * Si des sommets sont séléctionnés
                         */
                        if (!selectSommet.isEmpty()) {
                            int xChange = x - pos.x;
                            int yChange = y - pos.y;
                            for (Sommet s : selectSommet) {
                                s.setPos(s.pos.x + xChange, s.pos.y + yChange);
                            }
                        } else {
                            setPos(x, y);
                        }
                        parent.repaint();
                    }
                }
            }
        });
    }

    public void updateDatePlusTot(Graphe g) {
        int res = 0;
        ArrayList<Sommet> pred = getPredecesseurs(g);
        if (!pred.isEmpty()) {
            // res = Integer.MIN_VALUE;
            // compare (date au plus tot + poids arc) de tous ses pred et garde le max
            for (Sommet s : pred) {
                Arc a = g.getArc(s, this);
                if (a != null) {
                    int date = s.getDatePlusTot() + a.getPoids();
                    res = Integer.max(date, res);
                }
            }
        }
        this.datePlusTot = res;
    }

    public void updateDatePlusTard(Graphe g) {
        int res = g.getFin().getDatePlusTot(); // majorant
        ArrayList<Sommet> succ = getSuccesseurs();
        if (!succ.isEmpty()) {
            // compare (date au plus tard - poids arc) de tous ses succ et garde le min
            for (Sommet s : succ) {
                Arc a = g.getArc(this, s);
                if (a != null) {
                    int date = s.getDatePlusTard() - a.getPoids();
                    res = Integer.min(date, res);
                }
            }
        }
        this.datePlusTard = res;
    }

    public RoundBtn getButton() {
        return b;
    }

    public void setButton(RoundBtn b) {
        this.b = b;
    }

    public void setName(String name) {
        this.name = name;
        b.setText(name);
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void addSucc(Sommet child) {
        successeurs.add(child);
    }

    public ArrayList<Sommet> getSuccesseurs() {
        return successeurs;
    }

    public ArrayList<Sommet> getPredecesseurs(Graphe g) {
        ArrayList<Sommet> res = new ArrayList<Sommet>();
        ArrayList<Sommet> sommets = g.getGraphe();
        for (Sommet s : sommets) {
            if (s.getSuccesseurs().contains(this))
                res.add(s);
        }
        return res;
    }

    public void delSucc(Sommet s) {
        successeurs.removeIf(sommet -> sommet == s);
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(int x, int y) {
        pos = new Point(x + b.getWidth() / 2, y + b.getHeight() / 2);
        b.setLocation(x, y);
    }

    public static void setLignePanel(LignePanel l) {
        lignePanel = l;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Sommet s = (Sommet) o;
        return (this.pos.equals(s.pos) && this.name.equals(s.name));
    }

    public int getDatePlusTot() {
        return datePlusTot;
    }

    public void setDatePlusTot(int datePlusTot) {
        this.datePlusTot = datePlusTot;
    }

    public int getDatePlusTard() {
        return datePlusTard;
    }

    public void setDatePlusTard(int datePlusTard) {
        this.datePlusTard = datePlusTard;
    }

    @Override
    public String toString() {
        return "Sommet[nom=" + name + ", desc=" + desc + "]";
    }
}
