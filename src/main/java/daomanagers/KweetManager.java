package daomanagers;

import dao.IKweetDao;
import dao.JPA;
import domain.Kweet;
import domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class KweetManager {

    @JPA
    @Inject
    private IKweetDao dao;

    public KweetManager() {
        super();
    }

    public Kweet getKweet(long id) {
        return dao.Read(id);
    }

    public Kweet AddKweet(Kweet kweet) { return dao.Create(kweet); }

    public List<Kweet> getKweets() {
        return dao.All();
    }

    public Kweet EditKweet(Kweet kweet) { return dao.Update(kweet); }

    public void RemoveKweet(long id) { dao.Delete(id); }

    public List<Kweet> GetFeed(User user) { return dao.GetFeed(user); }

    public List<Kweet> GetKweetsFromUser(User user) {
        return dao.GetKweetsFromUser(user);
    }

    public List<Kweet> Search(String query)
    {
        return dao.FindKweets(query);
    }
}
