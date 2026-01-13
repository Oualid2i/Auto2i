package auto2i.ui;

import auto2i.model.Client;
import auto2i.ui.client.ClientListPanel;
import auto2i.ui.client.ClientNewPanel;
import auto2i.ui.client.ClientShowPanel;
import auto2i.ui.home.HomePanel;
import auto2i.ui.panels.PlaceholderPanel;
import auto2i.ui.panels.SidebarPanel;
import auto2i.ui.vehicule.VehiculeListPanel;
import auto2i.ui.vehicule.VehiculeNewPanel;
import auto2i.ui.vehicule.VehiculeShowPanel;

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

    public static final String PAGE_REPARATION    = "REPARATION";
    public static final String PAGE_ENTRETIEN     = "ENTRETIEN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);

    private SidebarPanel sidebar;

    private VehiculeShowPanel vehiculeShowPanel;
    private ClientShowPanel clientShowPanel;

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
            @Override public void goClientsNew() { showPage(PAGE_CLIENT_NEW); }
            @Override public void goReparations() { showPage(PAGE_REPARATION); }
            @Override public void goEntretiens() { showPage(PAGE_ENTRETIEN); }
        });

        body.add(sidebar, BorderLayout.WEST);

        // ===== HOME
        contentCards.add(new HomePanel(), PAGE_HOME);

        // ===== VEHICULES
        vehiculeShowPanel = new VehiculeShowPanel(() -> showPage(PAGE_VEHICULE_LIST));
        contentCards.add(vehiculeShowPanel, PAGE_VEHICULE_SHOW);

        contentCards.add(
                new VehiculeListPanel(
                        () -> showPage(PAGE_HOME),
                        () -> showPage(PAGE_VEHICULE_NEW),
                        v -> {
                            vehiculeShowPanel.setVehicule(v);
                            showPage(PAGE_VEHICULE_SHOW);
                        }
                ),
                PAGE_VEHICULE_LIST
        );

        contentCards.add(new VehiculeNewPanel(() -> showPage(PAGE_VEHICULE_LIST)), PAGE_VEHICULE_NEW);

        // ===== CLIENTS
        clientShowPanel = new ClientShowPanel(() -> showPage(PAGE_CLIENT_LIST));
        contentCards.add(clientShowPanel, PAGE_CLIENT_SHOW);

        contentCards.add(
                new ClientListPanel(
                        () -> showPage(PAGE_HOME),
                        () -> showPage(PAGE_CLIENT_NEW),
                        client -> {
                            clientShowPanel.setClient(client);
                            showPage(PAGE_CLIENT_SHOW);
                        }
                ),
                PAGE_CLIENT_LIST
        );

        contentCards.add(new ClientNewPanel(() -> showPage(PAGE_CLIENT_LIST)), PAGE_CLIENT_NEW);

        // ===== OTHERS
        contentCards.add(new PlaceholderPanel("Réparations"), PAGE_REPARATION);
        contentCards.add(new PlaceholderPanel("Entretiens"), PAGE_ENTRETIEN);

        body.add(contentCards, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        showPage(PAGE_HOME);
    }

    public void showPage(String pageKey) {
        cardLayout.show(contentCards, pageKey);

        // Pour garder la sidebar “Clients” active même quand on est en show
        if (PAGE_CLIENT_SHOW.equals(pageKey)) {
            sidebar.setActive(PAGE_CLIENT_LIST);
        } else if (PAGE_VEHICULE_SHOW.equals(pageKey)) {
            sidebar.setActive(PAGE_VEHICULE_LIST);
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
