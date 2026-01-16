package auto2i.ui;

import auto2i.model.Client;
import auto2i.ui.client.ClientListPanel;
import auto2i.ui.client.ClientNewPanel;
import auto2i.ui.client.ClientShowPanel;
import auto2i.ui.home.HomePanel;
import auto2i.ui.intervention.InterventionsPanel;
import auto2i.ui.panels.PlaceholderPanel;
import auto2i.ui.panels.SidebarPanel;
import auto2i.ui.vehicule.VehiculeListPanel;
import auto2i.ui.vehicule.VehiculeNewPanel;
import auto2i.ui.vehicule.VehiculeShowPanel;
import auto2i.ui.home.HomePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String PAGE_HOME          = "HOME";
    public static final String PAGE_VEHICULE_LIST = "VEHICULE_LIST";
    public static final String PAGE_VEHICULE_NEW  = "VEHICULE_NEW";
    public static final String PAGE_VEHICULE_SHOW = "VEHICULE_SHOW";

    public static final String PAGE_CLIENT_LIST   = "CLIENT_LIST";
    public static final String PAGE_CLIENT_NEW    = "CLIENT_NEW";
    public static final String PAGE_CLIENT_SHOW   = "CLIENT_SHOW";

    public static final String PAGE_INTERVENTIONS = "INTERVENTIONS";
    public static final String PAGE_REPARATION    = "REPARATION";
    public static final String PAGE_ENTRETIEN     = "ENTRETIEN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);

    private SidebarPanel sidebar;

    private HomePanel homePanel;

    private VehiculeShowPanel vehiculeShowPanel;
    private ClientShowPanel clientShowPanel;

    private ClientListPanel clientListPanel;
    private ClientNewPanel clientNewPanel;
    private VehiculeListPanel vehiculeListPanel;
    private VehiculeNewPanel vehiculeNewPanel;

    public MainFrame() {
        super("Auto2i - Gestion des automobiles");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new AppHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());

        sidebar = new SidebarPanel(new SidebarPanel.NavigationListener() {
            @Override public void goHome() { showPage(PAGE_HOME); }
            @Override public void goVehiculesList() { showPage(PAGE_VEHICULE_LIST); }
            @Override public void goVehiculesNew() { showPage(PAGE_VEHICULE_NEW); }
            @Override public void goClientsList() { showPage(PAGE_CLIENT_LIST); }
            @Override public void goClientsNew() { goClientNew(); }
            @Override public void goInterventions() { showPage(PAGE_INTERVENTIONS); }
            @Override public void goReparations()   { showPage(PAGE_REPARATION); }
            @Override public void goEntretiens()    { showPage(PAGE_ENTRETIEN); }

        });

        body.add(sidebar, BorderLayout.WEST);

        // ===== HOME
        homePanel = new HomePanel(v -> {
            vehiculeShowPanel.setVehicule(v);
            showPage(PAGE_VEHICULE_SHOW);
        });
        contentCards.add(homePanel, PAGE_HOME);


        // ===== INTERVENTIONS
        InterventionsPanel interPanel = new InterventionsPanel(
                () -> showPage(PAGE_HOME),
                () -> System.out.println("TODO: ajout intervention"),
                immat -> System.out.println("TODO: search immat = " + immat)
        );
        contentCards.add(interPanel, "INTERVENTIONS");

        contentCards.add(new PlaceholderPanel("Réparations"), PAGE_REPARATION);
        contentCards.add(new PlaceholderPanel("Entretiens"), PAGE_ENTRETIEN);

        // ===== VEHICULES
        vehiculeShowPanel = new VehiculeShowPanel(
                () -> {
                    showPage(PAGE_VEHICULE_LIST);
                    if (vehiculeListPanel != null) vehiculeListPanel.reloadAll();
                },
                vehToEdit -> {
                    vehiculeNewPanel.editVehicule(vehToEdit); // à ajouter comme pour client
                    showPage(PAGE_VEHICULE_NEW);
                }
        );
        contentCards.add(vehiculeShowPanel, PAGE_VEHICULE_SHOW);


        vehiculeListPanel =
                new VehiculeListPanel(
                        () -> showPage(PAGE_HOME),
                        () -> showPage(PAGE_VEHICULE_NEW),
                        v -> {
                            vehiculeShowPanel.setVehicule(v);
                            showPage(PAGE_VEHICULE_SHOW);
                        }
                );
        contentCards.add(vehiculeListPanel, PAGE_VEHICULE_LIST);


        vehiculeNewPanel = new VehiculeNewPanel(() -> {
            showPage(PAGE_VEHICULE_LIST);
            if (vehiculeListPanel != null) vehiculeListPanel.reloadAll();
        });
        contentCards.add(vehiculeNewPanel, PAGE_VEHICULE_NEW);

// ===== CLIENTS
        clientShowPanel = new ClientShowPanel(
                () -> {
                    showPage(PAGE_CLIENT_LIST);
                    clientListPanel.reloadAll();
                },
                clientToEdit -> {
                    clientNewPanel.editClient(clientToEdit);
                    showPage(PAGE_CLIENT_NEW);
                },
                veh -> {
                    vehiculeShowPanel.setVehicule(veh);
                    showPage(PAGE_VEHICULE_SHOW);
                }
        );




        contentCards.add(clientShowPanel, PAGE_CLIENT_SHOW);

        clientListPanel =
                new ClientListPanel(
                        () -> showPage(PAGE_HOME),
                        () -> goClientNew(),
                        client -> {
                            clientShowPanel.setClient(client);
                            showPage(PAGE_CLIENT_SHOW);
                        }
                );
        contentCards.add(clientListPanel, PAGE_CLIENT_LIST);

        clientNewPanel = new ClientNewPanel(() -> {
            // retour liste + refresh
            showPage(PAGE_CLIENT_LIST);
            clientListPanel.reloadAll();

        });
        contentCards.add(clientNewPanel, PAGE_CLIENT_NEW);

        body.add(contentCards, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        showPage(PAGE_HOME);
    }

    private void goClientNew() {
        if (clientNewPanel != null) clientNewPanel.resetForm(); // uniquement création
        showPage(PAGE_CLIENT_NEW);
    }

    public void showPage(String pageKey) {

        if (PAGE_HOME.equals(pageKey) && homePanel != null) {
            homePanel.reload();
        }

        if (PAGE_CLIENT_LIST.equals(pageKey) && clientListPanel != null) {
            clientListPanel.reloadAll();
        }

        if (PAGE_VEHICULE_LIST.equals(pageKey) && vehiculeListPanel != null) {
            vehiculeListPanel.reloadAll();
        }

        cardLayout.show(contentCards, pageKey);

        if (PAGE_CLIENT_SHOW.equals(pageKey)) {
            sidebar.setActive(PAGE_CLIENT_LIST);
        } else if (PAGE_VEHICULE_SHOW.equals(pageKey)) {
            sidebar.setActive(PAGE_VEHICULE_LIST);
        } else {
            sidebar.setActive(pageKey);
        }

        if (PAGE_REPARATION.equals(pageKey) || PAGE_ENTRETIEN.equals(pageKey)) {
            sidebar.setActive(PAGE_INTERVENTIONS);
        } else {
            sidebar.setActive(pageKey);
        }

    }





    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
