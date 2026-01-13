package auto2i.ui.panels;

import javax.swing.*;
import java.awt.*;
import static auto2i.ui.constants.UIConstants.*;

public class PlaceholderPanel extends JPanel {

    public PlaceholderPanel(String title) {
        setLayout(new GridBagLayout());
        setBackground(BG_APP);

        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 32f));
        label.setForeground(BLUE_MAIN);

        add(label);
    }
}
