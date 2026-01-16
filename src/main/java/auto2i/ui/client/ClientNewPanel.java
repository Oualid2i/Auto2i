package auto2i.ui.client;

import auto2i.dao.ClientDao;
import auto2i.model.Client;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;
import auto2i.dao.VehiculeDao;
import auto2i.model.Vehicule;
import auto2i.model.TypeVehicule;
import java.util.List;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class ClientNewPanel extends JPanel {

    private final Runnable onBack;
    private final ClientDao clientDao = new ClientDao();
    private Client editingClient = null; // null = création, sinon édition

    private final VehiculeDao vehiculeDao = new VehiculeDao();

    private JTextField tfSearchVehicule;
    private JPanel vehiculesListBox;     // conteneur des lignes
    private CardPanel vehiculesCard;     // la carte qui contient la liste
    private RoundedButton btnAddVehicule;


    private JTextField tfPrenom, tfNom, tfEmail, tfTel;

    public ClientNewPanel(Runnable onBack) {
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH); // ✅ comme VehiculeNew
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));

        RoundedButton back = new RoundedButton("←", ORANGE_MAIN, Color.WHITE, 16);
        back.setPreferredSize(new Dimension(50, 40));
        back.setHorizontalAlignment(SwingConstants.CENTER);
        back.addActionListener(e -> {
            resetForm();
            if (onBack != null) onBack.run();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(back);

        JLabel title = new JLabel("Ajout client", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }

    private JComponent buildForm() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;

        JPanel left = buildLeftFields();
        JPanel right = buildVehiculesBox(); // juste visuel mockup

        c.gridx = 0; c.weightx = 0.45;
        c.insets = new Insets(10, 10, 10, 25);
        wrap.add(left, c);

        c.gridx = 1; c.weightx = 0.55;
        c.insets = new Insets(10, 0, 10, 10);
        wrap.add(right, c);

        return wrap;
    }

    private JPanel buildLeftFields() {
        JPanel col = columnPanel();

        tfPrenom = new JTextField();
        col.add(labeledTextField("Prénom", tfPrenom));

        tfNom = new JTextField();
        col.add(labeledTextField("Nom", tfNom));

        tfEmail = new JTextField();
        col.add(labeledTextField("Email", tfEmail));

        tfTel = new JTextField();
        col.add(labeledTextField("Téléphone", tfTel));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildVehiculeSearchBar() {
        JPanel line = new JPanel(new BorderLayout(10, 0));
        line.setOpaque(false);

        tfSearchVehicule = new JTextField();
        tfSearchVehicule.setFont(tfSearchVehicule.getFont().deriveFont(Font.PLAIN, 14f));
        tfSearchVehicule.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tfSearchVehicule.setPreferredSize(new Dimension(300, FIELD_H));

        btnAddVehicule = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 18);
        btnAddVehicule.setPreferredSize(new Dimension(55, FIELD_H));
        btnAddVehicule.setHorizontalAlignment(SwingConstants.CENTER);

        btnAddVehicule.addActionListener(e -> {
            if (editingClient == null) {
                JOptionPane.showMessageDialog(this,
                        "Tu peux ajouter des véhicules uniquement en mode édition (après avoir créé le client).",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            openVehiculePicker(tfSearchVehicule.getText());
        });

        line.add(tfSearchVehicule, BorderLayout.CENTER);
        line.add(btnAddVehicule, BorderLayout.EAST);

        return line;
    }


    private JPanel buildVehiculesBox() {
        JPanel col = columnPanel();

        JLabel l = labelLeft("Véhicules");
        col.add(l);
        col.add(Box.createVerticalStrut(6));

        col.add(buildVehiculeSearchBar());
        col.add(Box.createVerticalStrut(12));

        vehiculesCard = new CardPanel(26, BORDER);
        vehiculesCard.setLayout(new BorderLayout());
        vehiculesCard.setBorder(new EmptyBorder(18, 18, 18, 18));

        vehiculesListBox = new JPanel();
        vehiculesListBox.setOpaque(false);
        vehiculesListBox.setLayout(new BoxLayout(vehiculesListBox, BoxLayout.Y_AXIS));

        JScrollPane sp = new JScrollPane(vehiculesListBox);
        sp.setBorder(null);
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        vehiculesCard.add(sp, BorderLayout.CENTER);
        col.add(vehiculesCard);

        col.add(Box.createVerticalGlue());

        // état initial (création => pas de véhicules)
        refreshVehiculesUI();

        return col;
    }


    private JComponent mockVehiculeRow(String immat, String marque, String modele, String energie) {
        CardPanel row = new CardPanel(18, BORDER);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));
        row.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel grid = new JPanel(new GridLayout(1, 4, 18, 0));
        grid.setOpaque(false);
        grid.add(new JLabel(immat));
        grid.add(new JLabel(marque));
        grid.add(new JLabel(modele));
        grid.add(new JLabel(energie));

        RoundedButton minus = new RoundedButton("-", ORANGE_MAIN, Color.WHITE, 20);
        minus.setPreferredSize(new Dimension(60, 46));
        minus.setFont(minus.getFont().deriveFont(Font.BOLD, 18f));

        row.add(grid, BorderLayout.CENTER);
        row.add(minus, BorderLayout.EAST);
        return row;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 10, 0, 10));

        RoundedButton save = new RoundedButton("Enregistrer", ORANGE_MAIN, Color.WHITE, 20);
        save.setPreferredSize(new Dimension(320, 55));
        save.setFont(save.getFont().deriveFont(Font.BOLD, 18f));

        // ✅ Liaison BDD
        save.addActionListener(e -> onSave());

        footer.add(save);
        return footer;
    }

    private void onSave() {
        String prenom = tfPrenom.getText().trim();
        String nom = tfNom.getText().trim();
        String email = tfEmail.getText().trim();
        String tel = tfTel.getText().trim();

        if (prenom.isBlank() || nom.isBlank() || email.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Prénom, nom et email sont obligatoires.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isNomPrenomValide(prenom)) {
            JOptionPane.showMessageDialog(this,
                    "Prénom invalide : pas de chiffres / caractères spéciaux.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isNomPrenomValide(nom)) {
            JOptionPane.showMessageDialog(this,
                    "Nom invalide : pas de chiffres / caractères spéciaux.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isEmailValide(email)) {
            JOptionPane.showMessageDialog(this,
                    "Email invalide.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isTelephoneValide(tel)) {
            JOptionPane.showMessageDialog(this,
                    "Téléphone invalide.\nExemples : 06 12 34 56 78 ou +33 6 12 34 56 78",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (editingClient == null) {
                // ✅ création
                Client c = new Client(prenom, nom, email, tel.isBlank() ? null : tel);
                clientDao.save(c);

                JOptionPane.showMessageDialog(this,
                        "Client enregistré ✅",
                        "OK",
                        JOptionPane.INFORMATION_MESSAGE);

                resetForm(); // vide après création
            } else {
                // ✅ modification
                editingClient.setPrenom(prenom);
                editingClient.setNom(nom);
                editingClient.setEmail(email);
                editingClient.setTelephone(tel.isBlank() ? null : tel);

                clientDao.update(editingClient); // ou save() si ton DAO fait merge
                JOptionPane.showMessageDialog(this,
                        "Client modifié ✅",
                        "OK",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (onBack != null) onBack.run();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur BDD : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

    }


    // ===== helpers (labels à gauche)
    private JPanel columnPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JPanel labeledTextField(String label, JTextField field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        styleField(field);
        setFieldHeight(field, FIELD_H);

        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        p.add(Box.createVerticalStrut(14));
        return p;
    }

    private JLabel labelLeft(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 16f));
        l.setForeground(Color.DARK_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(f.getFont().deriveFont(Font.PLAIN, 14f));
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        f.setAlignmentX(Component.LEFT_ALIGNMENT); // ✅ important
    }

    private void setFieldHeight(JComponent comp, int h) {
        comp.setPreferredSize(new Dimension(10, h));
        comp.setMinimumSize(new Dimension(10, h));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT); // ✅ important
    }

    public void resetForm() {
        editingClient = null; // repasse en mode création
        tfPrenom.setText("");
        tfNom.setText("");
        tfEmail.setText("");
        tfTel.setText("");
        tfSearchVehicule.setText("");
        refreshVehiculesUI();

    }

    public void editClient(Client c) {
        if (c == null) return;

        editingClient = c;

        tfPrenom.setText(c.getPrenom());
        tfNom.setText(c.getNom());
        tfEmail.setText(c.getEmail());
        tfTel.setText(c.getTelephone() == null ? "" : c.getTelephone());

        tfSearchVehicule.setText("");
        refreshVehiculesUI();

    }

    private boolean isNomPrenomValide(String s) {
        // lettres, espaces, tiret, apostrophe, accents OK
        return s != null && s.matches("^[A-Za-zÀ-ÖØ-öø-ÿ'\\-\\s]+$");
    }

    private boolean isEmailValide(String email) {
        if (email == null) return false;
        // email simple
        boolean ok = email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if (!ok) return false;

        // ✅ si tu veux forcer gmail uniquement :
        // return email.toLowerCase().endsWith("@gmail.com");

        return true;
    }

    private boolean isTelephoneValide(String tel) {
        if (tel == null || tel.isBlank()) return true; // téléphone optionnel
        String t = tel.trim();

        // formats acceptés :
        // +33 6 12 34 56 78
        // +33 06 12 34 56 78 (on accepte)
        // 06 12 34 56 78
        // 0612345678
        return t.matches("^\\+33\\s?[0-9]\\s?(\\d{2}\\s?){4}$")
                || t.matches("^0\\d(\\s?\\d{2}){4}$");
    }

    private void refreshVehiculesUI() {
        if (vehiculesListBox == null) return;

        vehiculesListBox.removeAll();

        if (editingClient == null) {
            JLabel msg = new JLabel("Crée d'abord le client pour gérer ses véhicules.");
            msg.setForeground(Color.GRAY);
            vehiculesListBox.add(msg);
        } else {
            List<Vehicule> vehs = vehiculeDao.findByClientId(editingClient.getId());

            if (vehs.isEmpty()) {
                JLabel msg = new JLabel("Aucun véhicule pour ce client.");
                msg.setForeground(Color.GRAY);
                vehiculesListBox.add(msg);
            } else {
                for (Vehicule v : vehs) {
                    vehiculesListBox.add(buildVehiculeRow(v));
                    vehiculesListBox.add(Box.createVerticalStrut(12));
                }
            }
        }

        vehiculesListBox.revalidate();
        vehiculesListBox.repaint();
    }

    private JComponent buildVehiculeRow(Vehicule v) {
        CardPanel row = new CardPanel(18, BORDER);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));
        row.setBorder(new EmptyBorder(12, 16, 12, 16));

        TypeVehicule tv = v.getTypeVehicule();

        String immat = v.getImmat() != null ? v.getImmat() : "-";
        String marque = (tv != null && tv.getMarque() != null) ? tv.getMarque() : "-";
        String modele = (tv != null && tv.getModele() != null) ? tv.getModele() : "-";
        String energie = (tv != null && tv.getEnergie() != null) ? tv.getEnergie().toString() : "-";

        JPanel grid = new JPanel(new GridLayout(1, 4, 18, 0));
        grid.setOpaque(false);
        grid.add(new JLabel(immat));
        grid.add(new JLabel(marque));
        grid.add(new JLabel(modele));
        grid.add(new JLabel(energie));

        RoundedButton minus = new RoundedButton("-", ORANGE_MAIN, Color.WHITE, 20);
        minus.setPreferredSize(new Dimension(60, 46));
        minus.setFont(minus.getFont().deriveFont(Font.BOLD, 18f));
        minus.addActionListener(e -> deleteVehicule(v));

        row.add(grid, BorderLayout.CENTER);
        row.add(minus, BorderLayout.EAST);
        return row;
    }

    private void deleteVehicule(Vehicule v) {
        if (v == null || v.getId() == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le véhicule \"" + v.getImmat() + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            vehiculeDao.delete(v.getId());
            refreshVehiculesUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur suppression véhicule : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openVehiculePicker(String query) {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Ajouter un véhicule au client", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dlg.setSize(720, 520);
        dlg.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTextField tf = new JTextField(query == null ? "" : query);
        JButton bSearch = new JButton("Chercher");
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.add(tf, BorderLayout.CENTER);
        top.add(bSearch, BorderLayout.EAST);

        DefaultListModel<Vehicule> model = new DefaultListModel<>();
        JList<Vehicule> list = new JList<>(model);

        list.setCellRenderer((jList, value, index, isSelected, cellHasFocus) -> {
            JLabel lab = new JLabel();
            String immat = value.getImmat() != null ? value.getImmat() : "-";
            TypeVehicule tv = value.getTypeVehicule();
            String marque = (tv != null && tv.getMarque() != null) ? tv.getMarque() : "-";
            String modele = (tv != null && tv.getModele() != null) ? tv.getModele() : "-";
            String txt = immat + " — " + marque + " " + modele;

            Client c = value.getClient();
            if (c != null) txt += "  (client: " + c.getPrenom() + " " + c.getNom() + ")";

            lab.setText(txt);
            lab.setOpaque(true);
            lab.setBorder(new EmptyBorder(8, 10, 8, 10));
            lab.setBackground(isSelected ? new Color(230, 240, 255) : Color.WHITE);
            return lab;
        });

        JScrollPane sp = new JScrollPane(list);

        JButton bOk = new JButton("Ajouter");
        JButton bCancel = new JButton("Annuler");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(bCancel);
        bottom.add(bOk);

        Runnable doSearch = () -> {
            model.clear();
            String q = tf.getText();
            List<Vehicule> res = (q == null || q.isBlank())
                    ? vehiculeDao.findAll()
                    : vehiculeDao.search(q);

            for (Vehicule v : res) model.addElement(v);
        };

        bSearch.addActionListener(e -> doSearch.run());
        tf.addActionListener(e -> doSearch.run());
        bCancel.addActionListener(e -> dlg.dispose());

        bOk.addActionListener(e -> {
            Vehicule selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(dlg, "Sélectionne un véhicule.");
                return;
            }

            // si déjà sur un autre client -> confirmation de réaffectation
            Client current = selected.getClient();
            if (current != null && editingClient != null && !current.getId().equals(editingClient.getId())) {
                int confirm = JOptionPane.showConfirmDialog(dlg,
                        "Ce véhicule appartient déjà à \"" + current.getPrenom() + " " + current.getNom() + "\".\n" +
                                "Le réaffecter à \"" + editingClient.getPrenom() + " " + editingClient.getNom() + "\" ?",
                        "Réaffectation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            try {
                selected.setClient(editingClient);
                vehiculeDao.update(selected);
                refreshVehiculesUI();
                dlg.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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



}
