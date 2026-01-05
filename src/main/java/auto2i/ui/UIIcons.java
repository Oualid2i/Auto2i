package auto2i.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UIIcons {

    public static ImageIcon load(String fileName, int size) {
        URL url = UIIcons.class.getResource("/icons/" + fileName);
        if (url == null) {
            System.err.println("❌ Icône introuvable: /icons/" + fileName);
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private UIIcons() {}
}
