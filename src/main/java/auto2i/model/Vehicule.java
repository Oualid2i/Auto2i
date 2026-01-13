package auto2i.model;

public class Vehicule {
    private final String immat;
    private final String marque;
    private final String modele;
    private final String dateCirculation;
    private final String dernierKm;
    private final String energie;
    private final String boite;
    private final String nbPortes;
    private final String nbPlaces;
    private final String puissance;
    private final String client;

    public Vehicule(String immat, String marque, String modele,
                    String dateCirculation, String dernierKm,
                    String energie, String boite,
                    String nbPortes, String nbPlaces, String puissance,
                    String client) {
        this.immat = immat;
        this.marque = marque;
        this.modele = modele;
        this.dateCirculation = dateCirculation;
        this.dernierKm = dernierKm;
        this.energie = energie;
        this.boite = boite;
        this.nbPortes = nbPortes;
        this.nbPlaces = nbPlaces;
        this.puissance = puissance;
        this.client = client;
    }

    public String getImmat() { return immat; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public String getDateCirculation() { return dateCirculation; }
    public String getDernierKm() { return dernierKm; }
    public String getEnergie() { return energie; }
    public String getBoite() { return boite; }
    public String getNbPortes() { return nbPortes; }
    public String getNbPlaces() { return nbPlaces; }
    public String getPuissance() { return puissance; }
    public String getClient() { return client; }
}
