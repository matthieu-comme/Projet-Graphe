package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Graphe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen extends JPanel {

    public StartScreen(MainFrame frame) {
        setLayout(new GridBagLayout());
        JPanel menu = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(border, "Menu"),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JButton nouveau = new JButton("Nouveau Graphe");
        nouveau.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.showMainWindow();
            }
        });
        nouveau.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton charger = new JButton("Charger");
        charger.setAlignmentX(Component.CENTER_ALIGNMENT);
        charger.addActionListener(e -> {
            LoadFileChooser lFC = new LoadFileChooser(new Graphe(new JPanel()));
            frame.showMainWindow(lFC.getGraphe());
        });

        JButton quitter = new JButton("Quitter");
        quitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quitter.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(nouveau);
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        menu.add(charger);
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        menu.add(quitter);
        menu.add(Box.createRigidArea(new Dimension(0, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(menu, gbc);

    }
}