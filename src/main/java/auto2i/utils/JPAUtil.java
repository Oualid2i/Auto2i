package auto2i.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {
    private static EntityManagerFactory emf;

    private JPAUtil() {}

    private static EntityManagerFactory emf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("Auto2iPU");
        }
        return emf;
    }

    public static EntityManager em() {
        return emf().createEntityManager();
    }

    public static void close() {
        if (emf != null) emf.close();
    }
}

