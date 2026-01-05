package auto2i.ui.components;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    private final int radius;
    private final Color borderColor;

    public CardPanel(int radius, Color borderColor) {
        this.radius = radius;
        this.borderColor = borderColor;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // fond blanc
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        // bordure
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);

        g2.dispose();
    }
}
