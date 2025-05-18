package panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

import components.RoundBtn;
import controller.LignePanelListener;
import model.Arc;
import model.Graphe;
import model.Sommet;

public class LignePanel extends JPanel {
    private LignePanelListener listener;
    private int mode = 1;
    private ArrayList<Arc> lineHitBox;
    private Point start = null, end = null;
    private ArrayList<Sommet> selectedSommet;

    public LignePanel() {
        lineHitBox = new ArrayList<Arc>();
        selectedSommet = new ArrayList<Sommet>();
        setOpaque(false);
        setLayout(null);
        selectArea();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Arc a;
                if (listener != null) {
                    if (mode == 0) {
                        if ((a = isOnArete(e.getPoint())) != null) {
                            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
                                listener.onClickShowArcInfo(a);
                        }
                    } else if (mode == 1) {
                        listener.onClickAddSommet(e.getPoint());
                    } else if (mode == 3) {

                        if ((a = isOnArete(e.getPoint())) != null) {
                            listener.onClickDelArc(a);
                        }
                    }
                }
            }
        });
    }

    public void setListener(LignePanelListener l) {
        listener = l;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        for (Arc a : lineHitBox) {
            Point p1 = a.getP1();
            Point p2 = a.getP2();
            int poids = a.getPoids();
            int midx = (p1.x + p2.x) / 2;
            int midy = (p1.y + p2.y) / 2;

            Color c1 = a.getS1().getButton().getBackground();
            Color c2 = a.getS2().getButton().getBackground();

            if (c1.equals(Color.RED) && c2.equals(Color.RED)) {
                g.setColor(c2);
            }
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            drawArrowHead(g2, p1.x, p1.y, p2.x, p2.y, a.getS2());
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK);
            g2.drawString(String.valueOf(poids), midx, midy - 10);
        }
        /**
         * Dessiner la zone de séléction
         */
        if (start != null && end != null) {
            int x = Math.min(start.x, end.x);
            int y = Math.min(start.y, end.y);
            int width = Math.abs(start.x - end.x);
            int height = Math.abs(start.y - end.y);
            g.setColor(new Color(15, 137, 197, 100));
            g.drawRect(x, y, width, height);
            g.setColor(new Color(107, 183, 221, 100));
            g.fillRect(x, y, width, height);
        }
    }

    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2, Sommet s) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLength = 15;
        /*
         * Calcul de vecteur directeur du segment [ParentEnfant] et le normaliser
         */
        double lenght = Math.sqrt((double) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
        double ux = (double) (x2 - x1) / lenght;
        double uy = (double) (y2 - y1) / lenght;

        // Calcul des deux points du triangle (tête de flèche)
        int x3 = (int) ((x2 - (80 * s.getButton().getZoom()) / 2 * ux) - arrowLength * Math.cos(angle - Math.PI / 6));
        int y3 = (int) ((y2 - (80 * s.getButton().getZoom()) / 2 * uy) - arrowLength * Math.sin(angle - Math.PI / 6));
        int x4 = (int) ((x2 - (80 * s.getButton().getZoom()) / 2 * ux) - arrowLength * Math.cos(angle + Math.PI / 6));
        int y4 = (int) ((y2 - (80 * s.getButton().getZoom()) / 2 * uy) - arrowLength * Math.sin(angle + Math.PI / 6));

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint((int) (x2 - (80 * s.getButton().getZoom() - 10) / 2 * ux),
                (int) (y2 - (80 * s.getButton().getZoom() - 10) / 2 * uy));
        arrowHead.addPoint(x3, y3);
        arrowHead.addPoint(x4, y4);

        g2.fill(arrowHead);
    }

    public void setMode(int i) {
        mode = i;
    }

    public void addHitBox(Sommet parent, Sommet enfant, Graphe g) {
        lineHitBox.add(new Arc(parent, enfant, g));
    }

    public void delHitBox(Arc a) {
        lineHitBox.removeIf(arc -> arc == a);
    }

    public void delHitBox(String s) {
        lineHitBox.removeIf(arc -> arc.getS1().getName().equals(s) || arc.getS2().getName().equals(s));
    }

    private Arc isOnArete(Point p) {
        int precision = 15;
        for (Arc a : lineHitBox) {
            if (distancePointSegment(p, a.getP1(), a.getP2()) <= precision)
                return a;
        }
        return null;
    }

    public static double distancePointSegment(Point p, Point a, Point b) {
        double px = p.x, py = p.y;
        double ax = a.x, ay = a.y;
        double bx = b.x, by = b.y;

        double abx = bx - ax;
        double aby = by - ay;

        double apx = px - ax;
        double apy = py - ay;

        double normeCarre = abx * abx + aby * aby;

        double prodSca = apx * abx + apy * aby;
        double t = normeCarre == 0 ? 0 : prodSca / normeCarre;

        // récupérer le point de l'arete le plus proche du clic
        t = Math.max(0, Math.min(1, t));

        double closestX = ax + t * abx;
        double closestY = ay + t * aby; // Le point le plus proche du clic sur l'arete

        double dx = px - closestX;
        double dy = py - closestY;

        return Math.sqrt(dx * dx + dy * dy); // La distance entre le point sur l'arete et le clic
    }

    private void selectArea() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (mode == 0 && e.getClickCount() <= 1) {
                    start = e.getPoint();
                    for (Sommet s : selectedSommet) {
                        s.getButton().setBorder(false);
                    }
                    selectedSommet.clear();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                start = null;
                end = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mode == 0) {
                    end = e.getPoint();
                    btnInArea();
                    /**
                     * Afficher les sommets séléctionnés
                     */
                    for (Sommet s : selectedSommet) {
                        RoundBtn btn = s.getButton();
                        btn.setBorder(true);
                    }
                    repaint(); // redessiner à chaque déplacement
                }
            }
        });

    }

    public void btnInArea() {
        ArrayList<Sommet> graphe = listener.getGraphe();
        int xMin = Math.min(start.x, end.x);
        int xMax = Math.max(start.x, end.x);
        int yMin = Math.min(start.y, end.y);
        int yMax = Math.max(start.y, end.y);
        for (Sommet s : graphe) {
            if (xMin < s.getPos().x && s.getPos().x < xMax && yMin < s.getPos().y && s.getPos().y < yMax) {
                if (!selectedSommet.contains(s))
                    selectedSommet.add(s);
            } else {
                selectedSommet.remove(s);
                s.getButton().setBorder(false);
            }
        }
    }

    public ArrayList<Arc> getArcs() {
        return lineHitBox;
    }

    public void setArcs(ArrayList<Arc> arcs) {
        lineHitBox = arcs;
    }

    public Arc getArc(Sommet s1, Sommet s2) { // retourne l'arc qui va de s1 à s2, null s'il n'existe pas
        for (Arc a : lineHitBox) {
            if (a.getS1().equals(s1) && a.getS2().equals(s2))
                return a;
        }
        return null;

    }

    public ArrayList<Sommet> getSelectedSommet() {
        return selectedSommet;
    }
}
