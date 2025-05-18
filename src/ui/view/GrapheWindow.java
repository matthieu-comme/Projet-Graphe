package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.Graphe;
import model.Sommet;
import panel.AbstractInfoPanel;
import panel.ToolBar;
import components.RoundBtn;

public class GrapheWindow extends JPanel {
    private ToolBar TB;
    public AbstractInfoPanel infos;
    private Graphe g;
    private JPanel container;

    public GrapheWindow() {
        container = new JPanel();
        container.setLayout(null);
        g = new Graphe(container);
        TB = new ToolBar(g);
        this.setLayout(new BorderLayout());
        this.add(TB, BorderLayout.WEST);
        add(container);
        tbAddAction();
        keyShortCut();
        zoom();
    }

    private void tbAddAction() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            JButton btn = TB.getButton(index);
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // reset ancien mode
                    int mode = Graphe.getMode();
                    TB.getButton(mode).setBorder(null);
                    g.setMode(index);
                    btn.setBorder(BorderFactory.createLineBorder(Color.RED)); // le mode activé est en rouge
                }
            });
        }
    }

    public void keyShortCut() {
        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        for (int i = 0; i <= 3; i++) {
            final int j = i;
            im.put(KeyStroke.getKeyStroke(String.valueOf(j + 1)), "mode" + (j));
            am.put("mode" + (j), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int mode = Graphe.getMode();
                    TB.getButton(mode).setBorder(null);
                    g.setMode(j);
                    TB.getButton(j).setBorder(BorderFactory.createLineBorder(Color.RED)); // le mode activé est en rouge
                }
            });
        }
    }

    private void zoom() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == -1) {
                    for (Sommet s : g.getGraphe()) {
                        s.getButton().zoom(1.15, e.getPoint());
                        g.getLignePanel().repaint();
                    }
                    RoundBtn.setZoom(1.15);
                } else {
                    for (Sommet s : g.getGraphe()) {
                        s.getButton().zoom(0.85, e.getPoint());
                        g.getLignePanel().repaint();
                    }
                    RoundBtn.setZoom(0.85);

                }
            }
        });
    }

    public void setG(Graphe g) {
        this.g = g;
    }

    public void setContainer(JPanel container) {
        this.remove(this.container);
        this.container = container;
        this.add(container);
        container.setLayout(null);
    }
}