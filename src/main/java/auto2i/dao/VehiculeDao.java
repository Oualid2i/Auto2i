package auto2i.dao;

import auto2i.model.Vehicule;
import auto2i.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VehiculeDao {

    public List<Vehicule> findAll() {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery(
                    "SELECT v FROM Vehicule v " +
                            "LEFT JOIN FETCH v.client " +
                            "LEFT JOIN FETCH v.typeVehicule " +
                            "ORDER BY v.immat", Vehicule.class
            ).getResultList();
        } finally {
            em.close();
        }
    }


    public Vehicule findById(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery(
                            "SELECT v FROM Vehicule v " +
                                    "LEFT JOIN FETCH v.client " +
                                    "LEFT JOIN FETCH v.typeVehicule " +
                                    "WHERE v.id = :id",
                            Vehicule.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }


    public Vehicule findByImmat(String immat) {
        if (immat == null || immat.isBlank()) return null;

        EntityManager em = JPAUtil.em();
        try {
            List<Vehicule> res = em.createQuery(
                            "SELECT v FROM Vehicule v WHERE v.immat = :immat",
                            Vehicule.class
                    )
                    .setParameter("immat", immat.trim())
                    .setMaxResults(1)
                    .getResultList();

            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    public List<Vehicule> search(String q) {
        EntityManager em = JPAUtil.em();
        try {
            String query = (q == null) ? "" : q.trim().toLowerCase();
            if (query.isEmpty()) return findAll();

            return em.createQuery(
                            "SELECT v FROM Vehicule v " +
                                    "LEFT JOIN FETCH v.client " +
                                    "LEFT JOIN FETCH v.typeVehicule tv " +
                                    "WHERE lower(v.immat) LIKE :q " +
                                    "   OR lower(tv.marque) LIKE :q " +
                                    "   OR lower(tv.modele) LIKE :q " +
                                    "   OR lower(str(tv.energie)) LIKE :q " +   // ✅ enum compatible
                                    "ORDER BY v.immat",
                            Vehicule.class
                    )
                    .setParameter("q", "%" + query + "%")
                    .getResultList();

        } finally {
            em.close();
        }
    }


    public void save(Vehicule v) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            em.persist(v);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Vehicule update(Vehicule v) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Vehicule merged = em.merge(v);
            em.getTransaction().commit();
            return merged;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Vehicule v = em.find(Vehicule.class, id);
            if (v != null) em.remove(v);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Vehicule> findByClientId(Long clientId) {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery(
                    "SELECT v FROM Vehicule v " +
                            "LEFT JOIN FETCH v.typeVehicule tv " +
                            "LEFT JOIN FETCH v.client c " +
                            "WHERE c.id = :id " +
                            "ORDER BY v.immat",
                    Vehicule.class
            ).setParameter("id", clientId).getResultList();
        } finally {
            em.close();
        }
    }


}
