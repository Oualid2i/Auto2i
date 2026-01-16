package auto2i.ui.panels;

import auto2i.ui.MainFrame;
import auto2i.ui.components.RoundedButton;
import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

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
        void goInterventions();
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

        btnVehicules = createMainButton("  Véhicules", "car.png", false);
        btnVehListe  = createSubButton("Liste");
        btnVehNew    = createSubButton("Nouveau");

        btnInter = createMainButton("  Interventions", "intervention.png", false);
        btnRep   = createSubButton("Réparation");
        btnEnt   = createSubButton("Entretien");

        btnClients = createMainButton("  Clients", "service-client.png", false);
        btnCliList = createSubButton("Liste");
        btnCliNew  = createSubButton("Nouveau");

        add(createSection(btnVehicules, btnVehListe, btnVehNew));
        add(createSection(btnInter, btnRep, btnEnt));
        add(createSection(btnClients, btnCliList, btnCliNew));

        // LISTENERS
        btnVehicules.addActionListener(e -> nav.goVehiculesList());
        btnVehListe.addActionListener(e -> nav.goVehiculesList());
        btnVehNew.addActionListener(e -> nav.goVehiculesNew());

        btnInter.addActionListener(e ->  nav.goInterventions());
        btnRep.addActionListener(e -> nav.goReparations());
        btnEnt.addActionListener(e -> nav.goEntretiens());

        btnClients.addActionListener(e -> nav.goClientsList());
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


    private void setMainActive(RoundedButton b) {
        b.setColors(ORANGE_MAIN, Color.WHITE);
    }

    private void setMainInactive(RoundedButton b) {
        b.setColors(BTN_MAIN_INACTIVE, BLUE_MAIN);
    }

    private void setSubActive(RoundedButton b) {
        b.setColors(YELLOW_ACTIVE, BLUE_MAIN);
    }

    private void setSubInactive(RoundedButton b) {
        b.setColors(BTN_SUB_INACTIVE, BLUE_MAIN);
    }



    public void setActive(String pageKey) {
        // reset tous les styles en "inactif"
        setMainInactive(btnVehicules);
        setSubInactive(btnVehListe);
        setSubInactive(btnVehNew);

        setMainInactive(btnClients);
        setSubInactive(btnCliList);
        setSubInactive(btnCliNew);

        setMainInactive(btnInter);
        setSubInactive(btnRep);
        setSubInactive(btnEnt);

        // activer selon la page
        switch (pageKey) {
            case MainFrame.PAGE_VEHICULE_LIST:
                setMainActive(btnVehicules);
                setSubActive(btnVehListe);
                break;
            case MainFrame.PAGE_VEHICULE_NEW:
                setMainActive(btnVehicules);
                setSubActive(btnVehNew);
                break;
            case MainFrame.PAGE_CLIENT_LIST:
                setMainActive(btnClients);
                setSubActive(btnCliList);
                break;
            case MainFrame.PAGE_CLIENT_NEW:
                setMainActive(btnClients);
                setSubActive(btnCliNew);
                break;
            case MainFrame.PAGE_INTERVENTIONS:
                setMainActive(btnInter);
                break;
            case MainFrame.PAGE_REPARATION:
                setMainActive(btnInter);
                setSubActive(btnRep);
                break;
            case MainFrame.PAGE_ENTRETIEN:
                setMainActive(btnInter);
                setSubActive(btnEnt);
                break;
            case MainFrame.PAGE_HOME:
            default:
                break;
        }

        repaint();
        revalidate();
    }




}
