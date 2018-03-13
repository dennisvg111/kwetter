package dao;

import domain.Kweet;
import domain.User;

import java.util.ArrayList;
import java.util.List;

public interface IKweetDao {
    Kweet GetKweetById(long id);
    ArrayList<Kweet> GetKweetsFromUser(User user);
    ArrayList<Kweet> FindKweets(String search);
    Kweet AddKweet(Kweet kweet);

    List<Kweet> GetKweets();
}
