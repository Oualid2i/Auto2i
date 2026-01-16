package auto2i.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(mappedBy = "client",  fetch = FetchType.LAZY)
    private List<Vehicule> vehicules = new ArrayList<>();

    // ✅ constructeur requis par JPA
    protected Client() {}



    public Client(String prenom, String nom, String email, String telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public void addVehicule(Vehicule v) {
        vehicules.add(v);
        v.setClient(this);
    }

    public void removeVehicule(Vehicule v) {
        vehicules.remove(v);
        v.setClient(null);
    }
}
