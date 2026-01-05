package auto2i.ui;

import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import java.awt.*;

import static auto2i.ui.UIConstants.*;

public class VehiculeListPanel extends JPanel {

    private final JTextField searchField = new JTextField();

    public VehiculeListPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        add(buildTopBar(() -> JOptionPane.showMessageDialog(this, "Retour")), BorderLayout.NORTH);
        // ===== Contenu =====
        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(center, BorderLayout.CENTER);

        // Barre de recherche
        center.add(buildSearchBar(), BorderLayout.NORTH);

        // Grande carte contenant la liste
        CardPanel listCard = new CardPanel(22, BORDER);
        listCard.add(buildList(), BorderLayout.CENTER);
        center.add(listCard, BorderLayout.CENTER);
    }

    private JPanel buildTopBar(Runnable onBack) {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        RoundedButton back = new RoundedButton("←", ORANGE_MAIN, Color.WHITE, 16);
        back.setHorizontalAlignment(SwingConstants.CENTER);
        back.setPreferredSize(new Dimension(50, 40));
        back.addActionListener(e -> onBack.run());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(back);

        JLabel title = new JLabel("Liste véhicule", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }


    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Champ de recherche (grand)
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.setFont(searchField.getFont().deriveFont(14f));
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));


        c.gridx = 0;
        c.weightx = 1.0;
        p.add(searchField, c);

        // Bouton Chercher
        RoundedButton btnSearch = new RoundedButton("Chercher", ORANGE_MAIN, Color.WHITE, 18);
        btnSearch.setPreferredSize(new Dimension(160, 40));
        btnSearch.addActionListener(e -> {
            // Pour l'instant, on ne filtre pas encore (UI d'abord)
            JOptionPane.showMessageDialog(this, "Recherche: " + searchField.getText());
        });

        c.gridx = 1;
        c.weightx = 0;
        p.add(btnSearch, c);

        // Bouton +
        RoundedButton btnAdd = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 18);
        btnAdd.setPreferredSize(new Dimension(55, 40));
        btnAdd.setHorizontalAlignment(SwingConstants.CENTER);
        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ajout véhicule (à faire)"));

        c.gridx = 2;
        p.add(btnAdd, c);

        return p;
    }

    private JComponent buildList() {
        // Liste verticale de "lignes" (cards)
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(vehicleRow("AJ-433-BD", "Peugeot", "206", "24 000 km", "Essence"));
        list.add(Box.createVerticalStrut(10));
        list.add(vehicleRow("GN-432-CL", "Citroen", "C5", "80 000 km", "Diesel"));
        list.add(Box.createVerticalStrut(10));
        list.add(vehicleRow("AA-123-BB", "Renault", "Clio", "45 200 km", "Essence"));

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(null);
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);

        return sp;
    }

    private JPanel vehicleRow(String immat, String marque, String modele, String km, String energie) {

        CardPanel row = new CardPanel(18, BORDER);
        row.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        row.setLayout(new BorderLayout());

        // --- contenu texte (colonnes) ---
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 18, 0, 18);
        c.gridy = 0;

        // Colonne 0 : immat
        c.gridx = 0;
        c.weightx = 0.22;
        content.add(new JLabel(immat), c);

        // Colonne 1 : marque
        c.gridx = 1;
        c.weightx = 0.22;
        content.add(new JLabel(marque), c);

        // Colonne 2 : modèle
        c.gridx = 2;
        c.weightx = 0.18;
        content.add(new JLabel(modele), c);

        // Colonne 3 : km
        c.gridx = 3;
        c.weightx = 0.22;
        content.add(new JLabel(km), c);

        // Colonne 4 : énergie
        c.gridx = 4;
        c.weightx = 0.16;
        content.add(new JLabel(energie), c);

        // --- actions à droite ---
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        RoundedButton btnClient = new RoundedButton("Client", ORANGE_MAIN, Color.WHITE, 16);
        RoundedButton btnVoir   = new RoundedButton("Voir", ORANGE_MAIN, Color.WHITE, 16);
        RoundedButton btnModif  = new RoundedButton("Modifier", ORANGE_MAIN, Color.WHITE, 16);

        btnClient.setPreferredSize(new Dimension(90, 32));
        btnVoir.setPreferredSize(new Dimension(80, 32));
        btnModif.setPreferredSize(new Dimension(100, 32));

        actions.add(btnClient);
        actions.add(btnVoir);
        actions.add(btnModif);

        row.add(content, BorderLayout.CENTER);
        row.add(actions, BorderLayout.EAST);

        // Hauteur fixe propre
        row.setPreferredSize(new Dimension(1000, 85));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        return row;
    }

}
