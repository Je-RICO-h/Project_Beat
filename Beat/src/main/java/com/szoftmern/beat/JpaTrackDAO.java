package com.szoftmern.beat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaTrackDAO implements TrackDAO {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Track t) {
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
    public void updateEntity(Track a) {
        System.out.println("Meg nem mentunk semmit sehova...");
    }

    @Override
    public void deleteEntity(Track a) {
        System.out.println("Meg nem torlunk semmit sehonnan...");
    }

    @Override
    public void close() throws Exception {
        entityManagerFactory.close();
        entityManager.close();
    }
}
