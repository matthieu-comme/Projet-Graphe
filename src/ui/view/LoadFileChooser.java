package view;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ModeBoutonListener;
import model.Arc;
import model.Graphe;
import model.Sommet;
import components.RoundBtn;

public class LoadFileChooser extends JFileChooser {
    private Graphe graphe;

    public LoadFileChooser(Graphe g) {
        this.graphe = g;
        setDialogTitle("Charger un graphe");

        // Filtre pour fichiers .ser uniquement
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers de graphe (*.ser)", "ser");
        setFileFilter(filter);

        int userSelection = showOpenDialog(null);
        boolean error = false;
        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fichier = getSelectedFile();
            if (fichier.getName().toLowerCase().endsWith(".ser")) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier))) {
                    Object obj1 = in.readObject();
                    Object obj2 = in.readObject();
                    // on vérifie si le fichier est bien valide
                    if (obj1 instanceof ArrayList<?> && obj2 instanceof ArrayList<?>) {
                        ArrayList<?> liste1 = (ArrayList<?>) obj1;
                        ArrayList<?> liste2 = (ArrayList<?>) obj2;
                        if (!liste1.isEmpty() && liste1.get(0) instanceof Sommet && !liste2.isEmpty()
                                && liste2.get(0) instanceof Arc) {
                            @SuppressWarnings("unchecked")
                            ArrayList<Sommet> sommets = (ArrayList<Sommet>) obj1;
                            @SuppressWarnings("unchecked")
                            ArrayList<Arc> arcs = (ArrayList<Arc>) obj2;

                            JPanel panel = graphe.getP();
                            // efface les anciens boutons
                            for (Component c : panel.getComponents()) {
                                if (c instanceof RoundBtn)
                                    panel.remove(c);

                            }
                            // recrée les boutons qui sont transient
                            for (Sommet s : sommets) {
                                String name = s.getName();
                                if (name.equals("Début")) {
                                    graphe.setDebut(s);
                                } else if (name.equals("Fin")) {
                                    graphe.setFin(s);
                                }
                                RoundBtn b = new RoundBtn(s, graphe);
                                s.setButton(b);
                                s.addDragAndDrop(b);
                                b.addActionListener(new ModeBoutonListener(graphe));
                                panel.add(b);
                            }
                            graphe.setGraphe(sommets);
                            graphe.setArcs(arcs);
                            System.out.println(graphe.getGraphe());
                            System.out.println(graphe.getArcs());
                            panel.setComponentZOrder(graphe.getLignePanel(), panel.getComponentCount() - 1);
                            panel.revalidate();
                            panel.repaint();
                            g.updateDates();
                            JOptionPane.showMessageDialog(null, "Graphe chargé avec succès !");
                        } else {// si obj1 et 2 sont pas listes sommet et arc
                            error = true;
                            JOptionPane.showMessageDialog(null, "Le fichier est invalide.", "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // si obj1 et 2 sont pas des listes
                        error = true;
                        JOptionPane.showMessageDialog(null, "Le fichier est invalide.", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else { // si le fichier n'est pas .ser
                error = true;
                JOptionPane.showMessageDialog(null, "Le fichier est invalide.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        /*
         * if (!error)
         * JOptionPane.showMessageDialog(null, "Le fichier est invalide.", "Erreur",
         * JOptionPane.ERROR_MESSAGE);
         * else
         * JOptionPane.showMessageDialog(null, "Graphe chargé avec succès !");
         */
    }

    public Graphe getGraphe() {
        return graphe;
    }

}
