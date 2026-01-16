package auto2i.ui.vehicule;

import auto2i.dao.ClientDao;
import auto2i.dao.TypeVehiculeDao;
import auto2i.dao.VehiculeDao;
import auto2i.Enum.*;
import auto2i.model.Client;
import auto2i.model.TypeVehicule;
import auto2i.model.Vehicule;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class VehiculeNewPanel extends JPanel {

    private final Runnable onBack;

    // DAO
    private final ClientDao clientDao = new ClientDao();
    private final VehiculeDao vehiculeDao = new VehiculeDao();

    // Valeur sélectionnée
    private Long selectedClientId = null;

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

    // ✅ enums (pas String)
    private JComboBox<Energie> cbEnergie;
    private JComboBox<TypeBoite> cbBoite;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VehiculeNewPanel(Runnable onBack) {
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        installValidators();
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

        c.gridx = 0; c.weightx = 0.45; c.insets = new Insets(10, 10, 10, 25);
        wrap.add(col1, c);

        c.gridx = 1; c.weightx = 0.40; c.insets = new Insets(10, 0, 10, 25);
        wrap.add(col2, c);

        c.gridx = 2; c.weightx = 0.15; c.insets = new Insets(10, 0, 10, 10);
        wrap.add(col3, c);

        return wrap;
    }

    private JPanel buildCol1() {
        JPanel col = columnPanel();

        tfImmat = new JTextField();
        col.add(labeledTextField("Immatriculation", tfImmat, "AA-123-BB"));

        tfDateCirculation = new JTextField();
        col.add(labeledDateField("Date mise en circulation", tfDateCirculation, "dd/MM/yyyy"));

        tfDernierKm = new JTextField();
        col.add(labeledTextField("Dernier kilométrage", tfDernierKm, "ex: 180000"));

        tfClient = new JTextField();
        tfClient.setEditable(false);
        tfClient.setBackground(Color.WHITE);
        col.add(labeledClientField("Client", tfClient, "Cliquer sur la loupe"));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildCol2() {
        JPanel col = columnPanel();

        tfMarque = new JTextField();
        col.add(labeledTextField("Marque", tfMarque, "Peugeot"));

        tfModele = new JTextField();
        col.add(labeledTextField("Modèle", tfModele, "206"));

        cbEnergie = new JComboBox<>(Energie.values());
        col.add(labeledCombo("Énergie", cbEnergie));

        cbBoite = new JComboBox<>(TypeBoite.values());
        col.add(labeledCombo("Boîte", cbBoite));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildCol3() {
        JPanel col = columnPanel();

        tfNbPortes = new JTextField();
        col.add(labeledTextField("Nb portes", tfNbPortes, "ex: 5"));

        tfNbPlaces = new JTextField();
        col.add(labeledTextField("Nb places", tfNbPlaces, "ex: 5"));

        tfPuissance = new JTextField();
        col.add(labeledTextField("Puissance", tfPuissance, "ex: 110"));

        col.add(Box.createVerticalGlue());
        return col;
    }

    // ================= FOOTER =================
    private JComponent buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 10, 0, 10));

        RoundedButton save = new RoundedButton("Enregistrer", ORANGE_MAIN, Color.WHITE, 20);
        save.setPreferredSize(new Dimension(220, 55));
        save.setFont(save.getFont().deriveFont(Font.BOLD, 18f));

        save.addActionListener(e -> onSave()); // enregistrement réel

        footer.add(save);
        return footer;
    }

    // ================== POPUP CLIENT ==================
    private void openClientPicker() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Choisir un client", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dlg.setSize(650, 480);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTextField tf = new JTextField();
        JButton bSearch = new JButton("Chercher");
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.add(tf, BorderLayout.CENTER);
        top.add(bSearch, BorderLayout.EAST);

        DefaultListModel<Client> model = new DefaultListModel<>();
        JList<Client> list = new JList<>(model);

        list.setCellRenderer((jList, value, index, isSelected, cellHasFocus) -> {
            JLabel lab = new JLabel();
            String txt = value.getPrenom() + " " + value.getNom();
            if (value.getEmail() != null && !value.getEmail().isBlank()) txt += " — " + value.getEmail();
            lab.setText(txt);
            lab.setOpaque(true);
            lab.setBorder(new EmptyBorder(8, 10, 8, 10));
            lab.setBackground(isSelected ? new Color(230, 240, 255) : Color.WHITE);
            return lab;
        });

        JScrollPane sp = new JScrollPane(list);

        JButton bOk = new JButton("Sélectionner");
        JButton bCancel = new JButton("Annuler");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(bCancel);
        bottom.add(bOk);

        Runnable doSearch = () -> {
            model.clear();
            String q = tf.getText();
            List<Client> res = (q == null || q.isBlank()) ? clientDao.findAll() : clientDao.search(q);
            for (Client c : res) model.addElement(c);
        };

        bSearch.addActionListener(e -> doSearch.run());
        tf.addActionListener(e -> doSearch.run());

        bCancel.addActionListener(e -> dlg.dispose());

        bOk.addActionListener(e -> {
            Client selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(dlg, "Sélectionne un client dans la liste.");
                return;
            }
            selectedClientId = selected.getId();
            tfClient.setText(selected.getPrenom() + " " + selected.getNom());
            dlg.dispose();
        });

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) bOk.doClick();
            }
        });

        root.add(top, BorderLayout.NORTH);
        root.add(sp, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        dlg.setContentPane(root);
        doSearch.run();
        dlg.setVisible(true);
    }

    // ================= VALIDATIONS =================
    private void installValidators() {
        tfImmat.setInputVerifier(new RegexVerifier(
                "^[A-Z]{2}-\\d{3}-[A-Z]{2}$",
                "Immatriculation invalide (ex: AA-123-BB)"
        ));

        tfDateCirculation.setInputVerifier(new InputVerifier() {
            @Override public boolean verify(JComponent input) {
                String s = ((JTextField) input).getText().trim();
                if (s.isEmpty()) return false;
                try { LocalDate.parse(s, DATE_FMT); return true; }
                catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(VehiculeNewPanel.this, "Date invalide (format attendu: dd/MM/yyyy)");
                    return false;
                }
            }
        });

        onlyDigits(tfDernierKm);
        onlyDigits(tfNbPortes);
        onlyDigits(tfNbPlaces);
        onlyDigits(tfPuissance);
    }

    private boolean validateForm() {
        if (!tfImmat.getInputVerifier().verify(tfImmat)) return false;
        if (!tfDateCirculation.getInputVerifier().verify(tfDateCirculation)) return false;

        if (tfImmat.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Immatriculation obligatoire."); return false; }
        if (selectedClientId == null) { JOptionPane.showMessageDialog(this, "Choisis un client (loupe)."); return false; }

        if (tfMarque.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Marque obligatoire."); return false; }
        if (tfModele.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Modèle obligatoire."); return false; }
        if (tfDernierKm.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Dernier kilométrage obligatoire."); return false; }
        if (tfNbPortes.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Nb portes obligatoire."); return false; }
        if (tfNbPlaces.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Nb places obligatoire."); return false; }
        if (tfPuissance.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Puissance obligatoire."); return false; }

        return true;
    }

    private void onSave() {
        if (!validateForm()) return;

        try {
            // 1) normaliser / lire champs
            String immat = tfImmat.getText().trim().toUpperCase();
            LocalDate date = LocalDate.parse(tfDateCirculation.getText().trim(), DATE_FMT);

            int km = Integer.parseInt(tfDernierKm.getText().trim());
            int nbPortes = Integer.parseInt(tfNbPortes.getText().trim());
            int nbPlaces = Integer.parseInt(tfNbPlaces.getText().trim());
            int puissance = Integer.parseInt(tfPuissance.getText().trim());

            String marque = tfMarque.getText().trim();
            String modele = tfModele.getText().trim();

            Energie energie = (Energie) cbEnergie.getSelectedItem();
            TypeBoite boite = (TypeBoite) cbBoite.getSelectedItem();

            // 2) immat unique (avant transaction)
            Vehicule existing = vehiculeDao.findByImmat(immat);
            if (existing != null) {
                // si création -> interdit
                if (vehiculeEditingId == null) {
                    throw new IllegalArgumentException("Cette immatriculation existe déjà.");
                }
                // si édition -> interdit seulement si c’est un autre véhicule
                if (!existing.getId().equals(vehiculeEditingId)) {
                    throw new IllegalArgumentException("Cette immatriculation existe déjà.");
                }
            }


            // 3) récupérer client
            Client client = clientDao.findById(selectedClientId);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable (re-sélectionne le client).");
            }

            // 4) type véhicule : find or create
            TypeVehicule tv = TypeVehiculeDao.findExisting(marque, modele, energie, boite, nbPortes, nbPlaces, puissance);
            if (tv == null) {
                tv = new TypeVehicule(marque, modele, energie, boite, nbPortes, nbPlaces, puissance);
                TypeVehiculeDao.save(tv);
            }

            // 5) créer / mettre à jour véhicule
            if (vehiculeEditingId == null) {

                // création
                Vehicule v = new Vehicule(immat, date, km, client, tv);
                vehiculeDao.save(v);

            } else {

                // modification
                Vehicule v = vehiculeDao.findById(vehiculeEditingId);
                if (v == null) {
                    throw new IllegalArgumentException("Véhicule introuvable.");
                }

                // ⚠️ si tu autorises la modification de l’immat :
                // - soit tu ne fais pas le check d’unicité pour le même véhicule
                // - soit tu vérifies que l’immat n’appartient pas à un autre véhicule
                v.setImmat(immat);
                v.setDateMiseEnCirculation(date);
                v.setDernierKilometrage(km);
                v.setClient(client);
                v.setTypeVehicule(tv);

                vehiculeDao.update(v);
            }


            JOptionPane.showMessageDialog(this, "Véhicule enregistré ✅");

            resetForm();
            if (onBack != null) onBack.run();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onlyDigits(JTextField tf) {
        ((AbstractDocument) tf.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("\\d+")) super.insertString(fb, offset, string, attr);
            }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null || text.isEmpty() || text.matches("\\d+")) super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    private static class RegexVerifier extends InputVerifier {
        private final String regex;
        private final String message;
        RegexVerifier(String regex, String message) { this.regex = regex; this.message = message; }

        @Override public boolean verify(JComponent input) {
            String s = ((JTextField) input).getText().trim().toUpperCase();
            if (s.isEmpty()) return false;
            boolean ok = s.matches(regex);
            if (!ok) JOptionPane.showMessageDialog(input, message);
            else ((JTextField) input).setText(s);
            return ok;
        }
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

    private JPanel labeledCombo(String label, JComboBox<?> combo) { // combo générique
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

        JButton cal = iconButton("calendar.png"); // optionnel
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
        search.addActionListener(e -> openClientPicker());

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
        comp.setPreferredSize(new Dimension(10, h));
        comp.setMinimumSize(new Dimension(10, h));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void resetForm() {
        tfImmat.setText("");
        tfDateCirculation.setText("");
        tfDernierKm.setText("");
        tfClient.setText("");
        selectedClientId = null;

        tfMarque.setText("");
        tfModele.setText("");
        tfNbPortes.setText("");
        tfNbPlaces.setText("");
        tfPuissance.setText("");

        cbEnergie.setSelectedIndex(0);
        cbBoite.setSelectedIndex(0);

        vehiculeEditingId = null;

    }

    private Long vehiculeEditingId = null;

    public void editVehicule(Vehicule v) {
        if (v == null) return;

        this.vehiculeEditingId = v.getId();

        tfImmat.setText(v.getImmat() != null ? v.getImmat() : "");
        if (v.getDateMiseEnCirculation() != null) {
            tfDateCirculation.setText(v.getDateMiseEnCirculation().format(DATE_FMT));
        } else tfDateCirculation.setText("");

        tfDernierKm.setText(v.getDernierKilometrage() != null ? String.valueOf(v.getDernierKilometrage()) : "");

        if (v.getClient() != null) {
            selectedClientId = v.getClient().getId();
            tfClient.setText((v.getClient().getPrenom() + " " + v.getClient().getNom()).trim());
        }

        TypeVehicule tv = v.getTypeVehicule();
        if (tv != null) {
            tfMarque.setText(tv.getMarque() != null ? tv.getMarque() : "");
            tfModele.setText(tv.getModele() != null ? tv.getModele() : "");

            if (tv.getEnergie() != null) cbEnergie.setSelectedItem(tv.getEnergie());
            if (tv.getBoiteVitesse() != null) cbBoite.setSelectedItem(tv.getBoiteVitesse());

            tfNbPortes.setText(tv.getNbPortes() != null ? String.valueOf(tv.getNbPortes()) : "");
            tfNbPlaces.setText(tv.getNbPlaces() != null ? String.valueOf(tv.getNbPlaces()) : "");
            tfPuissance.setText(tv.getPuissance() != null ? String.valueOf(tv.getPuissance()) : "");
        }
    }

}
