package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

import controller.ShowSommetInfoAdapter;

import model.Graphe;
import model.Sommet;

public class RoundBtn extends JButton {
    private Sommet s;
    boolean bordure = false;
    private int epaisseur = 2;
    private static int n = 80;
    private static double zoom = 1;

    public RoundBtn(Sommet s, Graphe g) {
        super(s.getName());
        this.s = s;
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBackground(Color.lightGray);
        setForeground(Color.BLACK);
        Point pos = s.getPos();
        setBounds(pos.x - n / 2, pos.y - n / 2, (int) (n * zoom), (int) (n * zoom));
        setFont(getFont().deriveFont((float) (20 * zoom / 1.5)));
        ShowSommetInfoAdapter adapter = new ShowSommetInfoAdapter(s, g);
        addMouseListener(adapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Lisse les bords
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Couleur du fond
        g2.setColor(getBackground());
        g2.fillOval(5, 5, (int) (getWidth() - 10), (int) (getHeight() - 10));

        g2.setColor(Color.black);
        g2.drawOval(5, 5, (int) (getWidth() - 12), (int) (getHeight() - 12));

        if (bordure) {
            g2.setColor(new Color(97, 0, 50));
            g2.setStroke(new BasicStroke(epaisseur));
            g2.drawRect(epaisseur / 2, epaisseur / 2, getWidth() - epaisseur, getHeight() - epaisseur);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        int size = Math.max(super.getPreferredSize().width, super.getPreferredSize().height);
        return new Dimension(size, size); // Carré, donc cercle parfait
    }

    @Override
    public boolean contains(int x, int y) {
        Ellipse2D circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return circle.contains(x, y);
    }

    public void setBorder(boolean b) {
        bordure = b;
    }

    public void zoom(double i, Point p) {
        int centreX = p.x;
        int centreY = p.y;

        int posX = (int) (s.getPos().x - (n * zoom) / 2);
        int posY = (int) (s.getPos().y - (n * zoom) / 2);

        int distanceX = centreX - posX;
        int distanceY = centreY - posY;

        int newDistanceX = (int) (distanceX * i);
        int newDistanceY = (int) (distanceY * i);
        if (0.7 < zoom && zoom < 2.5) {
            setBounds((p.x + newDistanceX - n) / 2, (p.y + newDistanceY - n) / 2, (int) (n * zoom),
                    (int) (n * zoom));
            s.setPos(posX + (distanceX - newDistanceX), posY + (distanceY - newDistanceY));
            setFont(getFont().deriveFont((float) (20 * zoom / 1.5)));
            repaint();
        }
    }

    public double getZoom() {
        return zoom;
    }

    public static void setZoom(double z) {
        /**
         * Pour borner le zoom
         */
        zoom = Math.min(2.5, Math.max(0.7, zoom * z));
    }
}
