package panel;

import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import model.Graphe;

public abstract class AbstractInfoPanel extends JPanel {
    protected JButton submitbtn;
    protected Graphe graphe;

    AbstractInfoPanel(Graphe g) {
        this.graphe = g;
        submitbtn = new JButton("Changer");

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 0)));
    }

    public abstract JTextField getTextField();

    public void unBindShortKey(Container panelParent) {
        InputMap im = ((JPanel) panelParent).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (int i = 1; i <= 4; i++) {
            im.put(KeyStroke.getKeyStroke(String.valueOf(i)), "none"); // bloquer raccourcis clavier
        }
    }

    public void bindShortKey(Container panelParent) {
        InputMap im = ((JPanel) panelParent).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (int i = 1; i <= 4; i++) {
            im.put(KeyStroke.getKeyStroke(String.valueOf(i)), "mode" + (i - 1)); // remettre les raccourcis clavier
        }
    }

}
