package com.szoftmern.beat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class JpaArtistDAO implements EntityDAO {
    public final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    public final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Object a) {
        System.out.println("Meg nem mentunk semmit sehova...");
    }

    @Override
    public List<Artist> getEntities() {
        TypedQuery query =
                entityManager.createQuery("SELECT A FROM Artist A", Artist.class);
        List<Artist> artists = query.getResultList();

        return artists;
    }

    @Override
    public void updateEntity(Object a) {
        System.out.println("Meg nem mentunk semmit sehova...");
    }

    @Override
    public void deleteEntity(Object a) {
        System.out.println("Meg nem torlunk semmit sehonnan...");
    }

    @Override
    public void close() {
        entityManagerFactory.close();
        entityManager.close();
    }
}
