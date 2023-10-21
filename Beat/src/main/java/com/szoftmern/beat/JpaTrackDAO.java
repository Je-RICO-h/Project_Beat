package com.szoftmern.beat;

import jakarta.persistence.*;
import java.util.List;

public class JpaTrackDAO implements AutoCloseable, EntityDAO {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Object t) {
        System.out.println("Meg nem mentunk semmit sehova...");
    }

    @Override
    public List<Track> getEntities() {
        TypedQuery query =
                entityManager.createQuery("SELECT T FROM Track T", Track.class);
        List<Track> tracks = query.getResultList();

        return tracks;
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
