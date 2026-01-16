package auto2i.ui.vehicule;

import auto2i.model.Client;
import auto2i.model.TypeVehicule;
import auto2i.model.Vehicule;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;
import auto2i.dao.VehiculeDao;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class VehiculeShowPanel extends JPanel {

    private final Runnable onBack;
    private final Consumer<Vehicule> onEdit;

    private final VehiculeDao vehiculeDao = new VehiculeDao();
    private Vehicule vehicule; // véhicule affiché


    private JTextField tfImmat, tfMarque, tfModele, tfDate, tfDernierKm, tfEnergie, tfBoite;
    private JTextField tfNbPortes, tfNbPlaces, tfPuissance, tfClient;

    private JLabel lblIntervDate, lblIntervType;

    public VehiculeShowPanel(Runnable onBack, Consumer<Vehicule> onEdit) {
        this.onBack = onBack;
        this.onEdit = onEdit;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

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

    private JComponent buildContent() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 18, 10, 18);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;

        JPanel col1 = buildLeftInfos();
        JPanel col2 = buildMiddleInfos();
        JPanel col3 = buildSmallFields();
        JPanel col4 = buildRightActions();

        c.gridy = 0;
        c.weighty = 0.0;

        c.gridx = 0; c.weightx = 0.30; wrap.add(col1, c);
        c.gridx = 1; c.weightx = 0.27; wrap.add(col2, c);
        c.gridx = 2; c.weightx = 0.15; wrap.add(col3, c);
        c.gridx = 3; c.weightx = 0.28; wrap.add(col4, c);

        JPanel clientLine = buildClientLine();
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 18, 0, 18);
        wrap.add(clientLine, c);

        c.gridx = 3;
        c.gridwidth = 1;
        c.weightx = 0.0;
        wrap.add(Box.createVerticalStrut(1), c);

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

    private JPanel buildMiddleInfos() {
        JPanel col = columnPanel();

        tfMarque = makeReadField();
        col.add(labeledField("Marque", tfMarque, COL2_W));

        tfModele = makeReadField();
        col.add(labeledField("Modèle", tfModele, COL2_W));

        tfEnergie = makeReadField();
        col.add(labeledField("Énergie", tfEnergie, COL2_W));

        tfBoite = makeReadField();
        col.add(labeledField("Boîte", tfBoite, COL2_W));

        col.add(Box.createVerticalGlue());
        return col;
    }

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

        lblIntervDate = new JLabel("—");
        lblIntervType = new JLabel("—", SwingConstants.RIGHT);

        header.add(lblIntervDate, BorderLayout.WEST);
        header.add(lblIntervType, BorderLayout.EAST);

        interventions.add(header, BorderLayout.NORTH);
        col.add(interventions);

        col.add(Box.createVerticalStrut(18));

        RoundedButton bAddInter = new RoundedButton("Ajouter Intervention", ORANGE_MAIN, Color.WHITE, 20);
        RoundedButton bModif    = new RoundedButton("Modifier", ORANGE_MAIN, Color.WHITE, 20);
        RoundedButton bDelete   = new RoundedButton("Effacer", ORANGE_MAIN, Color.WHITE, 20);

        bAddInter.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Interventions : à implémenter ...")
        );
        //  listeners AVANT le return
        bModif.addActionListener(e -> {
            if (vehicule != null && onEdit != null) onEdit.accept(vehicule);
        });
        bDelete.addActionListener(e -> onDelete());

        Dimension btnSize = new Dimension(320, 54);
        for (RoundedButton b : new RoundedButton[]{bAddInter, bModif, bDelete}) {
            b.setPreferredSize(btnSize);
            b.setMinimumSize(btnSize);
            b.setMaximumSize(btnSize);
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
        this.vehicule = v;
        if (v == null) return;

        tfImmat.setText(nvl(v.getImmat()));
        tfDate.setText(formatDate(v.getDateMiseEnCirculation()));
        tfDernierKm.setText(v.getDernierKilometrage() != null ? String.valueOf(v.getDernierKilometrage()) : "");

        TypeVehicule tv = v.getTypeVehicule();
        if (tv != null) {
            tfMarque.setText(nvl(tv.getMarque()));
            tfModele.setText(nvl(tv.getModele()));
            tfEnergie.setText(prettyEnum(tv.getEnergie()));
            tfBoite.setText(prettyEnum(tv.getBoiteVitesse()));

            tfNbPortes.setText(tv.getNbPortes() != null ? String.valueOf(tv.getNbPortes()) : "");
            tfNbPlaces.setText(tv.getNbPlaces() != null ? String.valueOf(tv.getNbPlaces()) : "");
            tfPuissance.setText(tv.getPuissance() != null ? String.valueOf(tv.getPuissance()) : "");
        } else {
            tfMarque.setText("");
            tfModele.setText("");
            tfEnergie.setText("");
            tfBoite.setText("");
            tfNbPortes.setText("");
            tfNbPlaces.setText("");
            tfPuissance.setText("");
        }

        Client c = v.getClient();
        if (c != null) {
            tfClient.setText((nvl(c.getPrenom()) + " " + nvl(c.getNom())).trim());
        } else {
            tfClient.setText("");
        }
    }

    private String nvl(String s) { return (s == null) ? "" : s; }

    private String prettyEnum(Object o) {
        if (o == null) return "";
        String s = o.toString().replace('_', ' ').toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String formatDate(LocalDate d) {
        if (d == null) return "";
        return d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    private void onDelete() {
        if (vehicule == null || vehicule.getId() == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le véhicule \"" + nvl(vehicule.getImmat()) + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            vehiculeDao.delete(vehicule.getId());

            JOptionPane.showMessageDialog(this,
                    "Véhicule supprimé ✅",
                    "OK",
                    JOptionPane.INFORMATION_MESSAGE);

            if (onBack != null) onBack.run();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression :\n" + ex.getMessage(),
                    "Erreur BDD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
