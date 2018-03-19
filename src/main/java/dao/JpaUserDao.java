package dao;

import domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@JPA
@Stateless
public class JpaUserDao extends DaoFacade<User> implements IUserDao {

    @PersistenceContext(unitName = "jpa-example")
    private EntityManager em;

    public JpaUserDao() {
        super(User.class);
    }

    @Override
    public User FindUser(String name) {
        try
        {
            Query query = em.createQuery("SELECT u FROM User u WHERE UPPER(u.name) = UPPER(:name)");
            query.setParameter("name", name);
            User user = (User) query.getSingleResult();
            return user;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<User> GetFollowing(long id) {
        try
        {
            Query query = em.createQuery("SELECT u.following FROM User u WHERE u.id = :id");
            query.setParameter("id", id);
            return query.getResultList();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<User> GetFollowers(long id) {
        try
        {
            Query query = em.createQuery("SELECT u.followers FROM User u WHERE u.id = :id");
            query.setParameter("id", id);
            return query.getResultList();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public boolean Follow(Long userId, Long followedUserId) {
        try
        {
            User user = Read(userId);
            User otherUser = Read(followedUserId);

            if (user.IsFollowing(otherUser))
            {
                return false;
            }

            user.addFollowing(otherUser);

            Update(user);
            Update(otherUser);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean Unfollow(Long userId, Long followedUserId) {
        try
        {
            User user = Read(userId);
            User otherUser = Read(followedUserId);

            if (!user.IsFollowing(otherUser))
            {
                return false;
            }

            user.RemoveFollowing(otherUser);

            Update(user);
            Update(otherUser);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
