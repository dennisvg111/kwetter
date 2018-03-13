package dao;

import domain.User;

import java.util.ArrayList;

public class CollectionUserDao implements IUserDao {
    private final ArrayList<User> users;

    public CollectionUserDao() {
        users = new ArrayList<>();
    }

    @Override
    public User FindUser(String name) {
        for (User user : users)
        {
            if (user.getName().toLowerCase().equals(name.toLowerCase()))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public User GetUser(long id) {
        for (User user : users)
        {
            if (user.getId() == id)
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean AddUser(User user) {
        for (User oldUser : users)
        {
            if (oldUser.getName().toLowerCase().equals(user.getName().toLowerCase()))
            {
                return false;
            }
        }
        users.add(user);
        return true;
    }
}
