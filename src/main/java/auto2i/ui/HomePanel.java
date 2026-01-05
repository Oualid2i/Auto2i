package auto2i.ui;

import auto2i.ui.components.CardPanel;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Bienvenue", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 42f));
        title.setForeground(new Color(45, 85, 140));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        add(center, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(16, 16, 16, 16);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        // Bloc haut : Derniers véhicules (grand)
        JPanel lastVehicles = makeCard("Derniers véhicules enregistrés");
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        c.weighty = 0.55;
        center.add(lastVehicles, c);

        // Bas gauche : Dernières réparations
        JPanel lastRepairs = makeCard("Dernières réparations");
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.45;
        center.add(lastRepairs, c);

        // Bas droite : Entretiens à venir
        JPanel upcoming = makeCard("Entretiens à venir");
        c.gridx = 1; c.gridy = 1;
        center.add(upcoming, c);

        // Contenu “placeholder” pour l’instant
        lastVehicles.add(new JScrollPane(makeSimpleList(
                "AJ-433-BD — Peugeot 206 — 24 000 km",
                "GN-432-CL — Citroen C5 — 80 000 km",
                "AA-123-BB — Renault Clio — 45 200 km"
        )), BorderLayout.CENTER);

        lastRepairs.add(new JScrollPane(makeSimpleList(
                "12/03/2025 — AJ-433-BD — Plaquettes AV",
                "01/02/2025 — GN-432-CL — Embrayage"
        )), BorderLayout.CENTER);

        upcoming.add(new JScrollPane(makeSimpleList(
                "GN-432-CL — Vidange — dans 20 jours",
                "AJ-433-BD — Révision 30 000 km — reste 2 000 km"
        )), BorderLayout.CENTER);
    }

    private JPanel makeCard(String header) {
        CardPanel p = new CardPanel(22, new Color(210, 210, 210));

        JLabel h = new JLabel(header);
        h.setFont(h.getFont().deriveFont(Font.PLAIN, 16f));
        h.setForeground(new Color(45, 85, 140));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(h, BorderLayout.WEST);

        p.add(top, BorderLayout.NORTH);
        return p;
    }


    private JList<String> makeSimpleList(String... items) {
        JList<String> list = new JList<>(items);
        list.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        list.setFont(list.getFont().deriveFont(14f));
        list.setSelectionBackground(new Color(255, 153, 51)); // orange
        list.setSelectionForeground(Color.WHITE);
        return list;
    }
}
