package daomanagers;

import dao.IKweetDao;
import domain.Kweet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class KweetManager {
    @Inject
    private IKweetDao dao;

    public KweetManager() {
        super();
    }

    public Kweet getKweet(long id) {
        return dao.GetKweetById(id);
    }

    public Kweet AddKweet(Kweet kweet) { return dao.AddKweet(kweet); }

    public List<Kweet> getKweets() {
        return dao.GetKweets();
    }
}
