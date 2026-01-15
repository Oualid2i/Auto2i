package auto2i.dao;

import auto2i.model.Client;
import auto2i.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClientDao {

    public List<Client> findAll() {
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery(
                    "SELECT c FROM Client c ORDER BY c.nom, c.prenom",
                    Client.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Client findById(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Client c) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Client update(Client c) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Client merged = em.merge(c);
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
            Client c = em.find(Client.class, id);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Client> search(String q) {
        if (q == null) q = "";
        q = q.trim();
        EntityManager em = JPAUtil.em();
        try {
            return em.createQuery(
                            "SELECT c FROM Client c " +
                                    "WHERE LOWER(c.nom) LIKE :q " +
                                    "   OR LOWER(c.prenom) LIKE :q " +
                                    "   OR LOWER(c.email) LIKE :q " +
                                    "ORDER BY c.nom, c.prenom",
                            Client.class
                    )
                    .setParameter("q", "%" + q.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
