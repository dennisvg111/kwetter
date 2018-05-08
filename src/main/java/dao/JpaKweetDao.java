package dao;

import daomanagers.UserManager;
import domain.Kweet;
import domain.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JPA
@Stateless
public class JpaKweetDao extends DaoFacade<Kweet> implements IKweetDao {

    @PersistenceContext(unitName = "jpa-example")
    private EntityManager em;

    @Inject
    private UserManager userManager;

    public JpaKweetDao() {
        super(Kweet.class);
    }

    public JpaKweetDao(EntityManager entityManager) {
        super(Kweet.class, entityManager);
        this.em = entityManager;
    }

    @Override
    public List<Kweet> GetKweetsFromUser(long id) {
        try
        {
            Query query = em.createQuery("SELECT k FROM Kweet k WHERE k.user.id = :id ORDER BY k.date");
            query.setParameter("id", id);
            List<Kweet> kweets = query.getResultList();

            Collections.sort(kweets);
            return kweets;
        }
        catch (Exception exception)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Kweet> FindKweets(String search) {
        try
        {
            Query query = em.createQuery("SELECT k FROM Kweet k WHERE UPPER(k.content) LIKE UPPER(:search) ORDER BY k.date");
            query.setParameter("search", "%" + search + "%");
            return query.getResultList();
        }
        catch (Exception exception)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Kweet> GetFeed(User user) {
        List<Kweet> feed = new ArrayList<>();
        for (User sourceUser : userManager.GetFollowing(user.getId())) {
            try
            {
                //No need to user parameters as it's not user inputted
                Query query = em.createQuery("SELECT k FROM Kweet k WHERE k.user.id = " + sourceUser.getId() + " ORDER BY k.date");
                feed.addAll(query.getResultList());
            } catch (Exception exception) {
            }
        }

        feed.addAll(GetKweetsFromUser(user.getId()));
        //Kweet is comparable on date
        Collections.sort(feed);
        return feed;
    }
}
