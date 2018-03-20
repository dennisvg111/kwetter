package dao;

import domain.Kweet;
import domain.User;

import java.util.List;

public interface IKweetDao {
    Kweet Read(long id);
    List<Kweet> GetKweetsFromUser(long id);
    List<Kweet> FindKweets(String search);
    Kweet Create(Kweet kweet);
    Kweet Update(Kweet kweet);
    void Delete(long id);
    List<Kweet> GetFeed(User user);


    List<Kweet> All();
}
