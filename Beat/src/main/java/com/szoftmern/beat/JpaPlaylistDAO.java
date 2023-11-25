package com.szoftmern.beat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class JpaPlaylistDAO implements AutoCloseable, EntityDAO {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Playlist> getEntities() {
        TypedQuery query =
                entityManager.createQuery("SELECT P FROM Playlist P", Playlist.class);
        List<Playlist> playlists = query.getResultList();

        return playlists;
    }

    @Override
    public void updateEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteEntity(Object t) {
        t = entityManager.merge(t);
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
