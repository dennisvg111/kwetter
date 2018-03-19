package dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.Collection;
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

    //this method first tries to replace all non-null fields of entity into the existing version
    //if that does not work fall back to overwriting the entity
    public T Update(T entity) {

        //based on https://stackoverflow.com/a/2146140/5022761
        Field idField = null;
        try {
            idField = entity.getClass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            return entityManager.merge(entity);
        }
        idField.setAccessible(true);
        long id;
        try {
            id = idField.getLong(entity);
        } catch (IllegalAccessException e) {
            return entityManager.merge(entity);
        }

        T updated = Read(id);
        boolean updateOld = false;

        for (Field f : entity.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.get(entity) != null) {
                    if (!(f.get(entity) instanceof Collection) || ((Collection)f.get(entity)).size() > 0)
                    {
                        f.set(updated, f.get(entity));
                    }
                }
            } catch (IllegalAccessException e) {
                updateOld = true;
                break;
            }
            f.setAccessible(false);
        }

        if (updateOld)
        {
            return entityManager.merge(entity);
        }

        return entityManager.merge(updated);
    }

    public T Read(long id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> All() {
        return entityManager.createQuery("SELECT x FROM " + this.entityClass.getName() + " x")
                .getResultList();
    }

    public void Delete(long id) {
        entityManager.remove(Read(id));
    }
}
