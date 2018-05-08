package daomanagers;

import com.google.common.hash.Hashing;
import com.mysql.cj.core.util.StringUtils;
import dao.IUserDao;
import dao.JPA;
import domain.Role;
import domain.User;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Stateless
public class UserManager implements Serializable {
    @JPA
    @Inject
    private IUserDao dao;

    public UserManager() {
        super();
    }

    public User FindUser(String name)
    {
        return dao.FindUser(name);
    }

    public User GetUser(long id)
    {
        return dao.Read(id);
    }

    //Return user to immediately be able to log in
    public User AddUser(User user)
    {
        String password = user.getHashedPassword();

        if (StringUtils.isNullOrEmpty(password))
        {
            return null;
        }

        // hash password
        String sha256HexPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        user.setHashedPassword(sha256HexPassword);

        return dao.Create(user);
    }

    public List<User> GetFollowing(long id) {return dao.GetFollowing(id); }

    public boolean Follow(Long userId, Long followedUserId)
    {
        if (userId == followedUserId)
        {
            return false;
        }
        return dao.Follow(userId, followedUserId);
    }

    public boolean Unfollow(Long userId, Long followedUserId) { return dao.Unfollow(userId, followedUserId); }

    public User EditUser(User user) {
        return dao.Update(user);
    }

    public void RemoveUser(long id) { dao.Delete(id); }

    public List<User> GetFollowers(long id) { return dao.GetFollowers(id); }

    public List<User> GetUsers() {
        return dao.All();
    }

    public User SetRoles(User user, Role... roles)
    {
        List<Role> uniqueRoles = new ArrayList<>();
        for (Role role : roles)
        {
            if (!uniqueRoles.stream().anyMatch(r -> r.getName().equals(role.getName())))
            {
                uniqueRoles.add(role);
            }
        }
        return dao.SetRoles(user, uniqueRoles.toArray(new Role[uniqueRoles.size()]));
    }
}
