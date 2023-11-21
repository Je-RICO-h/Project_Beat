package com.szoftmern.beat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JpaCountryDAO implements AutoCloseable, EntityDAO {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.szoftmern.beat");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public void saveEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Country> getEntities() {
        TypedQuery query = entityManager.createQuery("SELECT C FROM Country C", Country.class);
        List<Country> countries = query.getResultList();

        return countries;
    }

    @Override
    public void updateEntity(Object t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
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
