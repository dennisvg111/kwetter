package dao;

import domain.Role;
import domain.User;

import java.util.List;

public interface IUserDao {
    User FindUser(String name);
    User Create(User user);
    User Read(long id);
    User Update(User user);
    void Delete(long id);
    List<User> All();

    List<User> GetFollowing(long id);
    List<User> GetFollowers(long id);

    boolean Follow(Long userId, Long followedUserId);

    boolean Unfollow(Long userId, Long followedUserId);

    User SetRoles(User user, Role[] roles);
}
