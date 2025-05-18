package view;

import javax.swing.*;
import java.awt.*;
import model.Graphe;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private GrapheWindow mainWindow;
    private JPanel container;

    public MainFrame() {
        super("GraphiX");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        StartScreen start = new StartScreen(this);
        mainWindow = new GrapheWindow();
        container.add(start, "start");
        container.add(mainWindow, "main");
        mainWindow.keyShortCut();

        add(container);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }

    public void showMainWindow() {
        cardLayout.show(container, "main");
    }

    public void showMainWindow(Graphe g) {
        cardLayout.show(container, "main");
        mainWindow.setG(g);
        mainWindow.setContainer(g.getP());
    }
}
