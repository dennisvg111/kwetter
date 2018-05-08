package daomanagers;

import dao.IKweetDao;
import dao.JPA;
import domain.Kweet;
import domain.User;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class KweetManager implements Serializable {

    @JPA
    @Inject
    private IKweetDao dao;

    public KweetManager() {
        super();
    }

    public Kweet getKweet(long id) {
        return dao.Read(id);
    }

    public Kweet AddKweet(Kweet kweet) {
        if (kweet.getDate() == null)
        {
            kweet.setDate(new Date());
        }
        return dao.Create(kweet);
    }

    public List<Kweet> getKweets() {
        return dao.All();
    }

    public Kweet EditKweet(Kweet kweet) { return dao.Update(kweet); }

    public void RemoveKweet(long id) { dao.Delete(id); }

    public List<Kweet> GetFeed(User user) { return dao.GetFeed(user); }

    public List<Kweet> GetKweetsFromUser(long id) {
        return dao.GetKweetsFromUser(id);
    }

    public List<Kweet> Search(String query)
    {
        List<Kweet> kweets = dao.FindKweets(query);
        Collections.sort(kweets);
        return kweets;
    }
}
