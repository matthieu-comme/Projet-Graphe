package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.ValiderSommetKeyAdapter;
import model.Graphe;
import model.Sommet;

public class AddSommetDialog extends JDialog {
    private JPanel askName;
    private JLabel nameLabel, descLabel;
    private JTextField name;
    private JTextArea desc;

    public AddSommetDialog(Sommet s, Graphe g) {
        super((JFrame) SwingUtilities.getWindowAncestor(g.getP()), "Nouveau Sommet", true);
        askName = new JPanel();
        nameLabel = new JLabel("Nom");
        name = new JTextField();
        descLabel = new JLabel("Description");
        desc = new JTextArea();

        KeyAdapter validerSommet = new ValiderSommetKeyAdapter(name, desc, s, g, this);

        name.addKeyListener(validerSommet);
        name.setPreferredSize(new Dimension(200, 20));
        askName.add(nameLabel);
        askName.add(name);

        desc.setPreferredSize(new Dimension(220, 40));
        desc.addKeyListener(validerSommet);
        askName.add(descLabel);
        askName.add(desc);

        this.setSize(300, 150);
        this.add(askName);
        this.setLocation(new Point(800, 40));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
}
