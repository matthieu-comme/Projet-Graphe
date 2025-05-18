package controller;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import model.Graphe;
import model.Sommet;
import model.SommetExistantException;

import panel.InfoSommetPanel;

public class ValiderSommetKeyAdapter extends KeyAdapter {
    private JTextField name;
    private JTextArea desc;
    private Sommet s;
    private Graphe g;
    private JDialog dialog;
    private boolean newSommet;

    // pour AddSommet
    public ValiderSommetKeyAdapter(JTextField name, JTextArea desc, Sommet s, Graphe g, JDialog dialog) {
        this.name = name;
        this.desc = desc;
        this.s = s;
        this.g = g;
        this.dialog = dialog;
        newSommet = true;
    }

    // pour InfoSommet
    public ValiderSommetKeyAdapter(JTextField name, JTextArea desc, Sommet s, Graphe g) {
        this.name = name;
        this.desc = desc;
        this.s = s;
        this.g = g;
        this.dialog = null;
        newSommet = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            if (desc.hasFocus() && e.isShiftDown()) {
                desc.append("\n");
            } else {
                try {
                    String nomEntre = name.getText().trim();
                    String descEntre = desc.getText().trim();
                    if (nomEntre.length() >= 2) {
                        if (!Graphe.nomDisponible(nomEntre, s, g)) {
                            throw new SommetExistantException(nomEntre);
                        }
                        setNomDesc(s, nomEntre, descEntre, (dialog != null) ? dialog : null, g);
                    }
                } catch (SommetExistantException ex) {
                    JOptionPane.showMessageDialog(g.getP(), ex.getMessage(), "Nom déjà utilisé",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            e.consume();
        }
    }

    private void setNomDesc(Sommet s, String nom, String desc, JDialog dialog, Graphe g) {
        s.setDesc(desc);
        s.setName(nom);
        if (dialog != null)
            dialog.dispose();
        for (Component c : g.getP().getComponents()) {
            if (c instanceof InfoSommetPanel) {
                ((InfoSommetPanel) c).bindShortKey(g.getP().getParent());
                c.setVisible(false);
                g.getP().repaint();
            }
        }
        if (newSommet) {
            g.getGraphe().add(s);
            g.getP().add(s.getButton());
        }
    }
}
