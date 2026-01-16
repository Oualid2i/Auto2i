package auto2i.ui;

import javax.swing.*;
import java.awt.*;

public class AppHeader extends JPanel {

    public AppHeader() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setBackground(new Color(220, 220, 220));

        JLabel title = new JLabel("Auto 2i - Gestion des automobiles", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 55);
    }
}
