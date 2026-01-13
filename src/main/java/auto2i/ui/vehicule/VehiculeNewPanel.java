package auto2i.ui.vehicule;

import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*; // load(...)

public class VehiculeNewPanel extends JPanel {

    private final Runnable onBack;

    // Champs
    private JTextField tfImmat;
    private JTextField tfMarque;
    private JTextField tfModele;

    private JTextField tfDernierKm;
    private JTextField tfPuissance;
    private JTextField tfNbPortes;
    private JTextField tfNbPlaces;

    private JTextField tfDateCirculation;
    private JTextField tfClient;

    private JComboBox<String> cbEnergie;
    private JComboBox<String> cbBoite;

    public VehiculeNewPanel(Runnable onBack) {
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
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

        JLabel title = new JLabel("Ajout véhicule", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }

    // ================= FORM =================
    private JComponent buildForm() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;

        JPanel col1 = buildCol1();
        JPanel col2 = buildCol2();
        JPanel col3 = buildCol3();

        // Col1 (large)
        c.gridx = 0; c.weightx = 0.45;
        c.insets = new Insets(10, 10, 10, 25);
        wrap.add(col1, c);

        // Col2 (moyenne)
        c.gridx = 1; c.weightx = 0.40;
        c.insets = new Insets(10, 0, 10, 25);
        wrap.add(col2, c);

        // Col3 (petite)
        c.gridx = 2; c.weightx = 0.15;
        c.insets = new Insets(10, 0, 10, 10);
        wrap.add(col3, c);

        return wrap;
    }

    private JPanel buildCol1() {
        JPanel col = columnPanel();

        tfImmat = new JTextField();
        col.add(labeledTextField("Immatriculation", tfImmat, "AA-123-BB"));

        tfDateCirculation = new JTextField();
        col.add(labeledDateField("Date mise en circulation", tfDateCirculation, "23 - 01 - 1998"));

        tfDernierKm = new JTextField();
        col.add(labeledTextField("Dernier kilométrage", tfDernierKm, "30 000 km"));

        tfClient = new JTextField();
        col.add(labeledClientField("Client", tfClient, "John Doe"));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildCol2() {
        JPanel col = columnPanel();

        tfMarque = new JTextField();
        col.add(labeledTextField("Marque", tfMarque, "Peugeot"));

        tfModele = new JTextField();
        col.add(labeledTextField("Modèle", tfModele, "206"));

        cbEnergie = new JComboBox<>(new String[]{"Essence", "Diesel", "Hybride", "Électrique"});
        col.add(labeledCombo("Énergie", cbEnergie));

        cbBoite = new JComboBox<>(new String[]{"Manuelle", "Automatique"});
        col.add(labeledCombo("Boîte", cbBoite));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildCol3() {
        JPanel col = columnPanel();

        tfNbPortes = new JTextField();
        col.add(labeledTextField("Nb portes", tfNbPortes, "5"));

        tfNbPlaces = new JTextField();
        col.add(labeledTextField("Nb places", tfNbPlaces, "5"));

        tfPuissance = new JTextField();
        col.add(labeledTextField("Puissance", tfPuissance, "110 ch"));

        col.add(Box.createVerticalGlue());
        return col;
    }

    // ================= FOOTER (bouton indépendant) =================
    private JComponent buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 10, 0, 10));

        RoundedButton save = new RoundedButton("Enregistrer", ORANGE_MAIN, Color.WHITE, 20);
        save.setPreferredSize(new Dimension(220, 55));
        save.setFont(save.getFont().deriveFont(Font.BOLD, 18f));

        footer.add(save);
        return footer;
    }

    // ================= UI helpers =================
    private JPanel columnPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JPanel labeledTextField(String label, JTextField field, String placeholder) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        styleField(field, placeholder);
        setFieldHeight(field, FIELD_H);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JPanel labeledCombo(String label, JComboBox<String> combo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        setFieldHeight(combo, FIELD_H);
        combo.setFont(combo.getFont().deriveFont(Font.BOLD, 14f));
        combo.setBackground(Color.WHITE);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(combo);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JPanel labeledDateField(String label, JTextField field, String placeholder) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        styleField(field, placeholder);
        setFieldHeight(field, FIELD_H);

        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        setFieldHeight(line, FIELD_H);

        JButton cal = iconButton("calendar.png");

        line.add(field, BorderLayout.CENTER);
        line.add(cal, BorderLayout.EAST);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(line);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JPanel labeledClientField(String label, JTextField field, String placeholder) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        styleField(field, placeholder);
        field.setFont(field.getFont().deriveFont(Font.BOLD, 14f));
        setFieldHeight(field, FIELD_H);

        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        setFieldHeight(line, FIELD_H);

        JButton search = iconButton("search.png");

        line.add(field, BorderLayout.CENTER);
        line.add(search, BorderLayout.EAST);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(line);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JLabel labelLeft(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.PLAIN,16f));
        l.setForeground(Color.DARK_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton iconButton(String iconName) {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(ICON_BTN_W, FIELD_H));
        b.setMinimumSize(new Dimension(ICON_BTN_W, FIELD_H));
        b.setMaximumSize(new Dimension(ICON_BTN_W, FIELD_H));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(BORDER));
        b.setBackground(Color.WHITE);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon ico = load(iconName, 22);
        if (ico != null) b.setIcon(ico);

        return b;
    }

    private void styleField(JTextField f, String placeholder) {
        f.setFont(f.getFont().deriveFont(Font.PLAIN, 14f));
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        f.setToolTipText(placeholder);
    }

    private void setFieldHeight(JComponent comp, int h) {
        // largeur flexible, hauteur fixe
        comp.setPreferredSize(new Dimension(10, h));
        comp.setMinimumSize(new Dimension(10, h));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
