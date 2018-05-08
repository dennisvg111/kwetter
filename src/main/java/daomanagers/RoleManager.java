package daomanagers;

import dao.DaoFacade;
import dao.IRoleDao;
import dao.JPA;

import javax.ejb.Stateless;
import javax.inject.Inject;

import domain.Role;

import java.util.List;

@Stateless
public class RoleManager {

    @JPA
    @Inject
    private IRoleDao dao;

    public Role AddRole(Role role)
    {
        return dao.Create(role);
    }

    public Role GetRole(long id)
    {
        return dao.Read(id);
    }

    public Role UpdateRole(Role role)
    {
        return dao.Update(role);
    }

    public void DeleteRole(long id)
    {
        dao.Delete(id);
    }

    public List<Role> All()
    {
        return dao.All();
    }
}
