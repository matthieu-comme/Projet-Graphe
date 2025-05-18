package components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ToolBarButton extends JButton {
    public ToolBarButton(String fileName) {
        super(new ImageIcon("src/assets/" + fileName));
        this.setBackground(null);
        this.setBorder(null);
    }
}
