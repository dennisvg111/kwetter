package dao;

import domain.User;

import java.util.ArrayList;

public class CollectionUserDao implements IUserDao {
    private final ArrayList<User> users;
    private static long currentUserId = 0;

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
    public User AddUser(User user) {
        for (User oldUser : users)
        {
            if (oldUser.getName().toLowerCase().equals(user.getName().toLowerCase()))
            {
                return null;
            }
        }
        user.setId(currentUserId);
        currentUserId++;
        users.add(user);
        return user;
    }
}
