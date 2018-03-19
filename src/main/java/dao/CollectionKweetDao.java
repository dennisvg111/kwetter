package dao;


import domain.Kweet;
import domain.User;

import java.util.ArrayList;
import java.util.List;

public class CollectionKweetDao implements IKweetDao {
    private final ArrayList<Kweet> kweets;
    private static long currentKweetId = 0;

    public CollectionKweetDao() {

        kweets = new ArrayList<Kweet>();;
    }

    @Override
    public Kweet Read(long id)
    {
        for (Kweet kweet : kweets) {
            if (kweet.getId() == id) {
                return kweet;
            }
        }
        return null;
    }

    public ArrayList<Kweet> GetKweetsFromUser(User user) {
        ArrayList<Kweet> foundKweets = new ArrayList<Kweet>();
        for (Kweet kweet : kweets) {
            if (kweet.getUser().getId() == user.getId()) {
                foundKweets.add(kweet);
            }
        }
        return foundKweets;
    }

    public ArrayList<Kweet> FindKweets(String search) {
        return null;
    }

    @Override
    public Kweet Create(Kweet kweet) {
        kweet.setId(currentKweetId);
        currentKweetId++;
        kweets.add(kweet);
        return kweet;
    }

    @Override
    public Kweet Update(Kweet kweet) {
        return null;
    }

    @Override
    public void Delete(long id) {
        kweets.remove(Read(id));
    }

    @Override
    public List<Kweet> GetFeed(User user) {
        return null;
    }

    @Override
    public List<Kweet> All() {
        return kweets;
    }
}
