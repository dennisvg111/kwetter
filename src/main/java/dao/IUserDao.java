package dao;

import domain.User;

public interface IUserDao {
    User FindUser(String name);
    User GetUser(long id);
    User AddUser(User user);
}
