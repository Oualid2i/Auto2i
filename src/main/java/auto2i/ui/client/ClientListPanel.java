package auto2i.ui.client;

import auto2i.dao.ClientDao;
import auto2i.model.Client;
import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static auto2i.ui.constants.UIConstants.*;

public class ClientListPanel extends JPanel {

    private final Runnable onBack;
    private final Runnable onAdd;
    private final Consumer<Client> onShow;

    private final ClientDao clientDao = new ClientDao();

    private JTextField tfSearch;
    private JPanel listBox;

    public ClientListPanel(Runnable onBack, Runnable onAdd, Consumer<Client> onShow) {
        this.onBack = onBack;
        this.onAdd = onAdd;
        this.onShow = onShow;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        // Charge depuis la BDD
        reloadAll();
    }

    // rendu public pour pouvoir rafraîchir depuis l'extérieur
    public void reloadAll() {
        setClients(clientDao.findAll());
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

        JLabel title = new JLabel("Liste Clients", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }

    private JComponent buildContent() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        wrap.add(buildSearchLine());
        wrap.add(Box.createVerticalStrut(18));
        wrap.add(buildListCard());

        wrap.add(Box.createVerticalGlue());
        return wrap;
    }

    private JComponent buildSearchLine() {
        JPanel line = new JPanel(new GridBagLayout());
        line.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;

        tfSearch = new JTextField();
        styleField(tfSearch);
        setFieldHeight(tfSearch, 52);

        RoundedButton bSearch = new RoundedButton("Chercher", ORANGE_MAIN, Color.WHITE, 20);
        bSearch.setPreferredSize(new Dimension(260, 55));
        bSearch.setFont(bSearch.getFont().deriveFont(Font.BOLD, 18f));

        // action recherche BDD
        bSearch.addActionListener(e -> {
            String q = tfSearch.getText();
            if (q == null || q.isBlank()) {
                reloadAll();
            } else {
                setClients(clientDao.search(q));
            }
        });

        // Entrée -> chercher
        tfSearch.addActionListener(e -> bSearch.doClick());

        RoundedButton bAdd = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 20);
        bAdd.setPreferredSize(new Dimension(70, 55));
        bAdd.setFont(bAdd.getFont().deriveFont(Font.BOLD, 22f));
        bAdd.addActionListener(e -> { if (onAdd != null) onAdd.run(); });

        c.gridx = 0; c.weightx = 1.0; c.insets = new Insets(0, 0, 0, 18);
        line.add(tfSearch, c);

        c.gridx = 1; c.weightx = 0.0; c.insets = new Insets(0, 0, 0, 18);
        line.add(bSearch, c);

        c.gridx = 2; c.weightx = 0.0; c.insets = new Insets(0, 0, 0, 0);
        line.add(bAdd, c);

        return line;
    }

    private JComponent buildListCard() {
        CardPanel card = new CardPanel(26, BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        listBox = new JPanel();
        listBox.setOpaque(false);
        listBox.setLayout(new BoxLayout(listBox, BoxLayout.Y_AXIS));

        // scroll
        JScrollPane sp = new JScrollPane(listBox);
        sp.setBorder(null);
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);

        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    public void setClients(List<Client> clients) {
        listBox.removeAll();

        if (clients == null || clients.isEmpty()) {
            JLabel empty = new JLabel("Aucun client trouvé");
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listBox.add(empty);
        } else {
            for (Client cl : clients) {
                listBox.add(buildRow(cl));
                listBox.add(Box.createVerticalStrut(14));
            }
        }

        listBox.revalidate();
        listBox.repaint();
    }

    private JComponent buildRow(Client cl) {
        CardPanel row = new CardPanel(18, BORDER);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setPreferredSize(new Dimension(10, 70));
        row.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel grid = new JPanel(new GridLayout(1, 4, 18, 0));
        grid.setOpaque(false);

        grid.add(new JLabel(cl.getPrenom()));
        grid.add(new JLabel(cl.getNom()));
        grid.add(new JLabel(cl.getEmail()));
        grid.add(new JLabel(cl.getTelephone()));

        RoundedButton see = new RoundedButton("Voir", ORANGE_MAIN, Color.WHITE, 20);
        see.setPreferredSize(new Dimension(140, 46));
        see.setFont(see.getFont().deriveFont(Font.BOLD, 16f));
        see.addActionListener(e -> { if (onShow != null) onShow.accept(cl); });

        row.add(grid, BorderLayout.CENTER);
        row.add(see, BorderLayout.EAST);

        return row;
    }

    private void styleField(JTextField f) {
        f.setFont(f.getFont().deriveFont(Font.PLAIN, 14f));
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
    }

    private void setFieldHeight(JComponent comp, int h) {
        comp.setPreferredSize(new Dimension(10, h));
        comp.setMinimumSize(new Dimension(10, h));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
