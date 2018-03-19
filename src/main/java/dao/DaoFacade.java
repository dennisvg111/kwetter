package dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class DaoFacade<T> {
    private Class<T> entityClass;

    @PersistenceContext(unitName = "jpa-example")
    private EntityManager entityManager;

    public DaoFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T Create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T Update(T entity) {
        return entityManager.merge(entity);
    }

    public T Read(long id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> All() {
        return entityManager.createQuery("SELECT x FROM " + this.entityClass.getName() + " x")
                .getResultList();
    }

    public void Delete(T entity) {
        entityManager.remove(entity);
    }
}
