package dao;

import domain.Role;
import domain.User;

import java.util.ArrayList;
import java.util.List;

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
    public User Create(User user) {
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

    @Override
    public User Read(long id) {
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
    public User Update(User user) {
        User existingUser = Read(user.getId());
        if (existingUser == null)
        {
            return user;
        }
        int index = users.indexOf(existingUser);
        users.set(index, user);
        return user;
    }

    @Override
    public void Delete(long id) {
        users.remove(Read(id));
    }

    @Override
    public List<User> All() {
        return users;
    }

    @Override
    public List<User> GetFollowing(long id) {
        User foundUser = Read(id);
        return foundUser.getFollowing();
    }

    @Override
    public List<User> GetFollowers(long id) {
        User foundUser = Read(id);
        return foundUser.getFollowers();
    }

    @Override
    public boolean Follow(Long userId, Long followedUserId) {
        User user = Read(userId);
        User otherUser = Read(followedUserId);
        if (user.IsFollowing(otherUser))
        {
            return false;
        }
        user.addFollowing(otherUser);
        return true;
    }

    @Override
    public boolean Unfollow(Long userId, Long followedUserId) {
        User user = Read(userId);
        User otherUser = Read(followedUserId);
        if (!user.IsFollowing(otherUser))
        {
            return false;
        }
        user.RemoveFollowing(otherUser);
        return true;
    }

    @Override
    public User SetRoles(User user, Role[] roles) {
        user = Read(user.getId());
        user.getRoles().clear();
        for (Role role : roles)
        {
            user.getRoles().add(new Role(role.getName()));
        }
        return user;
    }
}
