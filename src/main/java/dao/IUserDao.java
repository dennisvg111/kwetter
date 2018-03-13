package dao;

import domain.User;

public interface IUserDao {
    User FindUser(String name);
    User GetUser(long id);
    boolean AddUser(User user);
}
