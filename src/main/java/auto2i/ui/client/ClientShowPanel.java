package auto2i.ui.client;

import auto2i.dao.ClientDao;
import auto2i.model.Client;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

import static auto2i.ui.constants.UIConstants.*;

public class ClientShowPanel extends JPanel {

    private final Runnable onBack;
    private final Consumer<Client> onEdit; // ✅ callback modifier

    private final ClientDao clientDao = new ClientDao();
    private Client client; // ✅ client affiché

    private JTextField tfPrenom, tfNom, tfEmail, tfTel;

    public ClientShowPanel(Runnable onBack, Consumer<Client> onEdit) {
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

        JLabel title = new JLabel("Fiche client", SwingConstants.CENTER);
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
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;

        JPanel left = buildLeftFields();
        JPanel right = buildVehiculesCard();
        JPanel actions = buildButtons();

        c.gridx = 0; c.weightx = 0.45;
        c.insets = new Insets(10, 10, 10, 25);
        wrap.add(left, c);

        c.gridx = 1; c.weightx = 0.55;
        c.insets = new Insets(10, 0, 10, 10);
        wrap.add(right, c);

        // Actions en bas à droite
        c.gridy = 1;
        c.gridx = 1;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 0, 0, 10);
        wrap.add(actions, c);

        return wrap;
    }

    private JPanel buildLeftFields() {
        JPanel col = columnPanel();

        tfPrenom = makeReadField();
        col.add(labeledField("Prénom", tfPrenom));

        tfNom = makeReadField();
        col.add(labeledField("Nom", tfNom));

        tfEmail = makeReadField();
        col.add(labeledField("Email", tfEmail));

        tfTel = makeReadField();
        col.add(labeledField("Téléphone", tfTel));

        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildVehiculesCard() {
        JPanel col = columnPanel();

        JLabel l = labelLeft("Véhicules");
        col.add(l);
        col.add(Box.createVerticalStrut(10));

        CardPanel card = new CardPanel(26, BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Mock rows (plus tard: charger depuis client.getVehicules())
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

        RoundedButton see = new RoundedButton("Voir", ORANGE_MAIN, Color.WHITE, 20);
        see.setPreferredSize(new Dimension(120, 46));
        see.setFont(see.getFont().deriveFont(Font.BOLD, 16f));

        row.add(grid, BorderLayout.CENTER);
        row.add(see, BorderLayout.EAST);
        return row;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        RoundedButton modif = new RoundedButton("Modifier", ORANGE_MAIN, Color.WHITE, 20);
        RoundedButton del = new RoundedButton("Effacer", ORANGE_MAIN, Color.WHITE, 20);

        Dimension btn = new Dimension(320, 60);
        for (RoundedButton b : new RoundedButton[]{modif, del}) {
            b.setPreferredSize(btn);
            b.setMinimumSize(btn);
            b.setMaximumSize(btn);
            b.setFont(b.getFont().deriveFont(Font.BOLD, 20f));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        // ✅ bouton Modifier -> callback
        modif.addActionListener(e -> {
            if (client != null && onEdit != null) {
                onEdit.accept(client);
            }
        });

        // ✅ Liaison suppression BDD
        del.addActionListener(e -> onDelete());

        p.add(modif);
        p.add(Box.createVerticalStrut(18));
        p.add(del);

        return p;
    }

    private void onDelete() {
        if (client == null || client.getId() == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer le client \"" + client.getPrenom() + " " + client.getNom() + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            clientDao.delete(client.getId());

            JOptionPane.showMessageDialog(this,
                    "Client supprimé ✅",
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

    // ===== helpers gauche
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
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private JPanel labeledField(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = labelLeft(label);

        field.setPreferredSize(new Dimension(10, FIELD_H));
        field.setMinimumSize(new Dimension(10, FIELD_H));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_H));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    // ===== API
    public void setClient(Client c) {
        if (c == null) return;

        this.client = c;

        tfPrenom.setText(c.getPrenom());
        tfNom.setText(c.getNom());
        tfEmail.setText(c.getEmail());
        tfTel.setText(c.getTelephone());
    }
}
