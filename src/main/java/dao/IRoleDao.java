package dao;

import domain.Role;

import java.util.List;

public interface IRoleDao {
    Role Create(Role role);

    Role Read(long id);

    Role Find(String name);

    Role Update(Role role);

    void Delete(long id);

    List<Role> All();
}
