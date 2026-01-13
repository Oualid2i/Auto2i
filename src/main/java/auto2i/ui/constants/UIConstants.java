package auto2i.ui.constants;

import java.awt.Color;

public class UIConstants {

    // Palette Auto2i
    public static final Color BG_APP      = new Color(245, 245, 245);
    public static final Color BG_SIDEBAR  = new Color(230, 230, 230);
    public static final Color BLUE_MAIN   = new Color(45, 85, 140);
    public static final Color ORANGE_MAIN = new Color(255, 153, 51);
    public static final Color BORDER      = new Color(210, 210, 210);
    public static final Color YELLOW_ACTIVE = new Color(255, 220, 120);
    public static final Color BTN_MAIN_INACTIVE = new Color(215, 215, 215);
    public static final Color BTN_SUB_INACTIVE  = new Color(230, 230, 230);



    // ====== Dimensions formulaire (VehiculeNew) ======
    public static final int FIELD_H = 42;

    public static final int COL1_W = 320;  // colonne gauche (immat/date/km/client)
    public static final int COL2_W = 280;  // colonne milieu (marque/modèle/énergie/boite)
    public static final int COL3_W = 70;  // colonne droite (portes/places/puissance)

    public static final int ICON_BTN_W = 46; // bouton calendrier / loupe

    public static final int GAP_COL = 28;    // espace entre colonnes
    public static final int GAP_ROW = 18;    // espace entre lignes




    private UIConstants() {
        // empêche l'instanciation (classe utilitaire)
    }
}
