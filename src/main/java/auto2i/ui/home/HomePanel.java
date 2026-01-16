package auto2i.ui.home;

import auto2i.dao.VehiculeDao;
import auto2i.model.TypeVehicule;
import auto2i.model.Vehicule;
import auto2i.ui.components.CardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Consumer;

public class HomePanel extends JPanel {

    private final VehiculeDao vehiculeDao = new VehiculeDao();
    private final Consumer<Vehicule> onOpenVehicule;

    private DefaultListModel<Vehicule> vehiculesModel;
    private JList<Vehicule> vehiculesList;

    public HomePanel(Consumer<Vehicule> onOpenVehicule) {
        this.onOpenVehicule = onOpenVehicule;

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

        JPanel lastVehicles = makeCard("Derniers véhicules enregistrés");
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        c.weighty = 0.55;
        center.add(lastVehicles, c);

        JPanel lastRepairs = makeCard("Dernières réparations");
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.45;
        center.add(lastRepairs, c);

        JPanel upcoming = makeCard("Entretiens à venir");
        c.gridx = 1; c.gridy = 1;
        center.add(upcoming, c);

        // Liste dynamique des derniers véhicules
        vehiculesModel = new DefaultListModel<>();
        vehiculesList = new JList<>(vehiculesModel);
        vehiculesList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        vehiculesList.setFont(vehiculesList.getFont().deriveFont(14f));
        vehiculesList.setSelectionBackground(new Color(255, 153, 51));
        vehiculesList.setSelectionForeground(Color.WHITE);

        // Renderer : immat — marque modele — km
        vehiculesList.setCellRenderer((list, v, index, isSelected, cellHasFocus) -> {
            JLabel lab = new JLabel();
            String immat = v.getImmat() != null ? v.getImmat() : "-";
            String km = (v.getDernierKilometrage() != null) ? (v.getDernierKilometrage() + " km") : "- km";

            TypeVehicule tv = v.getTypeVehicule();
            String marque = (tv != null && tv.getMarque() != null) ? tv.getMarque() : "-";
            String modele = (tv != null && tv.getModele() != null) ? tv.getModele() : "-";

            lab.setText(immat + " — " + marque + " " + modele + " — " + km);
            lab.setOpaque(true);
            lab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            lab.setBackground(isSelected ? new Color(255, 153, 51) : Color.WHITE);
            lab.setForeground(isSelected ? Color.WHITE : Color.DARK_GRAY);
            return lab;
        });

        // Ouverture fiche : double-clic ou Entrée
        Runnable openSelected = () -> {
            Vehicule selected = vehiculesList.getSelectedValue();
            if (selected == null || onOpenVehicule == null) return;

            // sécurité : recharger "fresh" (avec fetch) si tu veux être blindé
            Vehicule fresh = vehiculeDao.findById(selected.getId());
            onOpenVehicule.accept(fresh);
        };

        vehiculesList.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openSelected.run();
            }
        });

        vehiculesList.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) openSelected.run();
            }
        });

        JScrollPane sp = new JScrollPane(vehiculesList);
        lastVehicles.add(sp, BorderLayout.CENTER);

        // placeholders autres cards
        lastRepairs.add(new JScrollPane(new JList<>(new String[]{
                "— (à implémenter)"
        })), BorderLayout.CENTER);

        upcoming.add(new JScrollPane(new JList<>(new String[]{
                "— (à implémenter)"
        })), BorderLayout.CENTER);

        reload(); // charge dès l’ouverture
    }

    public void reload() {
        vehiculesModel.clear();
        List<Vehicule> latest = vehiculeDao.findLatest(8);
        if (latest == null || latest.isEmpty()) {
            // option : afficher une ligne "aucun"
        } else {
            for (Vehicule v : latest) vehiculesModel.addElement(v);
        }
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
}
