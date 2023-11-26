package com.szoftmern.beat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JpaPlaylistTracksDAO implements AutoCloseable, EntityDAO {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<PlaylistTracks> getEntities() {
        TypedQuery query =
                entityManager.createQuery("SELECT PT FROM PlaylistTracks PT", PlaylistTracks.class);
        List<PlaylistTracks> playlistTracks = query.getResultList();

        return playlistTracks;
    }

    @Override
    public void updateEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.remove(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public void close() {
        entityManagerFactory.close();
        entityManager.close();
    }
}
