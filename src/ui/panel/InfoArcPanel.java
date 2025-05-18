package panel;

import java.awt.*;
import javax.swing.*;

import model.Arc;
import model.ArcException;
import model.Graphe;

import java.awt.event.*;

public class InfoArcPanel extends AbstractInfoPanel {
    private JLabel parentName, childName, poidsLabel;
    private JTextField aretePoids;

    public InfoArcPanel(Arc a, Graphe g) {
        super(g);
        unBindShortKey(g.getP().getParent());
        JPanel areteInfo = new JPanel();
        areteInfo.setLayout(new BoxLayout(areteInfo, BoxLayout.Y_AXIS));

        parentName = new JLabel("Parent : " + a.getS1().getName());
        childName = new JLabel("Enfant : " + a.getS2().getName());
        areteInfo.add(parentName);
        areteInfo.add(childName);

        JPanel poidsInfo = new JPanel();
        poidsLabel = new JLabel("Poids : ");
        aretePoids = new JTextField(a.getPoids() + "");
        aretePoids.setPreferredSize(new Dimension(30, 25));

        poidsInfo.add(poidsLabel);
        poidsInfo.add(aretePoids);
        poidsInfo.add(submitbtn);

        submitbtn.addActionListener(e -> changerPoids(a, g.getP()));

        aretePoids.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10)
                    changerPoids(a, g.getP());
            }
        });
        this.add(areteInfo);
        this.add(poidsInfo);
        this.setSize(220, 110);

    }

    public JTextField getTextField() {
        return aretePoids;
    }

    private void changerPoids(Arc a, JPanel p) {
        try {
            int newPoids = Integer.parseInt(aretePoids.getText());
            try {
                a.setPoids(newPoids);
            } catch (ArcException e) {
                JOptionPane.showMessageDialog(null, "Exception : " + e.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            graphe.updateDates();
            this.setVisible(false);
            p.repaint();
            bindShortKey(p.getParent());
        } catch (NumberFormatException nfe) {
            JLabel errorLabel = new JLabel("Veuillez rentrer uniquement des nombres");
            errorLabel.setFont(errorLabel.getFont().deriveFont(10f));
            errorLabel.setForeground(Color.RED);
            this.add(errorLabel);
            p.revalidate();
        }
    }
}
