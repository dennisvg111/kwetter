package dao;

import domain.Role;
import domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@JPA
@Stateless
public class JpaRoleDao extends DaoFacade<Role> implements IRoleDao {

    @PersistenceContext(unitName = "jpa-example")
    private EntityManager em;

    public JpaRoleDao() {
        super(Role.class);
    }

    public JpaRoleDao(EntityManager entityManager) {
        super(Role.class, entityManager);
        this.em = entityManager;
    }

    @Override
    public Role Find(String name) {
        try
        {
            Query query = em.createQuery("SELECT r FROM Role r WHERE UPPER(r.name) = UPPER(:name)");
            query.setParameter("name", name);
            Role role = (Role) query.getSingleResult();
            return role;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
