package daomanagers;

import dao.IUserDao;
import dao.JPA;
import domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UserManager {
    @JPA
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
        return dao.Read(id);
    }

    //Return user to immediately be able to log in
    public User AddUser(User user)
    {
        return dao.Create(user);
    }

    public List<User> GetFollowing(long id) {return dao.GetFollowing(id); }

    public boolean Follow(Long userId, Long followedUserId)
    {
        if (userId == followedUserId)
        {
            return false;
        }
        return dao.Follow(userId, followedUserId);
    }

    public boolean Unfollow(Long userId, Long followedUserId) { return dao.Unfollow(userId, followedUserId); }

    public User EditUser(User user) { return dao.Update(user); }

    public void RemoveUser(long id) { dao.Delete(id); }

    public List<User> GetFollowers(long id) { return dao.GetFollowers(id); }

    public List<User> GetUsers() {
        return dao.All();
    }
}
