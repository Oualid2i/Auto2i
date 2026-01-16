package auto2i.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule")
    private Long id;

    @Column(name = "immatriculation", nullable = false, unique = true)
    private String immat;

    @Column(name = "date_mise_en_circ", nullable = false)
    private LocalDate dateMiseEnCirculation;

    @Column(name = "dernier_kilometrage", nullable = false)
    private Integer dernierKilometrage;

    // lien vers Client (colonne id_client dans vehicule)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    // lien vers TypeVehicule (colonne id_type_vehicule dans vehicule)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_vehicule", nullable = false)
    private TypeVehicule typeVehicule;

    protected Vehicule() {}

    public Vehicule(String immat, LocalDate dateMiseEnCirculation, Integer dernierKilometrage,
                    Client client, TypeVehicule typeVehicule) {
        this.immat = immat;
        this.dateMiseEnCirculation = dateMiseEnCirculation;
        this.dernierKilometrage = dernierKilometrage;
        this.client = client;
        this.typeVehicule = typeVehicule;
    }

    public Long getId() { return id; }
    public String getImmat() { return immat; }
    public LocalDate getDateMiseEnCirculation() { return dateMiseEnCirculation; }
    public Integer getDernierKilometrage() { return dernierKilometrage; }

    public Client getClient() { return client; }
    public TypeVehicule getTypeVehicule() { return typeVehicule; }

    public void setClient(Client client) { this.client = client; }
    public void setTypeVehicule(TypeVehicule typeVehicule) { this.typeVehicule = typeVehicule; }

    public void setImmat(String immat) {}

    public void setDateMiseEnCirculation(LocalDate date) {
    }

    public void setDernierKilometrage(int km) {
    }
}
