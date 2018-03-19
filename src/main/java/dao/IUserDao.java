package dao;

import domain.User;

import java.util.List;

public interface IUserDao {
    User FindUser(String name);
    User Create(User user);
    User Read(long id);
    User Update(User user);
    void Delete(User user);
    List<User> All();

    List<User> GetFollowing(long id);
    List<User> GetFollowers(long id);

    boolean Follow(Long userId, Long followedUserId);

    boolean Unfollow(Long userId, Long followedUserId);
}
