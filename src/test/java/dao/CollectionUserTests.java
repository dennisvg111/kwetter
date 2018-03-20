package dao;

import domain.Kweet;
import domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class CollectionUserTests {
    private static IKweetDao kweetDao;
    private static IUserDao userDao;

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @Before
    public void setUp() {
        kweetDao = new CollectionKweetDao();
        userDao = new CollectionUserDao();
    }

    private User AddUser() {
        User user = new User("username" + userDao.All().size(), "password", User.Role.USER);
        user.setId(userDao.All().size());
        return userDao.Create(user);
    }

    @Test
    public void CreateUser() {

        User user = new User("Dennis", "test", User.Role.USER);
        User created = userDao.Create(user);
        assertNotNull(created);

    }

    @Test
    public void ReadUser() {

        User newUser = AddUser();

        User found = userDao.Read(newUser.getId());
        assertNotNull(found);

        User notFound = userDao.Read(100);
        assertNull(notFound);

    }

    @Test
    public void FindUser() {

        userDao.Create(new User("username", "password", User.Role.USER));

        User found = userDao.FindUser("username");
        assertNotNull(found);

        User notFound = userDao.FindUser("unknown");
        assertNull(notFound);

    }

    @Test
    public void UpdateUser() {

        User newUser = AddUser();

        User user = userDao.Read(newUser.getId());
        String bio = "Updated bio";
        user.setBio(bio);
        User updated = userDao.Update(user);
        assertNotNull(updated);
        assertEquals(bio, updated.getBio());

    }

    @Test
    public void AllUsers() {

        AddUser();
        AddUser();

        List<User> foundUsers = userDao.All();
        assertEquals(2, foundUsers.size());

    }

    @Test
    public void DeleteUser() {

        userDao.Create(new User("username", "password", User.Role.USER));

        int initialLength = userDao.All().size();
        User user = userDao.FindUser("username");
        userDao.Delete(user.getId());

        int newLength = userDao.All().size();
        assertNotEquals(initialLength, newLength);

    }
}
