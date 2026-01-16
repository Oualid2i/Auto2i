package auto2i.ui.intervention;

import auto2i.ui.components.CardPanel;
import auto2i.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

import static auto2i.ui.constants.UIConstants.*;
import static auto2i.ui.constants.UIIcons.*;

public class InterventionsPanel extends JPanel {

    private final Runnable onBack;
    private final Runnable onAdd;
    private final Consumer<String> onSearchImmat;

    private JTextField tfImmat;

    public InterventionsPanel(Runnable onBack, Runnable onAdd, Consumer<String> onSearchImmat) {
        this.onBack = onBack;
        this.onAdd = onAdd;
        this.onSearchImmat = onSearchImmat;

        setLayout(new BorderLayout());
        setBackground(BG_APP);
        setBorder(new EmptyBorder(18, 22, 18, 22));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    // ================= TOP =================
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

        JLabel title = new JLabel("Interventions", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 40f));
        title.setForeground(BLUE_MAIN);

        top.add(left, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        return top;
    }

    // ================= CONTENT =================
    private JComponent buildContent() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        wrap.add(buildSearchLine());
        wrap.add(Box.createVerticalStrut(16));
        wrap.add(buildCardsLine());
        wrap.add(Box.createVerticalGlue());

        return wrap;
    }

    private JComponent buildSearchLine() {
        JPanel line = new JPanel(new GridBagLayout());
        line.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 12);

        tfImmat = new JTextField();
        styleField(tfImmat);
        setFieldHeight(tfImmat, 52);
        tfImmat.setToolTipText("AA-123-BB");

        RoundedButton bSearch = new RoundedButton("Chercher", ORANGE_MAIN, Color.WHITE, 20);
        bSearch.setPreferredSize(new Dimension(260, 55));
        bSearch.setFont(bSearch.getFont().deriveFont(Font.BOLD, 18f));

        RoundedButton bAdd = new RoundedButton("+", ORANGE_MAIN, Color.WHITE, 20);
        bAdd.setPreferredSize(new Dimension(70, 55));
        bAdd.setFont(bAdd.getFont().deriveFont(Font.BOLD, 22f));

        bSearch.addActionListener(e -> doSearch());
        tfImmat.addActionListener(e -> doSearch());
        bAdd.addActionListener(e -> { if (onAdd != null) onAdd.run(); });

        c.gridx = 0; c.weightx = 1.0;
        line.add(tfImmat, c);

        c.gridx = 1; c.weightx = 0.0;
        line.add(bSearch, c);

        c.gridx = 2; c.weightx = 0.0; c.insets = new Insets(0, 0, 0, 0);
        line.add(bAdd, c);

        return line;
    }

    private void doSearch() {
        String immat = (tfImmat.getText() == null) ? "" : tfImmat.getText().trim();
        if (onSearchImmat != null) onSearchImmat.accept(immat);
    }

    private JComponent buildCardsLine() {
        JPanel line = new JPanel(new GridBagLayout());
        line.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;

        CardPanel reparationsCard = buildBigCard("Dernières réparations réalisées");
        CardPanel entretiensCard  = buildBigCard("Derniers entretiens réalisés");

        c.gridx = 0; c.weightx = 0.50; c.insets = new Insets(0, 0, 0, 12);
        line.add(reparationsCard, c);

        c.gridx = 1; c.weightx = 0.50; c.insets = new Insets(0, 12, 0, 0);
        line.add(entretiensCard, c);

        // hauteur proche de ta maquette
        line.setPreferredSize(new Dimension(10, 430));

        return line;
    }

    private CardPanel buildBigCard(String title) {
        CardPanel card = new CardPanel(26, BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 14, 14, 14));

        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 14f));
        t.setForeground(Color.DARK_GRAY);

        // zone vide (tu mettras tes rows plus tard)
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(Box.createVerticalGlue());

        card.add(t, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // ================= UI helpers =================
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
    }
}
