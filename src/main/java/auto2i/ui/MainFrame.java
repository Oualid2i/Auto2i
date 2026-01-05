package auto2i.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String PAGE_HOME = "HOME";
    public static final String PAGE_VEHICULE_LIST = "VEHICULE_LIST";
    public static final String PAGE_VEHICULE_NEW  = "VEHICULE_NEW";
    public static final String PAGE_CLIENT_LIST   = "CLIENT_LIST";
    public static final String PAGE_CLIENT_NEW    = "CLIENT_NEW";
    public static final String PAGE_REPARATION    = "REPARATION";
    public static final String PAGE_ENTRETIEN     = "ENTRETIEN";


    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);

    public MainFrame() {
        super("Auto2i - Gestion des automobiles");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        // Layout global : header (NORTH) + body (CENTER)
        setLayout(new BorderLayout());

        // Header
        add(new AppHeader(), BorderLayout.NORTH);

        // Body = sidebar (WEST) + content (CENTER)
        JPanel body = new JPanel(new BorderLayout());

        SidebarPanel sidebar = new SidebarPanel(new SidebarPanel.NavigationListener() {
            @Override
            public void goHome() {
                showPage(PAGE_HOME);
            }

            @Override
            public void goVehiculesList() {
                showPage(PAGE_VEHICULE_LIST);
            }

            @Override
            public void goVehiculesNew() {
                showPage(PAGE_VEHICULE_NEW);
            }

            @Override
            public void goClientsList() {
                showPage(PAGE_CLIENT_LIST);
            }

            @Override
            public void goClientsNew() {
                showPage(PAGE_CLIENT_NEW);
            }

            @Override
            public void goReparations() {
                showPage(PAGE_REPARATION);
            }

            @Override
            public void goEntretiens() {
                showPage(PAGE_ENTRETIEN);
            }
        });

        body.add(sidebar, BorderLayout.WEST);

        // Pages (cards)
        contentCards.add(new HomePanel(), PAGE_HOME);
        contentCards.add(new VehiculeListPanel(), PAGE_VEHICULE_LIST);
        contentCards.add(new PlaceholderPanel("Ajout véhicule"), PAGE_VEHICULE_NEW);
        contentCards.add(new PlaceholderPanel("Liste clients"), PAGE_CLIENT_LIST);
        contentCards.add(new PlaceholderPanel("Ajout client"), PAGE_CLIENT_NEW);
        contentCards.add(new PlaceholderPanel("Réparations"), PAGE_REPARATION);
        contentCards.add(new PlaceholderPanel("Entretiens"), PAGE_ENTRETIEN);

        body.add(contentCards, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        showPage(PAGE_HOME);
    }

    private void showPage(String pageKey) {
        cardLayout.show(contentCards, pageKey);
    }

    public static void main(String[] args) {
        // Look and Feel système (optionnel mais agréable)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
