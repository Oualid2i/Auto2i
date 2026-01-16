package auto2i.dao;

import auto2i.Enum.*;
import auto2i.model.TypeVehicule;
import auto2i.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TypeVehiculeDao {

    public static TypeVehicule findExisting(String marque, String modele, Energie energie, TypeBoite boite,
                                            Integer nbPortes, Integer nbPlaces, Integer puissance) {

        EntityManager em = JPAUtil.em();
        try {
            List<TypeVehicule> res = em.createQuery(
                            "SELECT t FROM TypeVehicule t " +
                                    "WHERE t.marque = :marque AND t.modele = :modele " +
                                    "AND t.energie = :energie AND t.boiteVitesse = :boite " +
                                    "AND t.nbPortes = :nbPortes AND t.nbPlaces = :nbPlaces AND t.puissance = :puissance",
                            TypeVehicule.class)
                    .setParameter("marque", marque)
                    .setParameter("modele", modele)
                    .setParameter("energie", energie)
                    .setParameter("boite", boite)
                    .setParameter("nbPortes", nbPortes)
                    .setParameter("nbPlaces", nbPlaces)
                    .setParameter("puissance", puissance)
                    .setMaxResults(1)
                    .getResultList();

            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    public static TypeVehicule save(TypeVehicule t) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            return t;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
