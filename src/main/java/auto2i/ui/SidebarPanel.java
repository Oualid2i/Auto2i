package auto2i.ui;

import auto2i.ui.components.RoundedButton;
import static auto2i.ui.UIConstants.*;
import static auto2i.ui.UIIcons.*;

import javax.swing.*;
import java.awt.*;




public class SidebarPanel extends JPanel {

    private RoundedButton btnVehicules, btnVehListe, btnVehNew;
    private RoundedButton btnClients, btnCliList, btnCliNew;
    private RoundedButton btnInter, btnRep, btnEnt;


    public interface NavigationListener {
        void goHome();
        void goVehiculesList();
        void goVehiculesNew();
        void goClientsList();
        void goClientsNew();
        void goReparations();
        void goEntretiens();
    }

    public SidebarPanel(NavigationListener nav) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        setBackground(new Color(235, 235, 235));
        setPreferredSize(new Dimension(240, 100));

        JLabel brand = new JLabel("Auto 2i");
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 44f));
        brand.setForeground(new Color(45, 85, 140));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(brand);
        add(Box.createVerticalStrut(20));

        // Création des boutons
        //RoundedButton btnAccueil = createMainButton("  Accueil", null, true);

        RoundedButton btnVehicules = createMainButton("  Véhicules", "car.png", false);
        RoundedButton btnVehListe  = createSubButton("Liste");
        RoundedButton btnVehNew    = createSubButton("Nouveau");

        RoundedButton btnInter = createMainButton("  Interventions", "intervention.png", false);
        RoundedButton btnRep   = createSubButton("Réparation");
        RoundedButton btnEnt   = createSubButton("Entretien");

        RoundedButton btnClients = createMainButton("  Clients", "service-client.png", false);
        RoundedButton btnCliList = createSubButton("Liste");
        RoundedButton btnCliNew  = createSubButton("Nouveau");

        // Ajout des sections
        //add(createSection(btnAccueil));
        add(createSection(btnVehicules, btnVehListe, btnVehNew));
        add(createSection(btnInter, btnRep, btnEnt));
        add(createSection(btnClients, btnCliList, btnCliNew));

        // === LISTENERS (navigation) ===
        //btnAccueil.addActionListener(e -> nav.goHome());
        btnVehicules.addActionListener(e -> nav.goVehiculesList());
        btnClients.addActionListener(e -> nav.goClientsList());
        btnInter.addActionListener(e -> nav.goReparations()); // ou une page interventions

        btnVehListe.addActionListener(e -> nav.goVehiculesList());
        btnVehNew.addActionListener(e -> nav.goVehiculesNew());

        btnRep.addActionListener(e -> nav.goReparations());
        btnEnt.addActionListener(e -> nav.goEntretiens());

        btnCliList.addActionListener(e -> nav.goClientsList());
        btnCliNew.addActionListener(e -> nav.goClientsNew());

    }

    private JPanel section(String title, String sub1, Runnable a1, String sub2, Runnable a2) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(235, 235, 235));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton main = makeMainButton(title);
        p.add(main);
        p.add(Box.createVerticalStrut(8));

        RoundedButton b1 = makeSubButton(sub1);
        b1.addActionListener(e -> a1.run());
        p.add(b1);

        RoundedButton b2 = makeSubButton(sub2);
        b2.addActionListener(e -> a2.run());
        p.add(b2);

        return p;
    }

    private RoundedButton makeMainButton(String text) {
        RoundedButton b = new RoundedButton(text,
                new Color(215, 215, 215),
                new Color(45, 85, 140),
                18
        );
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private RoundedButton makeSubButton(String text) {
        RoundedButton b = new RoundedButton(text,
                new Color(230, 230, 230),
                new Color(45, 85, 140),
                16
        );
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 13f));
        return b;
    }

    private JPanel createSection(
            RoundedButton mainButton,
            RoundedButton... subButtons
    ) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);

        Dimension btnSize = new Dimension(180, 40);

        mainButton.setMaximumSize(btnSize);
        mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(mainButton);
        section.add(Box.createVerticalStrut(8));

        for (RoundedButton b : subButtons) {
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(b);
            section.add(Box.createVerticalStrut(6));
        }

        section.add(Box.createVerticalStrut(16));
        return section;
    }

    private RoundedButton createMainButton(String text, String iconName, boolean active) {
        Color bg = active ? ORANGE_MAIN : new Color(215, 215, 215);
        Color fg = active ? Color.WHITE : BLUE_MAIN;

        RoundedButton b = new RoundedButton(text, bg, fg, 18);

        if (iconName != null) {
            b.setIcon(load(iconName, 35));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setIconTextGap(10);
        }

        return b;
    }

    private RoundedButton createSubButton(String text) {
        RoundedButton b = new RoundedButton(
                "  " + text,
                new Color(230, 230, 230),
                BLUE_MAIN,
                16
        );
        return b;
    }

    public void setActive(String key) {
        // reset
        btnVehicules.setColors(new Color(215,215,215), BLUE_MAIN);
        btnVehListe.setColors(new Color(230,230,230), BLUE_MAIN);
        btnVehNew.setColors(new Color(230,230,230), BLUE_MAIN);

        btnClients.setColors(new Color(215,215,215), BLUE_MAIN);
        btnCliList.setColors(new Color(230,230,230), BLUE_MAIN);
        btnCliNew.setColors(new Color(230,230,230), BLUE_MAIN);

        btnInter.setColors(new Color(215,215,215), BLUE_MAIN);
        btnRep.setColors(new Color(230,230,230), BLUE_MAIN);
        btnEnt.setColors(new Color(230,230,230), BLUE_MAIN);

        // active main + active sub
        if ("VEH_LIST".equals(key)) {
            btnVehicules.setColors(ORANGE_MAIN, Color.WHITE);
            btnVehListe.setColors(new Color(255, 219, 102), new Color(200, 120, 0)); // jaune
        }
        if ("VEH_NEW".equals(key)) {
            btnVehicules.setColors(ORANGE_MAIN, Color.WHITE);
            btnVehNew.setColors(new Color(255, 219, 102), new Color(200, 120, 0));
        }
        if ("CLI_LIST".equals(key)) {
            btnClients.setColors(ORANGE_MAIN, Color.WHITE);
            btnCliList.setColors(new Color(255, 219, 102), new Color(200, 120, 0));
        }
        if ("CLI_NEW".equals(key)) {
            btnClients.setColors(ORANGE_MAIN, Color.WHITE);
            btnCliNew.setColors(new Color(255, 219, 102), new Color(200, 120, 0));
        }
    }



}
