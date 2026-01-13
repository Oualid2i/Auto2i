package auto2i.ui.vehicule;

import auto2i.model.Vehicule;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class VehiculeShowPanel extends JPanel {

    private final Runnable onBack;

    // champs affichage
    private JTextField tfImmat, tfMarque, tfModele, tfDate, tfDernierKm, tfEnergie, tfBoite;
    private JTextField tfNbPortes, tfNbPlaces, tfPuissance, tfClient;

    private JLabel lblIntervDate, lblIntervType;

    public VehiculeShowPanel(Runnable onBack) {
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
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

        JLabel title = new JLabel("Fiche véhicule", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }

    // ================= CONTENT (4 colonnes + client en bas sur 3 colonnes) =================
    private JComponent buildContent() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 18, 10, 18);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;

        JPanel col1 = buildLeftInfos();       // immat / date / km
        JPanel col2 = buildMiddleInfos();     // marque / modèle / énergie / boite
        JPanel col3 = buildSmallFields();     // nb portes / nb places / puissance
        JPanel col4 = buildRightActions();    // interventions + boutons

        // ===== Ligne 0 : 4 colonnes =====
        c.gridy = 0;
        c.weighty = 0.0;

        c.gridx = 0; c.weightx = 0.30;
        wrap.add(col1, c);

        c.gridx = 1; c.weightx = 0.27;
        wrap.add(col2, c);

        c.gridx = 2; c.weightx = 0.15;
        wrap.add(col3, c);

        c.gridx = 3; c.weightx = 0.28;
        wrap.add(col4, c);

        // ===== Ligne 1 : Client (span 3 colonnes, comme la mockup) =====
        JPanel clientLine = buildClientLine();
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 18, 0, 18);
        wrap.add(clientLine, c);

        // case vide sous la colonne 4 pour garder l’alignement
        c.gridx = 3;
        c.gridwidth = 1;
        c.weightx = 0.0;
        wrap.add(Box.createVerticalStrut(1), c);

        // ===== Ligne 2 : filler pour pousser en haut =====
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 4;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        wrap.add(Box.createVerticalGlue(), c);

        return wrap;
    }

    // ================= COLONNE 1 =================
    private JPanel buildLeftInfos() {
        JPanel col = columnPanel();

        tfImmat = makeReadField();
        col.add(labeledField("Immatriculation", tfImmat, COL1_W));

        tfDate = makeReadField();
        col.add(labeledDateLike("Date mise en circulation", tfDate, COL1_W));

        tfDernierKm = makeReadField();
        col.add(labeledField("Dernier Kilometrage", tfDernierKm, COL1_W));

        col.add(Box.createVerticalGlue());
        return col;
    }

    // ================= COLONNE 2 =================
    private JPanel buildMiddleInfos() {
        JPanel col = columnPanel();

        tfMarque = makeReadField();
        col.add(labeledField("Marque", tfMarque, COL2_W));

        tfModele = makeReadField();
        col.add(labeledField("Modèle", tfModele, COL2_W));

        tfEnergie = makeReadField();
        col.add(labeledField("énergie", tfEnergie, COL2_W));

        tfBoite = makeReadField();
        col.add(labeledField("Boite", tfBoite, COL2_W));

        col.add(Box.createVerticalGlue());
        return col;
    }

    // ================= COLONNE 3 (petits champs) =================
    private JPanel buildSmallFields() {
        JPanel col = columnPanel();

        tfNbPortes = makeReadField();
        col.add(labeledField("Nombre Portes", tfNbPortes, 120));

        tfNbPlaces = makeReadField();
        col.add(labeledField("Nombre Places", tfNbPlaces, 120));

        tfPuissance = makeReadField();
        col.add(labeledField("Puissance", tfPuissance, 140));

        col.add(Box.createVerticalGlue());
        return col;
    }

    // ================= COLONNE 4 (interventions + boutons) =================
    private JPanel buildRightActions() {
        JPanel col = columnPanel();

        JLabel t = new JLabel("Dernières interventions réalisées");
        t.setForeground(Color.DARK_GRAY);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 14f));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(t);
        col.add(Box.createVerticalStrut(8));

        CardPanel interventions = new CardPanel(22, BORDER);
        interventions.setLayout(new BorderLayout());
        interventions.setPreferredSize(new Dimension(320, 260));
        interventions.setMaximumSize(new Dimension(320, 260));
        interventions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(12, 14, 0, 14));

        lblIntervDate = new JLabel("12/03/2025");
        lblIntervType = new JLabel("Entretien", SwingConstants.RIGHT);

        header.add(lblIntervDate, BorderLayout.WEST);
        header.add(lblIntervType, BorderLayout.EAST);

        interventions.add(header, BorderLayout.NORTH);
        col.add(interventions);

        col.add(Box.createVerticalStrut(18));

        RoundedButton bAddInter = new RoundedButton("Ajouter Intervention", ORANGE_MAIN, Color.WHITE, 20);
        RoundedButton bModif    = new RoundedButton("Modifier", ORANGE_MAIN, Color.WHITE, 20);
        RoundedButton bDelete   = new RoundedButton("Effacer", ORANGE_MAIN, Color.WHITE, 20);

        Dimension btnSize = new Dimension(320, 54);
        for (RoundedButton b : new RoundedButton[]{bAddInter, bModif, bDelete}) {
            b.setPreferredSize(btnSize);
            b.setMinimumSize(btnSize);
            b.setMaximumSize(btnSize); // empêche le collapse
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setFont(b.getFont().deriveFont(Font.BOLD, 18f));
        }

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttons.add(bAddInter);
        buttons.add(Box.createVerticalStrut(12));
        buttons.add(bModif);
        buttons.add(Box.createVerticalStrut(12));
        buttons.add(bDelete);

        col.add(buttons);
        col.add(Box.createVerticalGlue());

        return col;
    }

    // ================= CLIENT LINE (bas, sur 3 colonnes) =================
    private JPanel buildClientLine() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel("Client");
        l.setForeground(Color.DARK_GRAY);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 16f));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        // largeur = COL1 + COL2 + COL3 + (petit espace visuel)
        int clientW = COL1_W + COL2_W + 140;
        line.setPreferredSize(new Dimension(clientW, FIELD_H));
        line.setMaximumSize(new Dimension(clientW, FIELD_H));

        tfClient = makeReadField();
        tfClient.setFont(tfClient.getFont().deriveFont(Font.BOLD, 14f));

        JButton search = new JButton();
        search.setPreferredSize(new Dimension(52, FIELD_H));
        search.setFocusPainted(false);
        search.setBorder(BorderFactory.createLineBorder(BORDER));
        search.setBackground(Color.WHITE);
        search.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon ico = load("search.png", 22);
        if (ico != null) search.setIcon(ico);

        line.add(tfClient, BorderLayout.CENTER);
        line.add(search, BorderLayout.EAST);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(line);

        return p;
    }

    // ================= HELPERS UI =================
    private JPanel columnPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JTextField makeReadField() {
        JTextField f = new JTextField();
        f.setEditable(false);
        f.setBackground(Color.WHITE);
        f.setFont(f.getFont().deriveFont(Font.PLAIN, 14f));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return f;
    }

    private JPanel labeledField(String label, JComponent field, int width) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 16f));
        l.setForeground(Color.DARK_GRAY);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        setFixedSize(field, width, FIELD_H);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JPanel labeledDateLike(String label, JTextField field, int width) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 16f));
        l.setForeground(Color.DARK_GRAY);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);

        int iconW = ICON_BTN_W;
        setFixedSize(line, width, FIELD_H);
        setFixedSize(field, width - iconW, FIELD_H);

        JButton cal = new JButton();
        setFixedSize(cal, iconW, FIELD_H);
        cal.setFocusPainted(false);
        cal.setBorder(BorderFactory.createLineBorder(BORDER));
        cal.setBackground(Color.WHITE);

        ImageIcon ico = load("calendar.png", 22);
        if (ico != null) cal.setIcon(ico);

        line.add(field, BorderLayout.CENTER);
        line.add(cal, BorderLayout.EAST);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(line);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private void setFixedSize(JComponent c, int w, int h) {
        Dimension d = new Dimension(w, h);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);

        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // ================= API: afficher un véhicule =================
    public void setVehicule(Vehicule v) {
        if (v == null) return;

        tfImmat.setText(v.getImmat());
        tfMarque.setText(v.getMarque());
        tfModele.setText(v.getModele());
        tfDate.setText(v.getDateCirculation());
        tfDernierKm.setText(v.getDernierKm());
        tfEnergie.setText(v.getEnergie());
        tfBoite.setText(v.getBoite());
        tfNbPortes.setText(v.getNbPortes());
        tfNbPlaces.setText(v.getNbPlaces());
        tfPuissance.setText(v.getPuissance());
        tfClient.setText(v.getClient());
    }
}
