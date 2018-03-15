package daomanagers;

import dao.IUserDao;
import domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserManager {
    @Inject
    private IUserDao dao;

    public UserManager() {
        super();
    }

    public User GetUser(String name)
    {
        return dao.FindUser(name);
    }

    public User GetUser(long id)
    {
        return dao.GetUser(id);
    }

    //Return user to immediately be able to log in
    public User AddUser(User user)
    {
        return dao.AddUser(user);
    }
}
