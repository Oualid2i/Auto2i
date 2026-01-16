package auto2i.model;

import jakarta.persistence.*;
import auto2i.Enum.*;

@Entity
@Table(name = "type_vehicule")
public class TypeVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_vehicule")
    private Long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Energie energie;

    @Enumerated(EnumType.STRING)
    @Column(name = "boite_vitesse", nullable = false)
    private TypeBoite boiteVitesse;

    @Column(name = "nb_portes", nullable = false)
    private Integer nbPortes;

    @Column(name = "nb_places", nullable = false)
    private Integer nbPlaces;

    @Column(nullable = false)
    private Integer puissance;

    protected TypeVehicule() {
    }

    public Long getId() {
        return id;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public Energie getEnergie() {
        return energie;
    }

    public TypeBoite getBoiteVitesse() {
        return boiteVitesse;
    }

    public Integer getNbPortes() {
        return nbPortes;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public TypeVehicule(String marque, String modele, Energie energie, TypeBoite boite,
                        Integer nbPortes, Integer nbPlaces, Integer puissance) {
        this.marque = marque;
        this.modele = modele;
        this.energie = energie;
        this.boiteVitesse = boite;
        this.nbPortes = nbPortes;
        this.nbPlaces = nbPlaces;
        this.puissance = puissance;
    }

}


