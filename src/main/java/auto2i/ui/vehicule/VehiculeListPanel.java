package auto2i.ui.vehicule;

import auto2i.model.Vehicule;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

import static auto2i.ui.constants.UIConstants.*;

public class VehiculeListPanel extends JPanel {

    private final Runnable onBack;
    private final Runnable onAdd;
    private final Consumer<Vehicule> onView;

    private final JTextField searchField = new JTextField();

    public VehiculeListPanel(Runnable onBack, Runnable onAdd, Consumer<Vehicule> onView) {
        this.onBack = onBack;
        this.onAdd = onAdd;
        this.onView = onView;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ================= TOP BAR =================
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));

        RoundedButton back = new RoundedButton("←", ORANGE_MAIN, Color.WHITE, 16);
        back.setPreferredSize(new Dimension(50, 40));
        back.setHorizontalAlignment(SwingConstants.CENTER);
        back.addActionListener(e -> { if (onBack != null) onBack.run(); });

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

    // ================= CENTER =================
    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(10, 0, 0, 0));

        center.add(buildSearchBar(), BorderLayout.NORTH);

        CardPanel listCard = new CardPanel(22, BORDER);
        listCard.setLayout(new BorderLayout());
        listCard.setBorder(new EmptyBorder(16, 16, 16, 16)); // padding interne comme ClientList
        listCard.add(buildList(), BorderLayout.CENTER);

        center.add(listCard, BorderLayout.CENTER);
        return center;
    }

    // ================= SEARCH BAR (style ClientList) =================
    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 14, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Champ de recherche (grand)
        searchField.setFont(searchField.getFont().deriveFont(16f));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        searchField.setPreferredSize(new Dimension(600, 50));

        c.gridx = 0;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 18);
        p.add(searchField, c);

        // Bouton Chercher (grand)
        RoundedButton btnSearch = new RoundedButton("Chercher", ORANGE_MAIN, Color.WHITE, 20);
        btnSearch.setFont(btnSearch.getFont().deriveFont(Font.BOLD, 20f));
        btnSearch.setPreferredSize(new Dimension(260, 55));
        btnSearch.setMinimumSize(new Dimension(260, 55));
        btnSearch.setMaximumSize(new Dimension(260, 55));
        btnSearch.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Recherche: " + searchField.getText())
        );

        c.gridx = 1;
        c.weightx = 0.0;
        c.insets = new Insets(0, 0, 0, 18);
        p.add(btnSearch, c);

        // Bouton +
        RoundedButton btnAdd = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 22);
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 22f));
        btnAdd.setPreferredSize(new Dimension(70, 55));
        btnAdd.setMinimumSize(new Dimension(70, 55));
        btnAdd.setMaximumSize(new Dimension(70, 55));
        btnAdd.setHorizontalAlignment(SwingConstants.CENTER);
        btnAdd.addActionListener(e -> { if (onAdd != null) onAdd.run(); });

        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(btnAdd, c);

        return p;
    }

    // ================= LIST =================
    private JComponent buildList() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        // --- exemples (à remplacer par ta vraie liste plus tard) ---
        list.add(vehicleRow(new Vehicule(
                "AJ-433-BD","Peugeot","206",
                "23 - 01 - 1998","24 000 km",
                "Essence","Manuelle",
                "5","5","110 ch",
                "John Doe"
        )));
        list.add(Box.createVerticalStrut(14));

        list.add(vehicleRow(new Vehicule(
                "GN-432-CL","Citroen","C5",
                "10 - 09 - 2006","180 000 km",
                "Diesel","Manuelle",
                "5","5","140 ch",
                "Jean Dupont"
        )));
        // ----------------------------------------------------------

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        return sp;
    }

    // ================= ROW (style ClientList) =================
    private JPanel vehicleRow(Vehicule v) {
        CardPanel row = new CardPanel(18, BORDER);
        row.setLayout(new BorderLayout());
        row.setBorder(new EmptyBorder(14, 18, 14, 18));
        row.setOpaque(false);

        // Partie texte (immat / marque / modele / energie)
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);

        JLabel lImmat = new JLabel(v.getImmat());
        JLabel lMarque = new JLabel(v.getMarque());
        JLabel lModele = new JLabel(v.getModele());
        JLabel lEnergie = new JLabel(v.getEnergie());

        Font f = lImmat.getFont().deriveFont(Font.PLAIN, 18f);
        lImmat.setFont(f);
        lMarque.setFont(f);
        lModele.setFont(f);
        lEnergie.setFont(f);

        c.gridx = 0; c.weightx = 0.22;
        content.add(lImmat, c);

        c.gridx = 1; c.weightx = 0.22;
        content.add(lMarque, c);

        c.gridx = 2; c.weightx = 0.18;
        content.add(lModele, c);

        c.gridx = 3; c.weightx = 0.20;
        content.add(lEnergie, c);

        // Bouton Voir à droite (grand)
        RoundedButton btnVoir = new RoundedButton("Voir", ORANGE_MAIN, Color.WHITE, 20);
        btnVoir.setFont(btnVoir.getFont().deriveFont(Font.BOLD, 20f));
        btnVoir.setPreferredSize(new Dimension(160, 50));
        btnVoir.setMinimumSize(new Dimension(160, 50));
        btnVoir.setMaximumSize(new Dimension(160, 50));
        btnVoir.addActionListener(e -> { if (onView != null) onView.accept(v); });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnVoir);

        row.add(content, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        // Hauteur identique aux cartes Clients
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        return row;
    }
}
