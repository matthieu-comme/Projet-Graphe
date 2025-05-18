package panel;

import javax.swing.*;
import controller.ValiderSommetKeyAdapter;
import model.Graphe;
import model.Sommet;
import model.SommetExistantException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

public class InfoSommetPanel extends AbstractInfoPanel {
    private JLabel nameLabel, descLabel;
    private JTextField nameTextField;
    private JTextArea descTextArea;

    public InfoSommetPanel(Sommet s, Graphe g) {
        super(g);
        unBindShortKey(g.getP().getParent());

        nameLabel = new JLabel("Nom : ");
        nameTextField = new JTextField(s.getName());
        nameTextField.setPreferredSize(new Dimension(150, 20));

        JPanel namesPanel = new JPanel();
        namesPanel.add(nameLabel);
        namesPanel.add(nameTextField);

        descLabel = new JLabel("Description : ");
        descTextArea = new JTextArea(s.getDesc());
        descTextArea.setPreferredSize(new Dimension(190, 100));

        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BorderLayout());
        descPanel.add(descLabel, BorderLayout.LINE_START);
        descPanel.add(descTextArea, BorderLayout.SOUTH);

        submitbtn.addActionListener(e -> updateSommet(s, g));

        this.add(namesPanel);
        this.add(descPanel);
        this.add(submitbtn);
        this.setSize(240, 200);
        KeyAdapter validerSommet = new ValiderSommetKeyAdapter(nameTextField,
                descTextArea, s, g);
        nameTextField.addKeyListener(validerSommet);
        descTextArea.addKeyListener(validerSommet);
    }

    public JTextField getTextField() {
        return nameTextField;
    }

    private void updateSommet(Sommet s, Graphe g) {
        try {
            String nomEntre = nameTextField.getText().trim();
            String descEntre = descTextArea.getText().trim();
            if (!Graphe.nomDisponible(nomEntre, s, g))
                throw new SommetExistantException(nomEntre);
            s.setName(nomEntre);
            s.setDesc(descEntre);
            bindShortKey(g.getP().getParent());
            this.setVisible(false);
            g.getP().repaint();
        } catch (SommetExistantException ex) {
            JOptionPane.showMessageDialog(g.getP(), ex.getMessage(), "Nom déjà utilisé",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
