package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Arc;
import model.Graphe;
import model.Sommet;

public class SaveFileChooser extends JFileChooser {
    ArrayList<Sommet> sommets;
    ArrayList<Arc> arcs;

    public SaveFileChooser(Graphe g) {
        this.sommets = g.getGraphe();
        this.arcs = g.getArcs();
        this.setDialogTitle("Sauvegarder le graphe");
        this.setApproveButtonText("Sauvegarder");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers de graphe (*.ser)", "ser");
        this.setFileFilter(filter);

        int userSelection = showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fichier = getSelectedFile();
            if (!fichier.getName().toLowerCase().endsWith(".ser")) {
                fichier = new File(fichier.getAbsolutePath() + ".ser");
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichier))) {
                out.writeObject(sommets); // par exemple
                out.writeObject(arcs);
                JOptionPane.showMessageDialog(null, "Graphe sauvegardé avec succès !");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde du graphe.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
