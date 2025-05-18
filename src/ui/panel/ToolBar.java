package panel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import components.ToolBarButton;
import model.Graphe;
import view.LoadFileChooser;
import view.SaveFileChooser;

import java.awt.Dimension;
import java.awt.Color;

public class ToolBar extends JPanel {

    private ToolBarButton[] comps;

    public ToolBar(Graphe g) {
        comps = new ToolBarButton[4];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(border, "Outils"),
                BorderFactory.createEmptyBorder(20, 5, 5, 11)));

        ToolBarButton sourisbtn = new ToolBarButton("souris.png");
        add(sourisbtn);
        comps[0] = sourisbtn;
        add(Box.createRigidArea(new Dimension(0, 10)));

        ToolBarButton addbtn = new ToolBarButton("AddBtn.png");
        add(addbtn);
        comps[1] = addbtn;
        add(Box.createRigidArea(new Dimension(0, 10)));

        ToolBarButton arrbtn = new ToolBarButton("Graphe_aretes.png");
        add(arrbtn);
        comps[2] = arrbtn;
        add(Box.createRigidArea(new Dimension(0, 10)));

        ToolBarButton suppbtn = new ToolBarButton("Graphe_supp.png");
        add(suppbtn);
        comps[3] = suppbtn;
        add(Box.createRigidArea(new Dimension(0, 10)));

        JButton save = new ToolBarButton("dl_graphe.png");
        save.addActionListener(e -> new SaveFileChooser(g));
        add(save);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JButton load = new ToolBarButton("up_graphe.png");
        load.addActionListener(e -> new LoadFileChooser(g));
        add(load);
    }

    public ToolBarButton getButton(int index) {
        return comps[index];
    }

}