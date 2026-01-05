package auto2i.ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private final int radius;
    private Color bg;
    private Color fg;

    public RoundedButton(String text, Color bg, Color fg, int radius) {
        super(text);
        this.radius = radius;
        this.bg = bg;
        this.fg = fg;

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(fg);
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(10, 14, 10, 14));
    }

    public void setColors(Color bg, Color fg) {
        this.bg = bg;
        this.fg = fg;
        setForeground(fg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // léger “hover” automatique via le modèle du bouton
        Color drawBg = bg;
        if (getModel().isRollover()) {
            drawBg = bg.darker();
        }

        g2.setColor(drawBg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }
}
