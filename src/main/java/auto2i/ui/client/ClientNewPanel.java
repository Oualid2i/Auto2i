package auto2i.ui.client;

import auto2i.dao.ClientDao;
import auto2i.model.Client;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class ClientNewPanel extends JPanel {

    private final Runnable onBack;
    private final ClientDao clientDao = new ClientDao();
    private Client editingClient = null; // null = création, sinon édition


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

        JTextField tfSearchVehicule = new JTextField();
        tfSearchVehicule.setFont(tfSearchVehicule.getFont().deriveFont(Font.PLAIN, 14f));
        tfSearchVehicule.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        tfSearchVehicule.setPreferredSize(new Dimension(300, FIELD_H));

        RoundedButton btnAdd = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 18);
        btnAdd.setPreferredSize(new Dimension(55, FIELD_H));
        btnAdd.setMinimumSize(new Dimension(55, FIELD_H));
        btnAdd.setMaximumSize(new Dimension(55, FIELD_H));
        btnAdd.setHorizontalAlignment(SwingConstants.CENTER);

        btnAdd.addActionListener(e -> {
            // plus tard : ouvrir une popup / filtrer / lier véhicule
            JOptionPane.showMessageDialog(this,
                    "Recherche véhicule : " + tfSearchVehicule.getText());
        });

        line.add(tfSearchVehicule, BorderLayout.CENTER);
        line.add(btnAdd, BorderLayout.EAST);

        return line;
    }

    private JPanel buildVehiculesBox() {
        JPanel col = columnPanel();

        JLabel l = labelLeft("Véhicules");
        col.add(l);
        col.add(Box.createVerticalStrut(6));

        col.add(buildVehiculeSearchBar());
        col.add(Box.createVerticalStrut(12));

        CardPanel card = new CardPanel(26, BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Mock rows
        card.add(mockVehiculeRow("AJ-433-BD", "Peugeot", "206", "Essence"));
        card.add(Box.createVerticalStrut(12));
        card.add(mockVehiculeRow("GN-432-CL", "Citroen", "C5", "Diesel"));

        col.add(card);
        col.add(Box.createVerticalGlue());

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
    }

    public void editClient(Client c) {
        if (c == null) return;

        editingClient = c;

        tfPrenom.setText(c.getPrenom());
        tfNom.setText(c.getNom());
        tfEmail.setText(c.getEmail());
        tfTel.setText(c.getTelephone() == null ? "" : c.getTelephone());
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



}
